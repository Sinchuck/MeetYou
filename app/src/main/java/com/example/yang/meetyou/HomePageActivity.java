package com.example.yang.meetyou;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.accounts.LoginActivity;
import com.example.yang.meetyou.publish.PublishActivity;
import com.example.yang.meetyou.userMessageCenter.PersonalCenterActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/24.
 */
public class HomePageActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "HomePageActivity";

    public static final String FIRST_USE = "first_use";

    ListView mListView;
    List<Huodong> mHuodongList = new ArrayList<>();
    TextView mHomePage;
    TextView mConcern;
    TextView mPublish;
    TextView mPersonalCenter;

    Toolbar mToolbar;

    String refreshHomePage;
    String search;
    String searchWithTag;
    String activity_kind;
    private String userAccount;
    private String userImage;
    private String userNickName;
    private String activityId;
    private String tagId;
    private String activityTheme;
    private String activityTime;
    private String participantCount;
    private String maxCount;

    OkHttpClient mClient = new OkHttpClient();
    Gson mGson = new Gson();

    private int refreshIndex = -1;
    int status;

    private SharedPreferences sp;

    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mListView = (ListView) findViewById(R.id.lv_activity);
        mHomePage = (TextView) findViewById(R.id.tv_home_page);
        mConcern = (TextView) findViewById(R.id.tv_concern);
        mPersonalCenter = (TextView) findViewById(R.id.tv_personalCenter);
        mPublish = (TextView) findViewById(R.id.tv_publish);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean(FIRST_USE, true)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mHomePage.setOnClickListener(this);
        mConcern.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPersonalCenter.setOnClickListener(this);
//        for(int i =0;i<10;i++) {
//            Huodong activity = new Huodong();
//            activity.setUserImage(userImage);
//            activity.setUserNickName(userNickName);
//            activity.setActivityTheme(activityTheme);
//            activity.setTagId(tagId);
//            activity.setActivityTime(activityTime);
//            activity.setParticipantCount(participantCount);
//            activity.setMaxCount(maxCount);
//
//            mHuodongList.add(activity);
//        }

//        mListView.setAdapter(new ActivityAdapter(HomePageActivity.this,R.layout.home_page_listview_item, mHuodongList));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent a = new Intent(HomePageActivity.this, ActivityContentActivity.class);
                startActivity(a);
            }
        });

        refreshHomePage = "http://119.29.224.50/meetyou/public/refreshHomePage?refreshIndex=" + refreshIndex;
        sendTo(refreshHomePage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search = "http://119.29.224.50/meetyou/public/searchForActivityWithKey?search_keywds=" + s;
                sendTo(search);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        MenuItem downItem = menu.findItem(R.id.classify_search);
        downItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showActivityKindDialog();
                return true;
            }
        });
        return true;
    }

    public void showActivityKindDialog() {
        final String[] items = new String[7];
        items[0] = "未知";
        items[1]="体育";
        items[2]= "手游";
        items[3]="桌游";
        items[4]="游戏";
        items[5]="学习";
        items[6]="其他";
        activity_kind = "未知";
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity_kind = items[which];
                        searchWithTag = "http://119.29.224.50/meetyou/public/searchForActivityWithTag?search_tag="
                                + activity_kind;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendTo(searchWithTag);
                        Toast.makeText(HomePageActivity.this,"确定",Toast.LENGTH_LONG).show();
                        //TODO: 请求服务端代码
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomePageActivity.this,"取消",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create().show();
    }

    private void sendTo(String ros){
        RefreshOrSearch myRequest = new RefreshOrSearch();
        myRequest.refreshOrSearch = ros;
        myRequest.execute();
    }

    private class RefreshOrSearch extends AsyncTask<String, Void, List<Huodong>> {

        String refreshOrSearch;

        @Override
        protected List<Huodong> doInBackground(String... params) {

            String huodongString;
            Huodong huodong;

            final Request request = new Request.Builder()
                    .get()
                    .tag(this)
                    .url( refreshOrSearch)
                    .build();

            Response response;
            try {
                response = mClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        status = jsonObject.getInt("msgCode");
//                        Log.i(TAG, status + "");
                        HomePageJson homePageJson = mGson.fromJson(response.body().string(),
                                HomePageJson.class);
                        mHuodongList = homePageJson.getData();
//                        for (int i = 0; i < huodongs.size(); i++) {
//                            huodong = new Huodong();
//                            huodong.setUserNickName(huodongs.get(i).getUserNickName());
//                            huodong.setTagId(huodongs.get(i).getTagId());
//                            huodong.setActivityTheme(huodongs.get(i).getActivityTheme());
//                            huodong.setActivityTime(huodongs.get(i).getActivityTime());
//                            huodong.setParticipantCount(huodongs.get(i).getParticipantCount());
//                            huodong.setMaxCount(huodongs.get(i).getMaxCount());
//                        }
                        Log.i(TAG, homePageJson.toString());
                        Log.i(TAG, mHuodongList.toString());

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

            HomePageAdapter adapter = new HomePageAdapter(HomePageActivity.this, huodongs);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_home_page:
                Toast.makeText(this,"当前所处页面就是主页",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_concern:
                Intent b = new Intent(HomePageActivity.this, ConcernActivity.class);
                startActivity(b);
                break;
            case R.id.tv_publish:
                Intent c = new Intent(HomePageActivity.this, PublishActivity.class);
                startActivity(c);
                break;
            case R.id.tv_personalCenter:
                Intent d = new Intent(HomePageActivity.this, PersonalCenterActivity.class);
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

    public interface PullToRefreshListener {
        void onRefresh();
    }
}
