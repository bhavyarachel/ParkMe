package com.example.parkme.utils;

import de.greenrobot.event.EventBus;

public class PickMeEventBus {

    private static EventBus sEventBus;

    public static EventBus getInstance() {
        if (sEventBus == null) {
            sEventBus = new EventBus();
        }
        return sEventBus;
    }
}
