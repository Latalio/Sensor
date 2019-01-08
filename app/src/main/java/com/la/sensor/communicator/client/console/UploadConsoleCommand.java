package com.la.sensor.communicator.client.console;

import com.la.sensor.communicator.protocol.request.MessageRequestPacket;

import io.netty.channel.Channel;

public class UploadConsoleCommand {

    public void exec(Channel channel, String message) {
        channel.writeAndFlush(new MessageRequestPacket(message));
    }
}

