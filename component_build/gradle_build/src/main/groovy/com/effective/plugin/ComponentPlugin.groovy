package com.effective.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 编写 ComponentPlugin 插件
 */
class ComponentPlugin implements Plugin<Project> {

    private static final String TAG = "ComponentPlugin -- "
    private static final String MAIN_MODULE_NAME = "mainModuleName"

    //默认是app，直接运行assembleRelease的时候，等同于运行app:assembleRelease
    String compileModule = "app"

    void apply(Project project) {

        //新增 componentBuild 扩展属性 ："applicationName" 和 "isRegisterCompoAuto"
        project.extensions.create('componentBuild', Extension)

        String taskNames = project.gradle.startParameter.taskNames.toString()
        System.out.println(TAG + "taskNames is " + taskNames)

        //获取当前运行的模块
        String module = project.path.replace(":", "")
        System.out.println(TAG + "current module is " + module)

        AssembleTask assembleTask = getTaskInfo(project.gradle.startParameter.taskNames)

        if (assembleTask.isAssemble) {
            fetchMainModulename(project, assembleTask)
            System.out.println("compile module is " + compileModule)
        }

        //需要在特定的模块中声明 isRunAlone，用于判断是否单独运行
        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("you should set isRunAlone in " + module + "'s gradle.properties")
        }

        boolean isRunAlone = Boolean.parseBoolean((project.properties.get("isRunAlone")))
        String mainModuleName = project.rootProject.property(MAIN_MODULE_NAME)


        if (isRunAlone && assembleTask.isAssemble) {
            //如果运行的模块就是app模块，或者当前运行的模块就是我们配置的mainmodulename，则默认需要单独运行，其他组件强制修改为false
            if (module.equals(compileModule) || module.equals(mainModuleName)) {
                isRunAlone = true
            } else {
                isRunAlone = false
            }
        }

        project.setProperty("isRunAlone", isRunAlone)
        System.out.println(TAG + "setProperty isRunAlone(" + isRunAlone + ")")

        //根据配置添加各种组件依赖，并且自动化生成组件加载代码
        if (isRunAlone) {
            project.apply plugin: 'com.android.application'
            System.out.println(TAG + "project.apply plugin: com.android.application")

            //对于组件，则需要读取alone目录进行运行
            if (!module.equals(mainModuleName)) {
                project.android.sourceSets {
                    main {
                        manifest.srcFile 'src/main/runalone/AndroidManifest.xml'
                        java.srcDirs = ['src/main/java', 'src/main/runalone/java']
                        res.srcDirs = ['src/main/res', 'src/main/runalone/res']
                        assets.srcDirs = ['src/main/assets', 'src/main/runalone/assets']
                        jniLibs.srcDirs = ['src/main/jniLibs', 'src/main/runalone/jniLibs']
                    }
                }
            }
            if (assembleTask.isAssemble && module.equals(compileModule)) {
                compileComponents(assembleTask, project)
                project.android.registerTransform(new CodeTransform(project))
            }
        } else {
            project.apply plugin: 'com.android.library'
            System.out.println(TAG + "project.apply plugin: com.android.library")
        }
    }

    /**
     * 根据当前的task，获取要运行的组件，规则如下：
     * assembleRelease ---app
     * app:assembleRelease :app:assembleRelease ---app
     * sharecomponent:assembleRelease :sharecomponent:assembleRelease ---sharecomponent
     * @param assembleTask
     */
    private void fetchMainModulename(Project project, AssembleTask assembleTask) {
        //需要在根目录 gradle.properties 中设置 mainmodulename
        if (!project.rootProject.hasProperty(MAIN_MODULE_NAME)) {
            throw new RuntimeException("you should set compilemodule in rootproject's gradle.properties")
        }
        if (assembleTask.modules.size() > 0 && assembleTask.modules.get(0) != null
                && assembleTask.modules.get(0).trim().length() > 0
                && !assembleTask.modules.get(0).equals("all")) {
            compileModule = assembleTask.modules.get(0)
        } else {
            compileModule = project.rootProject.property(MAIN_MODULE_NAME)
        }
        if (compileModule == null || compileModule.trim().length() <= 0) {
            compileModule = "app"
        }
        System.out.println(TAG + "fetchMainModulename : " + compileModule);
    }

    private AssembleTask getTaskInfo(List<String> taskNames) {
        System.out.println(TAG + "getTaskInfo")
        AssembleTask assembleTask = new AssembleTask()
        for (String task : taskNames) {

            System.out.println(TAG + "task(" + task + ")")

            if (task.toUpperCase().contains("ASSEMBLE")
                    || task.contains("aR")
                    || task.toUpperCase().contains("TINKER")
                    || task.toUpperCase().contains("INSTALL")
                    || task.toUpperCase().contains("RESGUARD")) {
                if (task.toUpperCase().contains("DEBUG")) {
                    assembleTask.isDebug = true
                }
                System.out.println(TAG + "task is debug (" + assembleTask.isDebug + ")")
                assembleTask.isAssemble = true
                String[] strs = task.split(":")
                assembleTask.modules.add(strs.length > 1 ? strs[strs.length - 2] : "all")
                break
            }
        }
        return assembleTask
    }

    /**
     * 自动添加依赖，只在运行assemble任务的才会添加依赖，因此在开发期间组件之间是完全感知不到的，这是做到完全隔离的关键
     * 支持两种语法：module或者groupId:artifactId:version(@aar),前者之间引用module工程，后者使用maven中已经发布的aar
     * @param assembleTask
     * @param project
     */
    private void compileComponents(AssembleTask assembleTask, Project project) {
        String components
        if (assembleTask.isDebug) {
            components = (String) project.properties.get("debugCompileComponent")
        } else {
            components = (String) project.properties.get("releaseCompileComponent")
        }

        if (components == null || components.length() == 0) {
            System.out.println("there is no add dependencies ")
            return
        }
        String[] compileComponents = components.split(",")
        if (compileComponents == null || compileComponents.length == 0) {
            System.out.println("there is no add dependencies ")
            return
        }
        for (String str : compileComponents) {
            System.out.println("comp is " + str)
            if (str.contains(":")) {
                /**
                 * 示例语法:groupId:artifactId:version(@aar)
                 * compileComponent=com.luojilab.reader:readercomponent:1.0.0
                 * 注意，前提是已经将组件aar文件发布到maven上，并配置了相应的repositories
                 */
                project.dependencies.add("implementation", str)
                System.out.println("add dependencies lib  : " + str)
            } else {
                /**
                 * 示例语法:module
                 * compileComponent=readercomponent,sharecomponent
                 */
                project.dependencies.add("implementation", project.project(':' + str))
                System.out.println("add dependencies project : " + str)
            }
        }
    }

    private class AssembleTask {
        boolean isAssemble = false
        boolean isDebug = false
        List<String> modules = new ArrayList<>()
    }

}