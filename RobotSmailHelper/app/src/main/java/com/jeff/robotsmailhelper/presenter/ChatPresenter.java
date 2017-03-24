package com.jeff.robotsmailhelper.presenter;


import com.jeff.robotsmailhelper.interfacecontract.IChatContract;
import com.jeff.robotsmailhelper.listener.VolleyLoaderListener;
import com.jeff.robotsmailhelper.model.biz.ChatBiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 聊天
 * Created by chen_yong on 2017/3/24.
 */

public class ChatPresenter implements IChatContract.IChatPresenter{

    private IChatContract.IChatView mIChatView;
    private ChatBiz mChatBiz;
    private String data = null;

    public ChatPresenter(IChatContract.IChatView mIChatView, ChatBiz mChatBiz) {
        this.mIChatView = mIChatView;
        this.mChatBiz = mChatBiz;
    }

    @Override
    public void loadData(String mContent) {
        try {
            mChatBiz.loadRobotContent(URLEncoder.encode(mContent, "utf-8"), new VolleyLoaderListener() {
                @Override
                public void loadSucceed(Object response) throws JSONException {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String code = String.valueOf(jsonObject.getString("code"));
                    switch (code){
                        case "100000":
                            //文本类数据
                            data = jsonObject.getString("text");
                            break;
                        case "200000":
                            //网址类数据
                            data = jsonObject.getString("text")+"<br>"+
                                    "<a href="+"\""+jsonObject.getString("url")+"\""+">点我查看</a>";
                            break;
                        case "302000":
                            //新闻
                            JSONArray jsonNewsArray = jsonObject.getJSONArray("list");
                            StringBuffer detail_news = new StringBuffer();
                            for(int i = 0 ; i < jsonNewsArray.length() ; i++){
                                JSONObject object = jsonNewsArray.getJSONObject(i);
                                detail_news.append("标题:"+object.getString("article")+"<br>"+
                                        "来源:"+object.getString("source")+"<br>"+
                                        "详情:"+"<a href="+"\""+object.getString("detailurl")+"\""+">点我查看</a>"+"<br>"
                                );
                            }
                            data = jsonObject.getString("text")+"<br>"+detail_news.toString();
                            break;
                        case "304000":
                            //应用、软件、下载
                            break;
                        case "305000":
                            //列车
                            JSONArray jsonTrainArray = jsonObject.getJSONArray("list");
                            StringBuffer detail_train = new StringBuffer();
                            for(int i = 0 ; i < jsonTrainArray.length() ; i++){
                                JSONObject object = jsonTrainArray.getJSONObject(i);
                                detail_train.append("车次:"+object.getString("trainnum")+"<br>"+
                                        "起始站:"+object.getString("start")+"<br>"+
                                        "终点站:"+object.getString("terminal")+"<br>"+
                                        "出发时间:"+object.getString("starttime")+"<br>"+
                                        "到达时间:"+object.getString("endtime")+"<br>"+
                                        "详情:"+"<a href="+"\""+object.getString("detailurl")+"\""+">点我查看</a>"+"<br>"
                                );
                            }
                            data = jsonObject.getString("text")+"<br>"+detail_train.toString();
                            break;
                        case "306000":
                            //航班
                            JSONArray jsonFlightArray = jsonObject.getJSONArray("list");
                            StringBuffer detail_flight = new StringBuffer();
                            for(int i = 0 ; i < jsonFlightArray.length() ; i++){
                                JSONObject object = jsonFlightArray.getJSONObject(i);
                                detail_flight.append("航班:"+object.getString("flight")+"<br>"+
                                        "出发时间:"+object.getString("starttime")+"<br>"+
                                        "到达时间:"+object.getString("endtime")+"<br>"+
                                        "<img src="+"\""+object.getString("icon")+"\""+"/><br>"
                                );
                            }
                            data = jsonObject.getString("text")+"<br>"+detail_flight.toString();
                            break;
                        case "308000":
                            //菜谱、视频、小说
                            JSONArray jsonMenuArray = jsonObject.getJSONArray("list");
                            StringBuffer detail_menu = new StringBuffer();
                            for(int i = 0 ; i < jsonMenuArray.length() ; i++){
                                JSONObject object = jsonMenuArray.getJSONObject(i);
                                detail_menu.append("菜名:"+object.getString("name")+"<br>"+
                                        "原料:"+object.getString("info")+"<br>"+
                                        "详情:"+"<a href="+"\""+object.getString("detailurl")+"\""+">点我查看</a>"+"<br>"+
                                        "<img src="+"\""+object.getString("icon")+"\""+"/><br>"
                                );
                            }
                            data = jsonObject.getString("text")+"<br>"+detail_menu.toString();
                            break;
                        case "309000":
                            //酒店
                            break;
                        case "311000":
                            //价格
                            break;
                        case "40001":
                            //key的长度错误（32位）
                           mIChatView.showToast("（系统）key的长度错误（32位）");
                            break;
                        case "40002":
                            //请求内容为空
                            mIChatView.showToast( "（系统）请求内容为空");
                            break;
                        case "40003":
                            //key错误或帐号未激活
                            mIChatView.showToast( "（系统）key错误或帐号未激活");
                            break;
                        case "40004":
                            //当天请求次数已用完
                            mIChatView.showToast( "（系统）当天请求次数已用完");
                            break;
                        case "40005":
                            //暂不支持该功能
                            mIChatView.showToast( "（系统）暂不支持该功能");
                            break;
                        case "40006":
                            //服务器升级中
                            mIChatView.showToast( "（系统）服务器升级中");
                            break;
                        case "40007":
                            //服务器数据格式异常
                            mIChatView.showToast( "（系统）服务器数据格式异常");
                            break;
                    }
                    mIChatView.showData(data);
                }

                @Override
                public void loadFailed(String message) {
                    mIChatView.showToast(message);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
