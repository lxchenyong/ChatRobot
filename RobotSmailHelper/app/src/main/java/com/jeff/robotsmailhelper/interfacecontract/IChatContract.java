package com.jeff.robotsmailhelper.interfacecontract;

import android.content.Context;


/**
 * 聊天接口类
 * Created by Administrator on 2017/3/24.
 */

public interface IChatContract {

    interface IChatView {
        /**
         * 是否显示加载条
         */
        void isShowSwipeLayout(boolean flag);

        /**
         * 显示界面数据
         */
        void showData(String data);

        /**
         * 显示说的话内容
         *
         * @param message 内容
         */
        void showSpeak(String message);

        /**
         * @return 得到上下文
         */
        Context getContext();

        /**
         * 显示Toast
         *
         * @param message 信息
         */
        void showToast(String message);
    }

    interface IChatPresenter {
        void loadData(String mContent);

        /**
         * 初始化语音识别
         */
        void initSpeech();
    }
}
