package com.la.sensor.adapter.packet;

import com.la.sensor.communicator.protocol.Packet;

import static com.la.sensor.communicator.protocol.command.Command.MESSAGE;

public class InsideMessagePacket extends Packet {
    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
