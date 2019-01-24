package com.effective.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * route节点
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface  RouteNode {

    /**
     * path of one route
     */
    String path();

    /**
     * The priority of route.
     */
    int priority() default -1;

    /**
     * description of the activity, user for gen route table
     */
    String desc() default "";
}
