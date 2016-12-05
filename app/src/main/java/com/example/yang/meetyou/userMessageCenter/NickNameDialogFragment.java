package com.example.yang.meetyou.userMessageCenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.R;
import com.example.yang.meetyou.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yang on 2016/11/19.
 */
public class NickNameDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int SHOW_TOAST = 11;
    private static final int CHANGE_NICKNAME = 12;
    private static final String TAG = "NickNameDialogFragment";


    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText nickname_edit;
    private static Context mContext;

    private String nickname;
    final OkHttpClient mClient = new OkHttpClient();
    private String msg;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(mContext,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_NICKNAME:
                    MyselfPersonalMessageActivity.nickname_tv.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };

     public static NickNameDialogFragment newInstance(Context context) {

        Bundle args = new Bundle();
        NickNameDialogFragment fragment = new NickNameDialogFragment();
        fragment.setArguments(args);
         mContext = context;

        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_nickname_dialog, container);
        cancel_tv = (TextView)view.findViewById(R.id.cancel_save_nickname_bt);
        sure_tv = (TextView) view.findViewById(R.id.save_nickname_bt);
        nickname_edit = (EditText) view.findViewById(R.id.edit_user_nickname);

        cancel_tv.setOnClickListener(this);
        sure_tv.setOnClickListener(this);


        return view;
    }

    private void saveNickname() {
        nickname = nickname_edit.getText().toString();
        Log.i("147", 13+"");
        if (nickname.equals("")) {
            handler.obtainMessage(SHOW_TOAST, "昵称不可为空").sendToTarget();
            return;
        }

        setNickname();

    }

    private void setNickname() {
        final String account = PreferenceUtil.getString(getActivity(), PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {

                String requestURL = "http://119.29.224.50/meetyou/public/updateUserInfo?operation=NICKNAME&user_account=" + account + "&value=" + nickname;

                final Request request = new Request.Builder()
                        .get()
                        .tag(this)
                        .url(requestURL)
                        .build();

                Response response;
                try {
                    response = mClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        try {
                            String response2 = response.body().string();
                            JSONObject jsonObject = new JSONObject(response2);
                            Log.i(TAG, response2);
                            int  status = jsonObject.getInt("msgCode");
                            Log.i("123", status + "");
                            msg = jsonObject.getString("msg");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                            if (status == 305) {
                                handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                                handler.obtainMessage(CHANGE_NICKNAME, jsonObject1.getString("nickName")).sendToTarget();
                            }else if(status == 306){
                                handler.obtainMessage(SHOW_TOAST,msg).sendToTarget();
                            }

                            Log.i("123", msg);
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_save_nickname_bt:

                this.dismiss();
                break;
            case R.id.save_nickname_bt:
                saveNickname();
                this.dismiss();
                break;
        }
    }
}
