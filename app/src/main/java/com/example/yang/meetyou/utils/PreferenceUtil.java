package com.example.yang.meetyou.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Yang on 2016/12/2.
 */
public class PreferenceUtil {
    public static final String ACCOUNT = "account";
    /**
     * 存储信息
     */
    public static void setString(Context context, String key, String value){
        // 得到SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取信息
     */
    public static String getString(Context context,String key)
    {
        SharedPreferences preferences = context.getSharedPreferences(
                "preference", Context.MODE_PRIVATE);
        // 返回key值，key值默认值是false
        return preferences.getString(key,"");
    }
}
