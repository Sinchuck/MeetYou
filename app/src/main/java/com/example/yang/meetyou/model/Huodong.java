package com.example.yang.meetyou.model;

public class Huodong {

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
        return "{" +
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
