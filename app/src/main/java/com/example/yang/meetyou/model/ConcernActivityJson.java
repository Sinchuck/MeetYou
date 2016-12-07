package com.example.yang.meetyou.model;

import com.example.yang.meetyou.model.Huodong;

import java.util.List;

/**
 * Created by Sinchuck on 16/12/6.
 */

public class ConcernActivityJson {
    private int msgCode;
    private String msg;
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

    public List<Huodong> getData() {
        return data;
    }

    public void setData(List<Huodong> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ConcernActivityJson{" +
                "msgCode=" + msgCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
