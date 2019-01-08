package com.la.sensor.communicator.protocol.request;


import com.la.sensor.communicator.protocol.Packet;

import static com.la.sensor.communicator.protocol.command.Command.MESSAGE_REQUEST;

public class MessageRequestPacket extends Packet {
    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
