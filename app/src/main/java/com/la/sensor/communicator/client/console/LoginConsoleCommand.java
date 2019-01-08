package com.la.sensor.communicator.client.console;

import com.la.sensor.displayer.MainActivity;
import com.la.sensor.communicator.protocol.request.LoginRequestPacket;

import io.netty.channel.Channel;

public class LoginConsoleCommand {

    public void exec(Channel channel) {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setUserName("Nubia");
        loginRequestPacket.setPassword("password");
        loginRequestPacket.setType("Sensor");
        MainActivity.stateHandler.info(
                "Login info: [Username]" + loginRequestPacket.getUserName() +
                " [Type]" + loginRequestPacket.getType()
        );

        // 发送登录数据包
        channel.writeAndFlush(loginRequestPacket);
        waitForLoginResponse();
    }

    private static void waitForLoginResponse() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}
