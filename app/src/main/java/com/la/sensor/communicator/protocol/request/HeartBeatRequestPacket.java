package com.la.sensor.communicator.protocol.request;


import com.la.sensor.communicator.protocol.Packet;

import static com.la.sensor.communicator.protocol.command.Command.HEARTBEAT_REQUEST;

public class HeartBeatRequestPacket extends Packet {
    private int constant = 1;

    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }
}
