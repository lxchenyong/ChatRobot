package com.jeff.robotsmailhelper.listener;

import org.json.JSONException;

/**
 * 回调监听
 * Created by chen_yong on 2016/6/24.
 */
public interface VolleyLoaderListener {

    void loadSucceed(Object response) throws JSONException;

    void loadFailed(String message);
}
