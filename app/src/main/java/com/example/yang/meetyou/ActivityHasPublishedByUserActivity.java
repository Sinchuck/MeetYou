package com.example.yang.meetyou;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yang.meetyou.model.ConcernActivityJson;
import com.example.yang.meetyou.model.Huodong;
import com.example.yang.meetyou.utils.PreferenceUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/26.
 */
public class ActivityHasPublishedByUserActivity extends AppCompatActivity {

    private static final String TAG = "HasPublishedActivity";

    ListView mActivityHasPublishedListView;
    List<Huodong> mHuodongHasPublishedList = new ArrayList<>();

    int publishedActivityMsgCode;
    String userAccount;

    OkHttpClient mClient = new OkHttpClient();
    Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_published_by_user);
        mActivityHasPublishedListView = (ListView) findViewById(R.id.lv_activity_has_published_by_user);

        mActivityHasPublishedListView.setAdapter(new ActivityAdapter
                (ActivityHasPublishedByUserActivity.this, R.layout.home_page_listview_item, mHuodongHasPublishedList));

        mActivityHasPublishedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("activityId", mHuodongHasPublishedList.get(i).getActivityId());
                Intent a = new Intent(ActivityHasPublishedByUserActivity.this,ActivityContentActivity.class);
                a.putExtras(bundle);
                startActivity(a);
            }
        });

        Bundle bundle = getIntent().getExtras();
        userAccount = bundle.getString("userAccount");

        new HasPublishedHuodong().execute(userAccount);
    }

    private class HasPublishedHuodong extends AsyncTask<String, Void, List<Huodong>> {

        private final String account = PreferenceUtil.getString(ActivityHasPublishedByUserActivity.this,
                PreferenceUtil.ACCOUNT);

        @Override
        protected List<Huodong> doInBackground(String... params) {

            String refresh = "http://139.199.180.51/meetyou/public/userActivity?user_account=" + userAccount;

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
                        publishedActivityMsgCode = concernActivityJson.getMsgCode();
                        if (publishedActivityMsgCode == 313) {
                            mHuodongHasPublishedList = concernActivityJson.getData();
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
            return mHuodongHasPublishedList;
        }

        @Override
        protected void onPostExecute(List<Huodong> huodongs) {
            super.onPostExecute(huodongs);
            HomePageAdapter adapter = new HomePageAdapter(ActivityHasPublishedByUserActivity.this, huodongs);
            mActivityHasPublishedListView.setAdapter(adapter);

            if (publishedActivityMsgCode == 315) {
                Toast.makeText(ActivityHasPublishedByUserActivity.this, "你还没有发布活动", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
