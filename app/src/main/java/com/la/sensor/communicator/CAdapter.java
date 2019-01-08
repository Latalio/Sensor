package com.la.sensor.communicator;

import com.la.sensor.adapter.Adapter;

public class CAdapter extends Adapter {

    public void initialize(Adapter otherAdapter) {
        this.connect(otherAdapter);
    }
}
