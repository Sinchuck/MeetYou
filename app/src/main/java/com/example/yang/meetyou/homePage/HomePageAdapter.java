package com.example.yang.meetyou.homePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yang.meetyou.R;
import com.example.yang.meetyou.model.Huodong;
import com.example.yang.meetyou.utils.DownloadImageTask;

import java.util.List;

/**
 * Created by Sinchuck on 16/12/4.
 */

public class HomePageAdapter extends BaseAdapter{

    private List<Huodong> mHuodongs;
    ViewHolder viewHolder = null;

    private LayoutInflater mLayoutInflater;

    private int msgCode;
    private int refreshIndex;

    public HomePageAdapter(Context context, List<Huodong> data) {
        mHuodongs = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mHuodongs.size();
    }

    @Override
    public Object getItem(int position) {
        return mHuodongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.home_page_listview_item, null);

            viewHolder.userImage = (ImageView) convertView.findViewById(R.id.home_page_user_image);
            viewHolder.userNickName = (TextView) convertView.findViewById(R.id.tv_home_page_user_nickname);
            viewHolder.tagId = (TextView) convertView.findViewById(R.id.tv_home_page_tag);
            viewHolder.activityTheme = (TextView) convertView.findViewById(R.id.tv_theme);
            viewHolder.activityTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.participantCount = (TextView) convertView.findViewById(R.id.tv_attended_number);
            viewHolder.maxCount = (TextView) convertView.findViewById(R.id.tv_max_number);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        new DownloadImageTask(viewHolder.userImage).execute(mHuodongs.get(position).getUserImage());
        viewHolder.userNickName.setText(mHuodongs.get(position).getUserNickName());
        viewHolder.tagId.setText(mHuodongs.get(position).getTagId());
        viewHolder.activityTheme.setText(mHuodongs.get(position).getActivityTheme());
        viewHolder.activityTime.setText(mHuodongs.get(position).getActivityTime());
        viewHolder.participantCount.setText(mHuodongs.get(position).getParticipantCount());
        viewHolder.maxCount.setText(mHuodongs.get(position).getMaxCount());

        return convertView;
    }

    private class ViewHolder {

        TextView userNickName;
        TextView tagId;
        TextView activityTheme;
        TextView activityTime;
        TextView participantCount;
        TextView maxCount;

        ImageView userImage;
    }
}
