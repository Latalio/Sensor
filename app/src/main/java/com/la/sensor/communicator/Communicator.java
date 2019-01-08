package com.la.sensor.communicator;

import com.la.sensor.adapter.packet.InsideCommandPacket;
import com.la.sensor.displayer.MainActivity;
import com.la.sensor.communicator.client.console.LoginConsoleCommand;
import com.la.sensor.communicator.client.console.UploadConsoleCommand;
import com.la.sensor.communicator.client.handler.HeartBeatTimerHandler;
import com.la.sensor.communicator.client.handler.LoginResponseHandler;
import com.la.sensor.communicator.client.handler.MessageResponseHandler;
import com.la.sensor.communicator.codec.PacketDecoder;
import com.la.sensor.communicator.codec.PacketEncoder;
import com.la.sensor.communicator.codec.Splitter;
import com.la.sensor.communicator.handler.IMIdleStateHandler;
import com.la.sensor.communicator.util.LogString;
import com.la.sensor.communicator.util.SessionManager;
import com.la.sensor.obtainer.OAdapter;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Communicator {
    private static final int MAX_RETRY = 20;
    private static final String HOST = "222.20.24.85";
    private static final int PORT = 23333;
    private LogString logString;

    public CAdapter cAdapter = new CAdapter();

    public Communicator() {
        logString = new LogString();
    }

    public void initialize(OAdapter oAdapter) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Splitter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);

        cAdapter.initialize(oAdapter);
    }

    public void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                MainActivity.stateHandler.println(logString.info("连接成功！"));
                Channel channel = ((ChannelFuture) future).channel();
                operate(channel);

            } else if (retry == 0) {
                MainActivity.stateHandler.println(logString.err("重试次数已用完，放弃连接！"));

            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                MainActivity.stateHandler.println(logString.err("连接失败，第" + order + "次重连……"));
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);

                InsideCommandPacket tag = new InsideCommandPacket();
                Byte button = 1;
                tag.setButton(button);
                cAdapter.send(tag);
                MainActivity.stateHandler.info("here");
            }
        });
    }

    public void operate(Channel channel) {
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        UploadConsoleCommand uploadConsoleCommand = new UploadConsoleCommand();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        InsideCommandPacket tag = new InsideCommandPacket();
                        Byte button = 1;
                        tag.setButton(button);
                        cAdapter.send(tag);
                        if (!SessionManager.hasLogin(channel)) {
                            loginConsoleCommand.exec(channel);

                        } else {
                            //String message = "这是一条骚气的测试消息->_->";
                            //uploadConsoleCommand.exec(channel, message);
                            //InsideCommandPacket tag = new InsideCommandPacket();
                            //Byte button = 1;
                            //tag.setButton(button);
                            //cAdapter.send(tag);
                        }
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}

