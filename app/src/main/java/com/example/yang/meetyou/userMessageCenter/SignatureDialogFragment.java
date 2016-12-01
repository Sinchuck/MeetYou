package com.example.yang.meetyou.userMessageCenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yang.meetyou.R;

/**
 * Created by Yang on 2016/11/23.
 */
public class SignatureDialogFragment extends DialogFragment implements View.OnClickListener {


    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText signature_edit;
    private static Context mContext;

    private String signature;
    public static SignatureDialogFragment newInstance(Context context) {
        mContext = context;

        Bundle args = new Bundle();

        SignatureDialogFragment fragment = new SignatureDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_signature_dialog, container);
        cancel_tv = (TextView)view.findViewById(R.id.cancel_save_signature_bt);
        sure_tv = (TextView) view.findViewById(R.id.save_signature_bt);
        signature_edit = (EditText) view.findViewById(R.id.edit_user_signature);

        cancel_tv.setOnClickListener(this);
        sure_tv.setOnClickListener(this);


        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_save_signature_bt:
                this.dismiss();
                break;
            case R.id.save_signature_bt:
//                saveNickname();
                this.dismiss();
                break;
        }
    }
}
