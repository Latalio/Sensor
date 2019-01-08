package com.la.sensor.communicator.protocol.response;

import com.la.sensor.communicator.protocol.Packet;

import static com.la.sensor.communicator.protocol.command.Command.MESSAGE_RESPONSE;


public class MessageResponsePacket extends Packet {

    private String fromUserId;
    private String fromUserName;
    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }


    public String getFromUserId() {
        return fromUserId;
    }
    public String getFromUserName() {
        return fromUserName;
    }
    public String getMessage() {
        return message;
    }

    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }
    public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }
    public void setMessage(String message) {this.message = message; }
}
