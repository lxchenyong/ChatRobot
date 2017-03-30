package com.jeff.robotsmailhelper.chat.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeff.robotsmailhelper.R;
import com.jeff.robotsmailhelper.chat.model.bean.MsgInfo;

import java.net.URL;
import java.util.List;

/**
 * 聊天适配器
 * Created by chen_yong on 2017/3/24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContent;
    private List<MsgInfo> msgInfoList;
    private LayoutInflater inflater;
    private Drawable drawable = null;

    public ChatAdapter(Context mContent, List<MsgInfo> msgInfoList) {
        this.mContent = mContent;
        this.msgInfoList = msgInfoList;
        this.inflater = LayoutInflater.from(mContent);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.layout_msg_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MsgInfo msgInfo = msgInfoList.get(position);
        /**
         * 这里实现ImageGetter接口，下载图片，一定要开新线程！！！
         */
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String msource) {
                final String source = msource;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url;
                        try {
                            url = new URL(source);
                            drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                                drawable.getIntrinsicHeight());
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return drawable;
            }
        };
        if (msgInfo.getChatType() == MsgInfo.TYPE_ROBOT) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.tv_Error.setVisibility(View.GONE);
            holder.left_msg.setText(Html.fromHtml(msgInfo.getChatInfo(), imgGetter, null));
            holder.left_msg.setMovementMethod(LinkMovementMethod.getInstance());
//            holder.left_msg.setMovementMethod(ScrollingMovementMethod.getInstance());
//            holder.left_msg.setText(msgInfo.getChatInfo());
        } else if(msgInfo.getChatType() == MsgInfo.TYPE_ERROR){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.tv_Error.setVisibility(View.VISIBLE);
            holder.tv_Error.setText(msgInfo.getChatInfo());
        } else{
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.tv_Error.setVisibility(View.GONE);
            holder.right_msg.setText(Html.fromHtml(msgInfo.getChatInfo(), imgGetter, null));
            holder.right_msg.setMovementMethod(LinkMovementMethod.getInstance());
//            holder.left_msg.setText(msgInfo.getChatInfo());
        }
    }


    @Override
    public int getItemCount() {
        return msgInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView left_msg;
        TextView right_msg;
        TextView tv_Error;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = (LinearLayout) itemView.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) itemView.findViewById(R.id.right_layout);
            left_msg = (TextView) itemView.findViewById(R.id.left_msg);
            right_msg = (TextView) itemView.findViewById(R.id.right_msg);
            tv_Error = (TextView) itemView.findViewById(R.id.tv_error);
        }
    }
}
