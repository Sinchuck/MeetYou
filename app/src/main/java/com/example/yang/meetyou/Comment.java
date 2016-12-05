package com.example.yang.meetyou;

/**
 * Created by Yang on 2016/12/5.
 */
public class Comment {
    private Person commentPerson;
    private Person receiveCommentPerson;
    private int commentType;
    private String content;
    private String commentTime;

    public Person getReceiveCommentPerson() {
        return receiveCommentPerson;
    }

    public void setReceiveCommentPerson(Person receiveCommentPerson) {
        this.receiveCommentPerson = receiveCommentPerson;
    }

    public Person getCommentPerson() {
        return commentPerson;
    }

    public void setCommentPerson(Person commentPerson) {
        this.commentPerson = commentPerson;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
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
