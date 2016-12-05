package com.example.yang.meetyou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yang.meetyou.userMessageCenter.OthersPersonalMessageActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/9/26.
 */
public class ActivityContentActivity extends AppCompatActivity {

    private final static String TAG = "ActivityContentActivity";

    HuodongDetailsJson huodongDetailsJson;

    String activityId;
    String huodongDetails;

    LinearLayout mHeadsLinear;
    TextView mActivityNameTextView;

    OkHttpClient mClient = new OkHttpClient();
    Gson mGson = new Gson();

    TextView mUserNickname;
    TextView mUserAccount;
    TextView mActivityTag;
    TextView mActivityTheme;
    TextView mActivityTime;
    TextView mMaxNumber;
    TextView mActivityDetails;
    ImageView mHeads;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mActivityNameTextView = (TextView) findViewById(R.id.tv_activity_name);
        mActivityNameTextView.setText("活动详情");
        mHeadsLinear = (LinearLayout)findViewById(R.id.linear_other_heads);
        mHeadsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(ActivityContentActivity.this, OthersPersonalMessageActivity.class);
                startActivity(a);
            }
        });

        mUserNickname = (TextView) findViewById(R.id.content_user_nickname);
        mUserAccount = (TextView) findViewById(R.id.content_user_account);
        mActivityTag = (TextView) findViewById(R.id.content_activity_tag);
        mActivityTheme = (TextView) findViewById(R.id.content_activity_theme);
        mActivityTime = (TextView) findViewById(R.id.content_activity_time);
        mMaxNumber = (TextView) findViewById(R.id.content_max_number);
        mActivityDetails = (TextView) findViewById(R.id.content_activity_details);
        mHeads = (ImageView) findViewById(R.id.content_releaser_head);

        Bundle bundle = getIntent().getExtras();
        activityId = bundle.getString("activityId");
        huodongDetails = "http://119.29.224.50/meetyou/public/activityInfo?activity_id=" + activityId;

        new GetActivityData().execute();
    }

    private class GetActivityData extends AsyncTask<Void, Void, HuodongDetailsJson> {

        @Override
        protected HuodongDetailsJson doInBackground(Void... params) {
            final Request request = new Request.Builder()
                    .get()
                    .tag(this)
                    .url(huodongDetails)
                    .build();

            Response response;
            try {
                response = mClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        huodongDetailsJson = mGson.fromJson(response.body().string(),
                                HuodongDetailsJson.class);
                        Log.i(TAG, huodongDetailsJson.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            return huodongDetailsJson;
        }

        @Override
        protected void onPostExecute(HuodongDetailsJson detailsJson) {
            super.onPostExecute(detailsJson);
            mUserAccount.setText(detailsJson.getActivityInfo().activity_releaser_account);
            mUserNickname.setText(detailsJson.getActivityInfo().activity_releaser_nickName);
            mActivityDetails.setText(detailsJson.getActivityInfo().activity_details);
            mMaxNumber.setText(detailsJson.getActivityInfo().activity_participants_max_count);
            mActivityTag.setText(detailsJson.getActivityInfo().activity_tag);
            mActivityTheme.setText(detailsJson.getActivityInfo().activity_theme);
            mActivityTime.setText(detailsJson.getActivityInfo().activity_time);
            new DownloadImageTask().execute(detailsJson.getActivityInfo().activity_releaser_headPic);

        }
        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

            public DownloadImageTask() {

            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    Log.i("123", 147258 + "");
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                if(result != null){
                    mHeads.setImageBitmap(result);
                }
            }
        }
    }




}
