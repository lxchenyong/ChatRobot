package com.jeff.robotsmailhelper.chat.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeff.robotsmailhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户指南适配器
 * Created by chen_yong on 2017/4/1.
 */

public class UserGuideAdapter extends RecyclerView.Adapter<UserGuideAdapter.ViewHolder> {

    private List<Integer> lists;
    private LayoutInflater inflater;

    public UserGuideAdapter(Context mContext) {
        loadData();
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.user_guide_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(lists.get(position));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
    private List<Integer> loadData(){
        lists = new ArrayList<>();
        lists.add(R.string.user_guide_one);
        lists.add(R.string.user_guide_two);
        lists.add(R.string.user_guide_three);
        lists.add(R.string.user_guide_four);
        lists.add(R.string.user_guide_five);
        lists.add(R.string.user_guide_six);
        lists.add(R.string.user_guide_seven);
        lists.add(R.string.user_guide_eight);
        lists.add(R.string.user_guide_nine);
        lists.add(R.string.user_guide_ten);
        lists.add(R.string.user_guide_eleven);
        lists.add(R.string.user_guide_twelve);
        lists.add(R.string.user_guide_thirteen);
        lists.add(R.string.user_guide_fourteen);
        lists.add(R.string.user_guide_fifteen);

        return lists;
    }
}
