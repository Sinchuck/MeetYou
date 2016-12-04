package com.example.yang.meetyou.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sinchuck on 16/12/1.
 */

public class HttpRequestUtils extends AsyncTask<Void, Void, Boolean> {

    private OkHttpClient mClient = new OkHttpClient();

    private static final String IP = "http://119.29.224.50/";
    private static final String PATH = "meetyou/public/";
    public String fullRequest;

    public int status;

    public static HttpRequestUtils newInstance() {
        return new HttpRequestUtils();
    }

    public void appendMethod(String method) {
        fullRequest = IP + PATH + method + "?";
    }

    public void appendPara(String parameter) {
        fullRequest = fullRequest + parameter;
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        final Request request = new Request.Builder()
                .get()
                .tag(this)
                .url(fullRequest)
                .build();

        Response response;
        try {
            response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    status = jsonObject.getInt("msgCode");
                    Log.i("httpRequest", status + "");
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
