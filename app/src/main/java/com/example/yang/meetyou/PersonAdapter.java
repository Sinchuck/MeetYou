package com.example.yang.meetyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yang on 2016/9/25.
 */
public class PersonAdapter extends ArrayAdapter<Person> {
    int listview_item_id;
    public PersonAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
        listview_item_id = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(listview_item_id,null);
        ImageView iv_heads = (ImageView) view.findViewById(R.id.iv_person_heads);
        TextView tv_name = (TextView)view.findViewById(R.id.tv_person_name);
        iv_heads.setImageDrawable(person.getHeads());
        tv_name.setText(person.getName());
        return view;
    }
}
