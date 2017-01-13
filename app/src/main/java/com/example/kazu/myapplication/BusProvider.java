package com.example.kazu.myapplication;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by kbx on 2017/01/12.
 */

public class BusProvider {

    private static final EventBus BUS = new EventBus();

    public static EventBus getInstance() {
        return BUS;
    }
}
