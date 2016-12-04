package com.example.yang.meetyou.publish;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yang.meetyou.R;

/**
 * Created by Yang on 2016/11/25.
 */
public class EditThemeFragment extends DialogFragment implements View.OnClickListener {

    private TextView cancel_tv;
    private TextView sure_tv;
    private EditText nickname_edit;


    public static EditThemeFragment newInstance() {
        Bundle args = new Bundle();
        EditThemeFragment fragment = new EditThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_edit_theme, container);
        cancel_tv = (TextView)view.findViewById(R.id.cancel_edit_theme_bt);
        sure_tv = (TextView) view.findViewById(R.id.save_theme_bt);
        nickname_edit = (EditText) view.findViewById(R.id.et_theme_dialog);

        cancel_tv.setOnClickListener(this);
        sure_tv.setOnClickListener(this);
        return view;

    }

    private void saveTheme() {
        PublishActivity.theme_et .setText(nickname_edit.getText().toString());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_edit_theme_bt:
                this.dismiss();
                break;
            case R.id.save_theme_bt:
                saveTheme();
                this.dismiss();
                break;
        }
    }

}
