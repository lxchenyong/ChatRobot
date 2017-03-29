package com.jeff.robotsmailhelper.model.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 消息类
 * Created by chen_yong on 2017/3/24.
 */

public class MsgInfo extends DataSupport {
    public static final int TYPE_ROBOT = 0;
    public static final int TYPE_USER  = 1;
    public static final int TYPE_ERROR = 2;


    private String chatInfo;
    private int chatType;

    public MsgInfo(String chatInfo, int chatType) {
        this.chatInfo = chatInfo;
        this.chatType = chatType;
    }

    public String getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(String chatInfo) {
        this.chatInfo = chatInfo;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

}
