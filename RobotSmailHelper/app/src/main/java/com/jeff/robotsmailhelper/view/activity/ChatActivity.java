package com.jeff.robotsmailhelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jeff.robotsmailhelper.R;
import com.jeff.robotsmailhelper.interfacecontract.IChatContract;
import com.jeff.robotsmailhelper.model.bean.MsgInfo;
import com.jeff.robotsmailhelper.model.biz.ChatBiz;
import com.jeff.robotsmailhelper.presenter.ChatPresenter;
import com.jeff.robotsmailhelper.view.adapter.ChatAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements IChatContract.IChatView,
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, View.OnLayoutChangeListener {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView mRecyclerView;
    private ChatAdapter mChatAdapter;
    private EditText mEditText;
    private Button mbtSpeak;
    private ImageView mImageView, ivSpeak;
    private Button mbtSend;
    private ChatPresenter presenter;
    private long mExitTime = 0;
    private List<MsgInfo> msgInfoList = new ArrayList<>();
    //Activity最外层的Layout视图
    private RelativeLayout activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private boolean isSpeak = true;
    private boolean showSpeak = true;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        for (int i = DataSupport.order("id desc").limit(10).find(MsgInfo.class).size() - 1; i >= 0; i--) {
            msgInfoList.add(DataSupport.order("id desc").limit(10).find(MsgInfo.class).get(i));
        }
        presenter = new ChatPresenter(this, new ChatBiz());
        if (msgInfoList.isEmpty()) {
            MsgInfo msgInfo = new MsgInfo("您好，我是邻家小妹妹很高心为你服务", MsgInfo.TYPE_ROBOT);
            msgInfoList.add(msgInfo);
            msgInfo.save();
        }
        initView();
        if (!msgInfoList.isEmpty()){
            mRecyclerView.smoothScrollToPosition(msgInfoList.size());
        }
        // 初始化识别对象
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=58d9d824");
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("趣逗");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        activityRootView = (RelativeLayout) findViewById(R.id.content_main);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        swipeRefresh.autoRefresh(R.color.primary_color,
//                R.color.colorAccent, R.color.primary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        swipeRefresh.setOnRefreshListener(this);
        mChatAdapter = new ChatAdapter(this, msgInfoList);
        mRecyclerView.setAdapter(mChatAdapter);
        mEditText = (EditText) findViewById(R.id.et_content);
        mbtSend = (Button) findViewById(R.id.bt_send);
        mImageView = (ImageView) findViewById(R.id.text_switch);
        ivSpeak = (ImageView) findViewById(R.id.iv_speak);
        mbtSpeak = (Button) findViewById(R.id.bt_speak);

        mbtSend.setOnClickListener(this);
        mbtSpeak.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        ivSpeak.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        index++;
        for (int i = 0; i < DataSupport.order("id desc").limit(10).offset(10*index).find(MsgInfo.class).size(); i++) {
            msgInfoList.add(0, DataSupport.order("id desc").limit(10).offset(10*index).find(MsgInfo.class).get(i));
        }
        if (DataSupport.order("id desc").limit(10).offset(10*index).find(MsgInfo.class).isEmpty()){
            showToast("数据已全部加载完成");
        }
        swipeRefresh.setRefreshing(false);
        mChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void isShowSwipeLayout(boolean flag) {

    }

    @Override
    public void showData(String data) {
        MsgInfo msgInfo = new MsgInfo(data, MsgInfo.TYPE_ROBOT);
        msgInfo.save();
        msgInfoList.add(msgInfo);
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(msgInfoList.size());
        if (showSpeak) {
            presenter.initSpeechCompound(data);
        }

    }

    @Override
    public void showSpeak(String message) {
        presenter.loadData(message);
        MsgInfo msgInfo = new MsgInfo(message, MsgInfo.TYPE_USER);
        msgInfo.save();
        msgInfoList.add(msgInfo);
        mChatAdapter.notifyDataSetChanged();

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void failurePrompt(String message) {
        MsgInfo msgInfo = new MsgInfo(message, MsgInfo.TYPE_ERROR);
        msgInfo.save();
        msgInfoList.add(msgInfo);
        mChatAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(msgInfoList.size());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                if (mEditText.getText().toString().isEmpty()) {
                    showToast("内容不能为空");
                } else {
                    presenter.loadData(mEditText.getText().toString());
                    MsgInfo msgInfo = new MsgInfo(mEditText.getText().toString(), MsgInfo.TYPE_USER);
                    msgInfo.save();
                    msgInfoList.add(msgInfo);
                    mEditText.setText("");
                    //隐藏键盘
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    mChatAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.text_switch:
                if (isSpeak) {
                    mbtSpeak.setVisibility(View.VISIBLE);
                    mbtSend.setVisibility(View.GONE);
                    mEditText.setVisibility(View.GONE);
                    mImageView.setImageResource(R.mipmap.ic_keyboard);
                    isSpeak = false;
                } else {
                    mbtSpeak.setVisibility(View.GONE);
                    mbtSend.setVisibility(View.VISIBLE);
                    mEditText.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(R.mipmap.ic_speaker);
                    isSpeak = true;
                }
                break;
            case R.id.bt_speak:
                presenter.initSpeech();
                break;
            case R.id.iv_speak:
                if (showSpeak) {
                    showSpeak = false;
                    ivSpeak.setImageResource(R.mipmap.ic_notifications_off_black);
                } else {
                    showSpeak = true;
                    ivSpeak.setImageResource(R.mipmap.ic_notifications_none_black);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_cache) {
            DataSupport.deleteAll(MsgInfo.class);
            msgInfoList.clear();
            mChatAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.explain) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1000) {
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

            //监听到软键盘弹起
            mRecyclerView.smoothScrollToPosition(msgInfoList.size());
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //监听到软件盘关闭

        }
    }

}
