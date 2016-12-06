package com.example.yang.meetyou;

/**
 * Created by Yang on 2016/12/6.
 */
public class CommentDataObject {
    private String receiverNickName;
    private String id;

    public String getReceiverNickName() {
        return receiverNickName;
    }

    public void setReceiverNickName(String receiverNickName) {
        this.receiverNickName = receiverNickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderNickName() {
        return senderNickName;
    }

    public void setSenderNickName(String senderNickName) {
        this.senderNickName = senderNickName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    private String senderNickName;
    private String senderImage;
    private String senderAccount;
    private String storey;
    private String commentType;
    private String content;
    private String commentTime;

    @Override
    public String toString() {
        return "CommentDataObject{" +
                "receiverNickName='" + receiverNickName + '\'' +
                ", id='" + id + '\'' +
                ", senderNickName='" + senderNickName + '\'' +
                ", senderImage='" + senderImage + '\'' +
                ", senderAccount='" + senderAccount + '\'' +
                ", storey='" + storey + '\'' +
                ", commentType='" + commentType + '\'' +
                ", content='" + content + '\'' +
                ", commentTime='" + commentTime + '\'' +
                '}';
    }
}
