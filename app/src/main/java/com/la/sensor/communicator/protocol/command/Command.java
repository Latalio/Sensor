package com.la.sensor.communicator.protocol.command;

public interface Command {

    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    Byte HEARTBEAT_REQUEST = 7;

    Byte HEARTBEAT_RESPONSE = 8;

    Byte COMMAND = 9;
    Byte MESSAGE = 10;
}
