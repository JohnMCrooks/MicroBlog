package com.crooks;

/**
 * Created by johncrooks on 6/6/16.
 */
public class Message {
    String msgContent;

    public Message(String msgContent) {
        this.msgContent = msgContent;
    }

    @Override
    public String toString() {
        return  msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
