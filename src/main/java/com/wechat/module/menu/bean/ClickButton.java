package com.wechat.module.menu.bean;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ClickButton extends Button {

    private String type;
    private String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
