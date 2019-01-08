package com.la.sensor.communicator.protocol;

import io.netty.buffer.ByteBuf;

import com.la.sensor.adapter.packet.InsideCommandPacket;
import com.la.sensor.adapter.packet.InsideMessagePacket;
import com.la.sensor.communicator.protocol.request.*;
import com.la.sensor.communicator.protocol.response.*;
import com.la.sensor.communicator.serialize.Serializer;
import com.la.sensor.communicator.serialize.impl.JSONSerializer;


import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.la.sensor.communicator.protocol.command.Command.*;

public class PacketCodec {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodec INSTANCE = new PacketCodec();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;


    private PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);

        packetTypeMap.put(MESSAGE, InsideMessagePacket.class);
        packetTypeMap.put(COMMAND, InsideCommandPacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 2. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {

        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }

    public void send(Packet packet, PipedOutputStream pipedOutputStream) {
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 2. 实际编码过程
        try {
            pipedOutputStream.write(MAGIC_NUMBER);
            pipedOutputStream.write(packet.getVersion());
            pipedOutputStream.write(Serializer.DEFAULT.getSerializerAlgorithm());
            pipedOutputStream.write(packet.getCommand());
            pipedOutputStream.write(bytes.length);
            pipedOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet get(PipedInputStream pipedInputStream) {
        try {
            //这里有一个bug
            //关于pipedOutputStream.write()
            // 跳过 magic number
            pipedInputStream.skip(1);
            //int magicNumber = pipedInputStream.read();
            //System.out.println(magicNumber);

            // 跳过版本号
            pipedInputStream.skip(1);

            // 序列化算法
            int serializeAlgorithm = pipedInputStream.read();

            // 指令
            int command = pipedInputStream.read();
            System.out.println(command);

            // 数据包长度
            int length = pipedInputStream.read();

            byte[] bytes = new byte[length];
            pipedInputStream.read(bytes);

            Class<? extends Packet> requestType = PacketCodec.INSTANCE.getRequestType((byte)command);
            Serializer serializer = getSerializer((byte)serializeAlgorithm);

            if (requestType != null && serializer != null) {
                return serializer.deserialize(requestType, bytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
