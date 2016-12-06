package com.example.yang.meetyou;

import java.util.List;

/**
 * Created by Yang on 2016/12/6.
 */
public class CommentJson {
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

    public List<CommentDataObject> getData() {
        return data;
    }

    public void setData(List<CommentDataObject> data) {
        this.data = data;
    }
    private int msgCode;
    private String msg;
    private List<CommentDataObject> data;
    @Override
    public String toString() {
        return "CommentJson{" +
                "msgCode=" + msgCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
