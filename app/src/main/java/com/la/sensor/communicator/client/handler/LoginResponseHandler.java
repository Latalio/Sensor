package com.la.sensor.communicator.client.handler;


import com.la.sensor.displayer.MainActivity;
import com.la.sensor.communicator.protocol.response.LoginResponsePacket;
import com.la.sensor.communicator.session.Session;
import com.la.sensor.communicator.util.SessionManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        String userId = loginResponsePacket.getUserId();
        String userName = loginResponsePacket.getUserName();

        if (loginResponsePacket.isSuccess()) {
            MainActivity.stateHandler.info("登录成功，User id: " + loginResponsePacket.getUserId());
            SessionManager.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            MainActivity.stateHandler.info("[" + userName + "]登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        MainActivity.stateHandler.info("客户端连接被关闭!");
    }
}
