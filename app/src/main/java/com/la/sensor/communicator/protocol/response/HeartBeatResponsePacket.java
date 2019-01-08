package com.la.sensor.communicator.protocol.response;


import com.la.sensor.communicator.protocol.Packet;

import static com.la.sensor.communicator.protocol.command.Command.HEARTBEAT_RESPONSE;

public class HeartBeatResponsePacket extends Packet {
    private int constant = 1;

    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }
}
