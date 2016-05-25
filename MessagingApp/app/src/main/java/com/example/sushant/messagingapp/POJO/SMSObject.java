package com.example.sushant.messagingapp.POJO;

/**
 * Created by sushant on 25/5/16.
 */
public class SMSObject {

    public SMSObject(int id, String address, String msgBody, String readState, String time) {
        this.id = id;
        this.address = address;
        this.msgBody = msgBody;
        this.readState = readState;
        this.time = time;
    }

    private int id;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    private String msgBody;
    private String readState;
    private String time;
    private String folderName;

}
