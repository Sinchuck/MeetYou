package com.example.yang.meetyou.model;

/**
 * Created by Yang on 2016/9/25.
 */
public class Person {

    private String userAccount;
    private String userNickName;
    private String sex;
    private String userImage;
    private String privacy;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userAccount='" + userAccount + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", sex='" + sex + '\'' +
                ", userImage='" + userImage + '\'' +
                ", privacy='" + privacy + '\'' +
                '}';
    }
}
