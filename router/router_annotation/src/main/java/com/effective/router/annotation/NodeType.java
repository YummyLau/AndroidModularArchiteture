package com.effective.router.annotation;

/**
 * 节点类型，当前只支持activity
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public enum NodeType {

    ACTIVITY(0, "android.app.Activity"),
    INVALID(-1, "invalid node type, currently only activity allowed");

    int id;
    String className;

    NodeType(int id, String className) {
        this.id = id;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public NodeType setId(int id) {
        this.id = id;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public NodeType setClassName(String className) {
        this.className = className;
        return this;
    }

    public static NodeType parse(String name) {
        for (NodeType nodeType : NodeType.values()) {
            if (nodeType.getClassName().equals(name)) {
                return nodeType;
            }
        }
        return INVALID;
    }

}
