package com.la.sensor.communicator.client.handler;


import com.la.sensor.adapter.NAdapter;
import com.la.sensor.communicator.Communicator;
import com.la.sensor.displayer.MainActivity;
import com.la.sensor.communicator.protocol.response.MessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    NAdapter adapter = new NAdapter();
    public MessageResponseHandler(NAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        String fromUserId = messageResponsePacket.getFromUserId();
        String fromUserName = messageResponsePacket.getFromUserName();
        String message = messageResponsePacket.getMessage();

        MainActivity.stateHandler.info(fromUserId + ":" + fromUserName + " -> " + messageResponsePacket
                .getMessage());
        if (message.equals("Start")) {
            adapter.dataSendTag = 1;
        } else if (message.equals("End")) {
            adapter.dataSendTag = 0;
        } else if (message.equals("On")) {
            adapter.controlSendTag = 1;
        } else if (message.equals("Off")) {
            adapter.controlSendTag = 0;
        } else {
            MainActivity.stateHandler.err(
                    "(Client/MessageResponseHandler)data send tag or control tag error");
        }
    }
}
