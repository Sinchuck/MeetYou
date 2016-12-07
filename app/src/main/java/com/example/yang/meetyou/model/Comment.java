package com.example.yang.meetyou.model;

/**
 * Created by Yang on 2016/12/5.
 */
public class Comment {

    private String nickname;
    private String commentId;

    @Override
    public String toString() {
        return "Comment{" +
                "nickname='" + nickname + '\'' +
                ", commentId='" + commentId + '\'' +
                ", userHeads='" + userHeads + '\'' +
                ", senderAccount='" + senderAccount + '\'' +
                ", storey='" + storey + '\'' +
                ", commentType='" + commentType + '\'' +
                ", content='" + content + '\'' +
                ", commentTime='" + commentTime + '\'' +
                '}';
    }

    private String userHeads;
    private String senderAccount;
    private String storey; //评论在数据库里面的索引
    private String commentType;
    private String content;
    private String commentTime;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }



    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }




    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }




    public String getUserHeads() {
        return userHeads;
    }

    public void setUserHeads(String userHeads) {
        this.userHeads = userHeads;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(
            String commentType) {
        this.commentType = commentType;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment() {

    }

}
