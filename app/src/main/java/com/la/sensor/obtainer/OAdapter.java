package com.la.sensor.obtainer;

import com.la.sensor.adapter.Adapter;
import com.la.sensor.adapter.packet.InsideCommandPacket;
import com.la.sensor.displayer.MainActivity;

public class OAdapter extends Adapter {
    private Byte dataObtainTag = 0;

    public void initialize(Adapter otherAdapter) {
        this.connect(otherAdapter);
    }

    public void start() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        InsideCommandPacket insideCommandPacket;
                        while (true) {
                            MainActivity.stateHandler.info("OAdapter here");
                            insideCommandPacket = (InsideCommandPacket)get();
                            dataObtainTag = insideCommandPacket.getButton();
                            MainActivity.stateHandler.info(dataObtainTag.toString());
                        }
                    }
                }
        ).start();
    }

    public Byte getDataObtainTag() {
        return dataObtainTag;
    }
}
