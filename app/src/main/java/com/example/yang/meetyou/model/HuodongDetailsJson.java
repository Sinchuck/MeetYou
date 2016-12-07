package com.example.yang.meetyou.model;

/**
 * Created by Sinchuck on 16/12/5.
 */

public class HuodongDetailsJson {
    private int msgCode;
    private String msg;
    ActivityInfo activityInfo  = new ActivityInfo();

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ActivityInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    @Override
    public String toString() {
        return "HuodongDetailsJson{" +
                "msgCode=" + msgCode +
                ", msg='" + msg + '\'' +
                ", activityInfo=" + activityInfo +
                '}';
    }

     public class ActivityInfo {
         public String isParticipated;
         public String activity_tag;
         public String activity_theme;
         public String activity_release_time;
         public String activity_time;
         public String activity_details;
         public String activity_comment_count;
         public String activity_participants_count;
         public String activity_participants_max_count;
         public String activity_releaser_account;
         public String activity_releaser_headPic;
         public String activity_releaser_nickName;

         @Override
         public String toString() {
             return "ActivityInfo{" +
                     "activity_is_participated='" + isParticipated + '\'' +
                     ", activity_tag='" + activity_tag + '\'' +
                     ", activity_theme='" + activity_theme + '\'' +
                     ", activity_release_time='" + activity_release_time + '\'' +
                     ", activity_time='" + activity_time + '\'' +
                     ", activity_details='" + activity_details + '\'' +
                     ", activity_comment_count='" + activity_comment_count + '\'' +
                     ", activity_participants_count='" + activity_participants_count + '\'' +
                     ", activity_participants_max_count='" + activity_participants_max_count + '\'' +
                     ", activity_releaser_account='" + activity_releaser_account + '\'' +
                     ", activity_releaser_headPic='" + activity_releaser_headPic + '\'' +
                     ", activity_releaser_nickName='" + activity_releaser_nickName + '\'' +
                     '}';
         }
     }
}
