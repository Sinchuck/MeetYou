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
 * Created by Yang on 2016/11/23.
 */
public class ContactDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final int SHOW_TOAST = 11;
    private static final int CHANGE_CONTACT = 12;
    private static final String TAG = "ContactDialogFragment";

    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText contact_edit;
    private static Context mContext;

    private String contact;
    public static ContactDialogFragment newInstance(Context context) {
        mContext = context;

        Bundle args = new Bundle();

        ContactDialogFragment fragment = new ContactDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_contact_dialog, container);
        cancel_tv = (TextView)view.findViewById(R.id.cancel_save_contact_bt);
        sure_tv = (TextView) view.findViewById(R.id.save_contact_bt);
        contact_edit = (EditText) view.findViewById(R.id.edit_user_contact);

        cancel_tv.setOnClickListener(this);
        sure_tv.setOnClickListener(this);


        return view;
    }


    final OkHttpClient mClient = new OkHttpClient();
    private String msg;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(mContext,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_CONTACT:
                    if (msg.obj.toString() == null) {
                        MyselfPersonalMessageActivity.contacts_tv.setText("");
                        break;
                    }
                    MyselfPersonalMessageActivity.contacts_tv.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };
    private void saveContact() {
        contact = contact_edit.getText().toString();

        setContact();

    }

    private void setContact() {
        final String account = PreferenceUtil.getString(getActivity(), PreferenceUtil.ACCOUNT);

        new Thread(new Runnable() {
            @Override
            public void run() {

                String requestURL = "http://139.199.180.51/meetyou/public/updateUserInfo?operation=CONTACTS&user_account=" + account + "&value=" + contact;

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
                                handler.obtainMessage(CHANGE_CONTACT, jsonObject1.getString("contacts")).sendToTarget();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_save_contact_bt:
                this.dismiss();
                break;
            case R.id.save_contact_bt:
                saveContact();
                this.dismiss();
                break;
        }
    }
}
