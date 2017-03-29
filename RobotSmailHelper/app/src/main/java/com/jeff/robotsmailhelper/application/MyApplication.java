package com.jeff.robotsmailhelper.application;

import org.litepal.LitePal;

import bz.sunlight.volleylibrary.StorageApplication;

/**
 * 自定义application
 * Created by chen_yong on 2017/3/29.
 */

public class MyApplication extends StorageApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
