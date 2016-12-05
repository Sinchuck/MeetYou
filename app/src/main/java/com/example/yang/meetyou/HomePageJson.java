package com.example.yang.meetyou;

import java.util.List;

/**
 * Created by Sinchuck on 16/12/4.
 */

public class HomePageJson {

    private int msgCode;
    private String msg;
    private int refreshIndex;

    private List<Huodong> data;

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

    public int getRefreshIndex() {
        return refreshIndex;
    }

    public void setRefreshIndex(int refreshIndex) {
        this.refreshIndex = refreshIndex;
    }

    public List<Huodong> getData() {
        return data;
    }

    public void setData(List<Huodong> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HomePageJson{" +
                "msgCode=" + msgCode +
                ", msg='" + msg + '\'' +
                ", refreshIndex=" + refreshIndex +
                ", data=" + data +
                '}';
    }

    private class BriefInformation {

        private String userAccount;
        private String userImage;
        private String userNickName;
        private String activityId;
        private String tagId;
        private String activityTheme;
        private String activityTime;
        private String participantCount;
        private String maxCount;

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
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

        @Override
        public String toString() {
            return "BriefInformation{" +
                    "userAccount='" + userAccount + '\'' +
                    ", userImage='" + userImage + '\'' +
                    ", userNickName='" + userNickName + '\'' +
                    ", activityId='" + activityId + '\'' +
                    ", tagId='" + tagId + '\'' +
                    ", activityTheme='" + activityTheme + '\'' +
                    ", activityTime='" + activityTime + '\'' +
                    ", participantCount='" + participantCount + '\'' +
                    ", maxCount='" + maxCount + '\'' +
                    '}';
        }
    }
}
