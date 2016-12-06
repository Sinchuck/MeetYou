package com.example.yang.meetyou;

import java.util.List;

/**
 * Created by Sinchuck on 16/12/6.
 */

public class ConcernFriendJson {

    private int msgCode;
    private String msg;
    private List<Person> data;

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

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}
