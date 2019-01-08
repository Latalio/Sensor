package com.la.sensor.communicator.client.handler;


import com.la.sensor.displayer.MainActivity;
import com.la.sensor.communicator.protocol.response.MessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        MainActivity.stateHandler.info(fromUserId + ":" + fromUserName + " -> " + messageResponsePacket
                .getMessage());
    }
}
