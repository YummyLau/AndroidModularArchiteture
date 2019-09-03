package com.effective.router.compiler;

/**
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */

public interface Constants {


    ///////////////////////////////////////////////////////////////////////////
    // Options of processor
    ///////////////////////////////////////////////////////////////////////////
    String KEY_HOST_NAME = "host";

    String ANNOTATION_FACADE_PKG = "com.effective.router.annotation";
    String ANNOTATION_TYPE_ROUTE_NODE = ANNOTATION_FACADE_PKG + ".RouteNode";
    String ANNOTATION_TYPE_AUTOWIRED = ANNOTATION_FACADE_PKG + ".Autowired";

    String PREFIX_OF_LOGGER = "[Router-Anno-Compiler]-- ";


    // System interface
    String ACTIVITY = "android.app.Activity";
    String FRAGMENT = "android.app.Fragment";
    String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    String SERVICE = "android.app.Service";
    String PARCELABLE = "android.os.Parcelable";

    // Java type
    String LANG = "java.lang";
    String BYTE = LANG + ".Byte";
    String SHORT = LANG + ".Short";
    String INTEGER = LANG + ".Integer";
    String LONG = LANG + ".Long";
    String FLOAT = LANG + ".Float";
    String DOUBEL = LANG + ".Double";
    String BOOLEAN = LANG + ".Boolean";
    String STRING = LANG + ".String";



    String ISYRINGE = "com.effective.router.core.ui.ISyringe";
    String JSON_SERVICE = "com.effective.router.core.service.JsonService";
    String BASECOMPROUTER = "com.effective.router.core.ui.BaseComponentRouter";

}
