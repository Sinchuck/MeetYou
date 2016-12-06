package com.example.yang.meetyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yang.meetyou.utils.DownloadImageTask;

import java.util.List;

/**
 * Created by Yang on 2016/9/25.
 */
public class PersonAdapter extends BaseAdapter {

    private List<Person> mPersons;

    ViewHolder mViewHolder = null;

    LayoutInflater mLayoutInflater;

    public PersonAdapter(Context context, List<Person> persons) {
        mPersons = persons;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPersons.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.concern_friend_list_item, null);

            mViewHolder.userNickname = (TextView) convertView.findViewById(R.id.tv_person_name);
            mViewHolder.userImage = (ImageView) convertView.findViewById(R.id.iv_person_heads);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.userNickname.setText(mPersons.get(position).getUserNickName());
        new DownloadImageTask(mViewHolder.userImage).execute(mPersons.get(position).getUserImage());

        return convertView;
    }

    class ViewHolder {
        ImageView userImage;
        TextView userNickname;
    }
}
