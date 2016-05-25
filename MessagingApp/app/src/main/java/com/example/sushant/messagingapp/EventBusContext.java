package com.example.sushant.messagingapp;

/**
 * Created by sushant on 25/5/16.
 */
public class EventBusContext {

    public int ActionCode;

    public EventBusContext(int actionCode)
    {
        this.ActionCode=actionCode;
    }

    public int getActionCode() {
        return this.ActionCode;
    }

}
