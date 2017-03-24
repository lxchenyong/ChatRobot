package com.jeff.robotsmailhelper.interfacecontract;

import com.jeff.robotsmailhelper.model.bean.MsgInfo;

import java.util.List;

/**
 * 聊天接口类
 * Created by Administrator on 2017/3/24.
 */

public interface IChatContract {

    interface IChatView{
        /**
         * 是否显示加载条
         */
        void isShowSwipeLayout(boolean flag);

        /**
         * 显示界面数据
         */
        void showData(String data);

        /**
         * 显示Toast
         *
         * @param message 信息
         */
        void showToast(String message);
    }
    interface IChatPresenter{
        void loadData(String mContent);
    }
}
