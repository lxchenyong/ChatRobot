package com.jeff.robotsmailhelper.chat.model.biz;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jeff.robotsmailhelper.listener.GenericErrorListener;
import com.jeff.robotsmailhelper.listener.VolleyLoaderListener;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import bz.sunlight.volleylibrary.VolleyHelper;


/**
 * 聊天Biz
 * Created by chen_yong on 2017/3/24.
 */

public class ChatBiz {
    private static final String API_KEY = "60b362d7dad74e3d9ed533a55c0fefa9";
    private VolleyHelper volleyHelper;

    public ChatBiz() {
        if (volleyHelper == null) {
            volleyHelper = new VolleyHelper();
        }
    }

    /**
     * 机器人回的信息
     *
     * @param mContent             发送消息
     * @param volleyLoaderListener 监听
     */
    public void loadRobotContent(String mContent, final VolleyLoaderListener volleyLoaderListener) {
        String url = "http://www.tuling123.com/openapi/api?key=" + API_KEY + "&info=" + mContent;
        volleyHelper.newJsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    volleyLoaderListener.loadSucceed(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new GenericErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                volleyLoaderListener.loadFailed(VolleyHelper.getGeneralErrorMessage(error));
            }
        }, null);
    }
}
