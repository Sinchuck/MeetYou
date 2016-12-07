package com.example.yang.meetyou;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.publish.PublishActivity;
import com.example.yang.meetyou.userMessageCenter.OthersPersonalMessageActivity;
import com.example.yang.meetyou.userMessageCenter.PersonalCenterActivity;
import com.example.yang.meetyou.utils.PreferenceUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/25.
 */
public class ConcernActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ConcernActivity";

    ListView mActivityListView;
    ListView mFriendListView;
    List<Huodong> mHuodongList = new ArrayList<>();
    List<Person> mPersonList = new ArrayList<>();
    TextView tv_concernActivity;
    TextView tv_concernFriend;
    TextView mHomePage;
    TextView mConcern;
    TextView mPublish;
    TextView mPersonalCenter;

    private int concernActivityMsgCode;
    private int concernFriendMsgCode;

    OkHttpClient mClient = new OkHttpClient();
    Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);

        mActivityListView = (ListView) findViewById(R.id.list_view_concern_activity);
        mFriendListView = (ListView)findViewById(R.id.list_view_concern_friend);
        tv_concernActivity = (TextView) findViewById(R.id.tv_concernActivity);
        tv_concernFriend = (TextView) findViewById(R.id.tv_concernFriend);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);


        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);

        tv_concernActivity.setTextColor(Color.rgb(34,144, 175));
        tv_concernFriend.setTextColor(Color.rgb(82, 71, 71));
        tv_concernActivity.setOnClickListener(this);
        tv_concernFriend.setOnClickListener(this);
        
        mActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("ACTIVITY_ID", mHuodongList.get(i).getActivityId());
                Bundle bundle = new Bundle();
                bundle.putString("activityId", mHuodongList.get(i).getActivityId());
                Intent a = new Intent(ConcernActivity.this,ActivityContentActivity.class);
                a.putExtras(bundle);
                startActivity(a);
            }
        });
        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("USER_ACCOUNT", mPersonList.get(i).getUserAccount());
                Bundle bundle = new Bundle();
                bundle.putString("userAccount", mPersonList.get(i).getUserAccount());
                Intent b = new Intent(ConcernActivity.this, OthersPersonalMessageActivity.class);
                b.putExtras(bundle);
                startActivity(b);
            }
        });

        new RefreshConcernHuodong().execute();
    }

    private class RefreshConcernHuodong extends AsyncTask<String, Void, List<Huodong>> {

        private final String account = PreferenceUtil.getString(ConcernActivity.this, PreferenceUtil.ACCOUNT);

        String refresh = "http://139.199.180.51/meetyou/public/refreshSocietyActivity?user_account=" + account;

        @Override
        protected List<Huodong> doInBackground(String... params) {

            final Request request = new Request.Builder()
                    .get()
                    .tag(this)
                    .url(refresh)
                    .build();

            Response response;
            try {
                response = mClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        ConcernActivityJson concernActivityJson = mGson.fromJson(response.body().string(),
                                ConcernActivityJson.class);
                        concernActivityMsgCode = concernActivityJson.getMsgCode();
                        if (concernActivityMsgCode == 703) {
                            mHuodongList = concernActivityJson.getData();
                            Log.i(TAG, concernActivityJson.toString());
                        }
                    } catch (Exception je) {
                        je.printStackTrace();
                    }
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mHuodongList;
        }

        @Override
        protected void onPostExecute(List<Huodong> huodongs) {
            super.onPostExecute(huodongs);
            HomePageAdapter adapter = new HomePageAdapter(ConcernActivity.this, huodongs);
            mActivityListView.setAdapter(adapter);

            if (concernActivityMsgCode == 708) {
                Toast.makeText(ConcernActivity.this, "你还没有关注活动", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class RefreshConcernFriend extends AsyncTask<String, Void, List<Person>> {

        private final String account = PreferenceUtil.getString(ConcernActivity.this, PreferenceUtil.ACCOUNT);

        String refresh = "http://139.199.180.51/meetyou/public/refreshSocietyUser?user_account=" + account;

        @Override
        protected List<Person> doInBackground(String... params) {

            final Request request = new Request.Builder()
                    .get()
                    .tag(this)
                    .url(refresh)
                    .build();

            Response response;
            try {
                response = mClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        ConcernFriendJson concernFriendJson = mGson.fromJson(response.body().string(),
                                ConcernFriendJson.class);
                        concernFriendMsgCode = concernFriendJson.getMsgCode();
                        if (concernFriendMsgCode == 705) {
                            mPersonList = concernFriendJson.getData();
                            Log.i(TAG, concernFriendJson.toString());
                            Log.i(TAG, mPersonList.toString());
                        }
                    } catch (Exception je) {
                        je.printStackTrace();
                    }
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mPersonList;
        }

        @Override
        protected void onPostExecute(List<Person> persons) {
            super.onPostExecute(persons);
            if (concernFriendMsgCode == 705) {
                PersonAdapter adapter = new PersonAdapter(ConcernActivity.this, persons);
                mFriendListView.setAdapter(adapter);
            }
            if (concernFriendMsgCode == 709) {
                Toast.makeText(ConcernActivity.this, "你还没有关注的好友", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_concernActivity:
                new RefreshConcernHuodong().execute();
                mFriendListView.setVisibility(view.GONE);
                mActivityListView.setVisibility(View.VISIBLE);
                tv_concernActivity.setTextColor(Color.rgb(34,144, 175));
                tv_concernFriend.setTextColor(Color.rgb(82, 71, 71));

                break;
            case R.id.tv_concernFriend:
                new RefreshConcernFriend().execute();
                mActivityListView.setVisibility(View.GONE);
                mFriendListView.setVisibility(view.VISIBLE);
                tv_concernFriend.setTextColor(Color.rgb(34,144, 175));
                tv_concernActivity.setTextColor(Color.rgb(82, 71, 71));

                break;

            case R.id.tv_home_page:
                Intent a = new Intent(ConcernActivity.this, HomePageActivity.class);
                startActivity(a);
                break;
            case R.id.tv_concern:
                    Toast.makeText(this, "当前所处页面就是关注页面", Toast.LENGTH_SHORT).show();
                    break;
            case R.id.tv_publish:
                Intent c = new Intent(ConcernActivity.this, PublishActivity.class);
                startActivity(c);
                break;
            case R.id.tv_personalCenter:
                Intent d = new Intent(ConcernActivity.this, PersonalCenterActivity.class);
                startActivity(d);
                break;

            }
        }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}