package com.example.sushant.messagingapp.POJO;

import java.util.ArrayList;

/**
 * Created by sushant on 26/5/16.
 */
public class ParticularSMS {

    public ParticularSMS(String address, String msg){
        this.address = address;
        this.msg = msg;
    }

    public String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String msg;
}
