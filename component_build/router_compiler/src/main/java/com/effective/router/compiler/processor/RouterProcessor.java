package com.effective.router.compiler.processor;

import com.effective.router.annotation.Autowired;
import com.effective.router.annotation.Node;
import com.effective.router.annotation.NodeType;
import com.effective.router.annotation.RouteNode;
import com.effective.router.annotation.RouteUtils;
import com.effective.router.compiler.Logger;
import com.effective.router.compiler.util.FileUtils;
import com.effective.router.compiler.util.TypeUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.effective.router.compiler.Constants.ACTIVITY;
import static com.effective.router.compiler.Constants.ANNOTATION_TYPE_ROUTE_NODE;
import static com.effective.router.compiler.Constants.BASECOMPROUTER;
import static com.effective.router.compiler.Constants.KEY_HOST_NAME;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
@AutoService(Processor.class)
@SupportedOptions(KEY_HOST_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({ANNOTATION_TYPE_ROUTE_NODE})
public class RouterProcessor extends AbstractProcessor {

    private static final String mRouteMapperFieldName = "routeMapper";
    private static final String mParamsMapperFieldName = "paramsMapper";

    private Logger logger;

    private Filer mFiler;
    private Types types;
    private Elements elements;

    private TypeMirror type_String;

    private ArrayList<Node> routerNodes;
    private TypeUtils typeUtils;
    private String host = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        routerNodes = new ArrayList<>();

        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elements = processingEnv.getElementUtils();
        typeUtils = new TypeUtils(types, elements);

        type_String = elements.getTypeElement("java.lang.String").asType();
        logger = new Logger(processingEnv.getMessager());

        Map<String, String> options = processingEnv.getOptions();

        //从gradle中读取，不同组件配置不同的host
        //  javaCompileOptions {
        //            annotationProcessorOptions {
        //                arguments = [host: "app"]
        //            }
        //        }
        if (MapUtils.isNotEmpty(options)) {
            host = options.get(KEY_HOST_NAME);
            logger.info(">>> host is " + host + " <<<");
        }
        if (host == null || host.equals("")) {
            host = "default";
        }
        logger.info(">>> RouteProcessor init. <<<");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(set)) {
            //获取所有 routeNode 节点
            Set<? extends Element> routeNodes = roundEnvironment.getElementsAnnotatedWith(RouteNode.class);
            try {
                logger.info(">>> Found routes, start... <<<");
                parseRouteNodes(routeNodes);
            } catch (Exception e) {
                logger.error(e);
            }
            generateRouterImpl();
            generateRouterTable();
            return true;
        }
        return false;
    }

    /**
     * generate HostRouterTable.txt
     */
    private void generateRouterTable() {
        String fileName = RouteUtils.genRouterTable(host);
        if (FileUtils.createFile(fileName)) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("auto generated, do not change !!!! \n\n");
            stringBuilder.append("HOST : " + host + "\n\n");

            for (Node node : routerNodes) {
                stringBuilder.append(node.getDesc() + "\n");
                stringBuilder.append(node.getPath() + "\n");
                Map<String, String> paramsType = node.getParamsDesc();
                if (MapUtils.isNotEmpty(paramsType)) {
                    for (Map.Entry<String, String> types : paramsType.entrySet()) {
                        stringBuilder.append(types.getKey() + ":" + types.getValue() + "\n");
                    }
                }
                stringBuilder.append("\n");
            }
            FileUtils.writeStringToFile(fileName, stringBuilder.toString(), false);
        }
    }

    /**
     * 生成路由实现类，位于build目录下 com.effective.gen.rounter
     */
    private void generateRouterImpl() {

        String claName = RouteUtils.genHostUIRouterClass(host);

        //pkg
        String pkg = claName.substring(0, claName.lastIndexOf("."));
        //simpleName
        String cn = claName.substring(claName.lastIndexOf(".") + 1);
        // superClassName
        ClassName superClass = ClassName.get(elements.getTypeElement(BASECOMPROUTER));

        MethodSpec initHostMethod = generateInitHostMethod();
        MethodSpec initMapMethod = generateInitMapMethod();

        try {
            JavaFile.builder(pkg, TypeSpec.classBuilder(cn)
                    .addModifiers(PUBLIC)
                    .superclass(superClass)
                    .addMethod(initHostMethod)
                    .addMethod(initMapMethod)
                    .build()
            ).build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRouteNodes(Set<? extends Element> routeElements) {

        TypeMirror type_Activity = elements.getTypeElement(ACTIVITY).asType();

        for (Element element : routeElements) {
            TypeMirror tm = element.asType();

            //获取 routeNode 即诶单
            RouteNode route = element.getAnnotation(RouteNode.class);

            //判断该节点是否是 activity 的子类
            if (types.isSubtype(tm, type_Activity)) {

                logger.info(">>> Found activity route: " + tm.toString() + " <<<");

                Node node = new Node();

                //读取path
                String path = route.path();
                checkPath(path);

                node.setPath(path);
                node.setDesc(route.desc());
                node.setPriority(route.priority());
                node.setNodeType(NodeType.ACTIVITY);
                node.setRawType(element);

                Map<String, Integer> paramsType = new HashMap<>();
                Map<String, String> paramsDesc = new HashMap<>();


                for (Element field : element.getEnclosedElements()) {
                    //如果当前作用于变量且是 Autowired
                    if (field.getKind().isField() && field.getAnnotation(Autowired.class) != null) {
                        Autowired paramConfig = field.getAnnotation(Autowired.class);
                        paramsType.put(StringUtils.isEmpty(paramConfig.name())
                                ? field.getSimpleName().toString() : paramConfig.name(), typeUtils.typeExchange(field));
                        paramsDesc.put(StringUtils.isEmpty(paramConfig.name())
                                ? field.getSimpleName().toString() : paramConfig.name(), typeUtils.typeDesc(field));
                    }
                }
                //参数类型集合
                node.setParamsType(paramsType);

                //参数描述集合
                node.setParamsDesc(paramsDesc);
                if (!routerNodes.contains(node)) {
                    routerNodes.add(node);
                }
            } else {
                throw new IllegalStateException("only activity can be annotated by RouteNode");
            }
        }
    }

    /**
     * 判断是否 path 的格式是否正确
     *
     * @param path
     */
    private void checkPath(String path) {
        if (path == null || path.isEmpty() || !path.startsWith("/"))
            throw new IllegalArgumentException("path cannot be null or empty,and should start with /,this is:" + path);

        if (path.contains("//") || path.contains("&") || path.contains("?"))
            throw new IllegalArgumentException("path should not contain // ,& or ?,this is:" + path);

        if (path.endsWith("/"))
            throw new IllegalArgumentException("path should not endWith /,this is:" + path
                    + ";or append a token:index");
    }

    /**
     * create init host method
     */
    private MethodSpec generateInitHostMethod() {
        TypeName returnType = TypeName.get(type_String);

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("getHost")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addStatement("return $S", host);

        return openUriMethodSpecBuilder.build();
    }

    /**
     * create init map method
     */
    private MethodSpec generateInitMapMethod() {
        TypeName returnType = TypeName.VOID;

        MethodSpec.Builder openUriMethodSpecBuilder = MethodSpec.methodBuilder("initMap")
                .returns(returnType)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC);

        openUriMethodSpecBuilder.addStatement("super.initMap()");

        for (Node node : routerNodes) {
            openUriMethodSpecBuilder.addStatement(
                    mRouteMapperFieldName + ".put($S,$T.class)",
                    node.getPath(),
                    ClassName.get((TypeElement) node.getRawType()));

            // Make map body for paramsType
            StringBuilder mapBodyBuilder = new StringBuilder();
            Map<String, Integer> paramsType = node.getParamsType();
            if (MapUtils.isNotEmpty(paramsType)) {
                for (Map.Entry<String, Integer> types : paramsType.entrySet()) {
                    mapBodyBuilder.append("put(\"").append(types.getKey()).append("\", ").append(types.getValue()).append("); ");
                }
            }
            String mapBody = mapBodyBuilder.toString();
            logger.info(">>> mapBody: " + mapBody + " <<<");
            if (!StringUtils.isEmpty(mapBody)) {
                openUriMethodSpecBuilder.addStatement(
                        mParamsMapperFieldName + ".put($T.class,"
                                + "new java.util.HashMap<String, Integer>(){{" + mapBody + "}}" + ")",
                        ClassName.get((TypeElement) node.getRawType()));
            }
        }

        return openUriMethodSpecBuilder.build();
    }
}
