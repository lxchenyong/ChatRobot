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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeff.robotsmailhelper.R;
import com.jeff.robotsmailhelper.interfacecontract.IChatContract;
import com.jeff.robotsmailhelper.model.bean.MsgInfo;
import com.jeff.robotsmailhelper.model.biz.ChatBiz;
import com.jeff.robotsmailhelper.presenter.ChatPresenter;
import com.jeff.robotsmailhelper.view.adapter.ChatAdapter;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements IChatContract.IChatView,
        SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener,
        View.OnClickListener, View.OnLayoutChangeListener {

    //    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private RecyclerView swipeRefreshRecyclerView;
    private ChatAdapter mChatAdapter;
    private EditText mEditText;
    private ChatPresenter presenter;
    private long mExitTime = 0;
    private List<MsgInfo> msgInfoList = new ArrayList<>();
    //Activity最外层的Layout视图
    private RelativeLayout activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        presenter = new ChatPresenter(this, new ChatBiz());
//        presenter.loadData("");
        msgInfoList.add(new MsgInfo("您好，我是邻家小妹妹很高心为你服务", MsgInfo.TYPE_ROBOT));
        initView();

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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        activityRootView = (RelativeLayout) findViewById(R.id.content_main);
//        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        swipeRefreshRecyclerView.autoRefresh(R.color.primary_color,
//                R.color.colorAccent, R.color.primary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        swipeRefreshRecyclerView.setLayoutManager(layoutManager);
//        swipeRefreshRecyclerView.setRefreshing(false);
//        swipeRefreshRecyclerView.setLoading(false);
        swipeRefreshRecyclerView.setEnabled(false);
//        swipeRefreshRecyclerView.setEnabledLoad(false);
//        swipeRefreshRecyclerView.setOnRefreshListener(this);
//        swipeRefreshRecyclerView.setOnListLoadListener(this);
        mChatAdapter = new ChatAdapter(this, msgInfoList);
        swipeRefreshRecyclerView.setAdapter(mChatAdapter);
        mEditText = (EditText) findViewById(R.id.et_content);
        Button mbtSend = (Button) findViewById(R.id.bt_send);
        mbtSend.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onListLoad() {

    }

    @Override
    public void isShowSwipeLayout(boolean flag) {

    }

    @Override
    public void showData(String data) {
        msgInfoList.add(new MsgInfo(data, MsgInfo.TYPE_ROBOT));
        mChatAdapter.notifyDataSetChanged();
        swipeRefreshRecyclerView.smoothScrollToPosition(msgInfoList.size());
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                if (mEditText.getText().toString().isEmpty()) {
                    showToast("内容不能为空");
                } else {
                    presenter.loadData(mEditText.getText().toString());
                    msgInfoList.add(new MsgInfo(mEditText.getText().toString(), MsgInfo.TYPE_USER));
                    mEditText.setText("");
                    //隐藏键盘
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    mChatAdapter.notifyDataSetChanged();
                }


                break;
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
            swipeRefreshRecyclerView.smoothScrollToPosition(msgInfoList.size());
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //监听到软件盘关闭

        }

    }
}
