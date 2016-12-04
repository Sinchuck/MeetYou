package com.example.yang.meetyou;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

/**
 * Created by Sinchuck on 16/12/4.
 */

public class HomePageAdapter extends ArrayAdapter{

    Gson mGson;

    private int msgCode;
    private int refreshIndex;

    private String userNickName;
    private String tagId;
    private String activityTheme;
    private String activityTime;
    private String participantCount;
    private String maxCount;

    private Drawable userImage;

    public HomePageAdapter(Context context, int resource) {
        super(context, resource);

        mGson = new Gson();
        HomePageJson homePageJson = mGson.fromJson("HomePage", HomePageJson.class);
    }



    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getRefreshIndex() {
        return refreshIndex;
    }

    public void setRefreshIndex(int refreshIndex) {
        this.refreshIndex = refreshIndex;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getActivityTheme() {
        return activityTheme;
    }

    public void setActivityTheme(String activityTheme) {
        this.activityTheme = activityTheme;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(String participantCount) {
        this.participantCount = participantCount;
    }

    public String getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
    }

    public Drawable getUserImage() {
        return userImage;
    }

    public void setUserImage(Drawable userImage) {
        this.userImage = userImage;
    }
}
