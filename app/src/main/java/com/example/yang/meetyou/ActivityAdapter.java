package com.example.yang.meetyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yang on 2016/9/24.
 */
public class ActivityAdapter extends ArrayAdapter<Huodong> {
    int listview_item_id;

    public ActivityAdapter(Context context, int resource, List<Huodong> activityList) {
        super(context, resource,activityList);
        listview_item_id = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Huodong activity = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(listview_item_id,null);
//        ImageView kindImage = (ImageView) view.findViewById(R.id.iv_kind);
//        TextView publisherId = (TextView) view.findViewById(R.id.tv_publisher_id);
        TextView theme = (TextView) view.findViewById(R.id.tv_theme);
//        TextView publishTime = (TextView) view.findViewById(R.id.tv_publisher_time);
//        kindImage.setImageDrawable(activity.getKind());
//        publisherId.setText(activity.getPublisherId());
//        publishTime.setText(activity.getPublishTime());
//        theme.setText(activity.getTheme());
        return view;
    }
}
