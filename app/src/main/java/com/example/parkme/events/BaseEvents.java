package com.example.parkme.events;

public class BaseEvents {

    private int mEventType;
    private Object mObject;

    public BaseEvents(int type) {
        mEventType = type;
    }


    public BaseEvents(int type, Object data) {
        mEventType = type;
        mObject = data;
    }

    public int getmEventType() {
        return mEventType;
    }

    public Object getmObject() {
        return mObject;
    }
}
