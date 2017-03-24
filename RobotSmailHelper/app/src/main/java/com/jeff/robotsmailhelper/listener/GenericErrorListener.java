package com.jeff.robotsmailhelper.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import bz.sunlight.volleylibrary.VolleyHelper;


/**
 * 针对 Volley 回应的标准错误处理监听器
 * <p/>
 * 在服务端返回 HTTP StatusCode 403 时，跳转至登录界面，要求用户重新输入密码。
 */
public class GenericErrorListener implements Response.ErrorListener {
    private Activity activity;

    public GenericErrorListener(Activity activity) {
        if (activity == null)
            throw new NullPointerException("activity should not be null");

        this.activity = activity;
    }

    public GenericErrorListener(Fragment fragment) {
        this(fragment.getActivity());
    }

    public GenericErrorListener() {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (activity == null)
            return;
        // 显示错误信息
        String message = VolleyHelper.getGeneralErrorMessage(error);
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();


        // 跳转至登录界面
        if (error.networkResponse != null && error.networkResponse.statusCode == 403) {
//            Intent intent = new Intent(activity, LoginActivity.class);
//            activity.startActivity(intent);
//            activity.finish();
        }
    }

}
