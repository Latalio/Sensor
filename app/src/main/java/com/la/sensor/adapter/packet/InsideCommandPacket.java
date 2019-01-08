package com.la.sensor.adapter.packet;

import com.la.sensor.communicator.protocol.Packet;

import static com.la.sensor.communicator.protocol.command.Command.COMMAND;

public class InsideCommandPacket extends Packet {
    private Byte button = 0;

    @Override
    public Byte getCommand() {
        return COMMAND;
    }

    public Byte getButton() {
        return this.button;
    }

    public void setButton(Byte button) {
        this.button = button;
    }
}
