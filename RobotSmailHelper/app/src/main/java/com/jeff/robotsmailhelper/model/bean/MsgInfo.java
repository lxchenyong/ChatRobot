package com.jeff.robotsmailhelper.model.bean;

import org.litepal.crud.DataSupport;

/**
 * 消息类
 * Created by chen_yong on 2017/3/24.
 */

public class MsgInfo extends DataSupport {
    public static final int TYPE_ROBOT = 0;
    public static final int TYPE_USER  = 1;

    private String chatInfo;
    private int chatObj;

    public MsgInfo(String chatInfo, int chatObj) {
        this.chatInfo = chatInfo;
        this.chatObj = chatObj;
    }

    public String getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(String chatInfo) {
        this.chatInfo = chatInfo;
    }

    public int getChatObj() {
        return chatObj;
    }

    public void setChatObj(int chatObj) {
        this.chatObj = chatObj;
    }
}
