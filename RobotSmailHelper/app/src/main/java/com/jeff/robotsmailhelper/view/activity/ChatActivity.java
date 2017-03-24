package com.jeff.robotsmailhelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jeff.robotsmailhelper.R;
import com.jeff.robotsmailhelper.interfacecontract.IChatContract;
import com.jeff.robotsmailhelper.model.bean.MsgInfo;
import com.jeff.robotsmailhelper.model.biz.ChatBiz;
import com.jeff.robotsmailhelper.presenter.ChatPresenter;
import com.jeff.robotsmailhelper.view.adapter.ChatAdapter;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements IChatContract.IChatView,
        SwipeRefreshLayout.OnRefreshListener, SwipeRefreshAdapterView.OnListLoadListener,
        View.OnClickListener {

    private SwipeRefreshRecyclerView swipeRefreshRecyclerView;
    private ChatAdapter mChatAdapter;
    private EditText mEditText;
    private ChatPresenter presenter;
    private long mExitTime = 0;
    private List<MsgInfo> msgInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        presenter = new ChatPresenter(this, new ChatBiz());
//        presenter.loadData("");
        initView();

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

        swipeRefreshRecyclerView = (SwipeRefreshRecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshRecyclerView.autoRefresh(R.color.primary_color,
                R.color.colorAccent, R.color.primary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        swipeRefreshRecyclerView.setLayoutManager(layoutManager);
        swipeRefreshRecyclerView.setRefreshing(false);
        swipeRefreshRecyclerView.setLoading(false);
        swipeRefreshRecyclerView.setOnRefreshListener(this);
        swipeRefreshRecyclerView.setOnListLoadListener(this);
        mChatAdapter = new ChatAdapter(this,msgInfoList);
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
        msgInfoList.add(new MsgInfo(data,MsgInfo.TYPE_ROBOT));
        mChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                if (mEditText.getText().toString().isEmpty()){
                    showToast("内容不能为空");
                }else{
                    presenter.loadData(mEditText.getText().toString());
                    msgInfoList.add(new MsgInfo(mEditText.getText().toString(),MsgInfo.TYPE_USER));
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


}
