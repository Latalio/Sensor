package com.la.sensor.communicator;

import com.la.sensor.adapter.NAdapter;
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
import com.la.sensor.obtainer.Obtainer;

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
    private static final String HOST = "193.112.144.64";
    private static final int PORT = 23333;
    private LogString logString;

    public NAdapter adapter = new NAdapter();


    public Communicator() {
        logString = new LogString();
    }

    public void initialize() {
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
                        ch.pipeline().addLast(new MessageResponseHandler(adapter));
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
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
                        if (!SessionManager.hasLogin(channel)) {
                            loginConsoleCommand.exec(channel);

                        } else if(adapter.dataSendTag == 1) {
                            String message = adapter.data;
                            uploadConsoleCommand.exec(channel, message);
                            Thread.sleep(100);
                        } else if(adapter.dataSendTag == 0) {
                        } else {
                            MainActivity.stateHandler.println("(Communicator)data send tag error!");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }



}

