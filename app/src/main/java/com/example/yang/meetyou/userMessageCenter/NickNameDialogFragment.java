package com.example.yang.meetyou.userMessageCenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.meetyou.R;

/**
 * Created by Yang on 2016/11/19.
 */
public class NickNameDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final int SHOW_TOAST = 11;
    private static final int CHANGE_NICKNAME = 12;


    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText nickname_edit;
    private static Context mContext;

    private String nickname;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TOAST:
                    Toast.makeText(mContext,msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case CHANGE_NICKNAME:
//                    AccountFragment.user_nickname.setText(msg.obj.toString());
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

//    private void saveNickname() {
//        nickname = nickname_edit.getText().toString();
//        if (nickname.equals("")) {
//
//            handler.obtainMessage(SHOW_TOAST, "昵称不可为空").sendToTarget();
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                JSONObject jsonObject = new JSONObject();
//                String token = PreferenceUtil.getString(getActivity(), PreferenceUtil.TOKEN);
//                try {
//                    jsonObject.put("token", token);
//                    jsonObject.put("update_type", 101);
//                    jsonObject.put("nickname", nickname);
//                    jsonObject.put("avatar", "");
//                    jsonObject.put("sex", "");
//                    jsonObject.put("job", "");
//                    jsonObject.put("birthday", "");
//                    String response = ConnectToServer.doPost(ConnectToServer.update_user_imfo, jsonObject);
//                    JSONObject response_object = new JSONObject(response);
//                    int code = response_object.getInt("errorCode");
//                    String result = "";
//                    if (code == 200) {
//                        Log.i("response", response);
//                        result = response_object.getString("msg");
//                        handler.obtainMessage(SHOW_TOAST, result).sendToTarget();
//                        handler.obtainMessage(CHANGE_NICKNAME,nickname).sendToTarget();
//                    } else if (code == 400) {
//                        result = response_object.getString("msg");
//                        handler.obtainMessage(SHOW_TOAST, result).sendToTarget();
//                        Intent i = new Intent(getActivity(), LoginActivity.class);
//                        startActivity(i);
//                    } else {
//                        result = response_object.getString("msg");
//                        handler.obtainMessage(SHOW_TOAST, result).sendToTarget();
//
//                    }
//                } catch (JSONException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_save_nickname_bt:
                this.dismiss();
                break;
            case R.id.save_nickname_bt:
//                saveNickname();
                this.dismiss();
                break;
        }
    }
}
