package com.la.sensor.adapter;

import com.la.sensor.communicator.protocol.Packet;
import com.la.sensor.communicator.protocol.PacketCodec;
import com.la.sensor.communicator.serialize.Serializer;
import com.la.sensor.displayer.MainActivity;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static com.la.sensor.communicator.protocol.PacketCodec.MAGIC_NUMBER;

public class Adapter {

    private PipedInputStream pipedInputStream = new PipedInputStream();
    private PipedOutputStream pipedOutputStream = new PipedOutputStream();

    public void connect(Adapter otherAdapter) {
        try {
            pipedInputStream.connect(otherAdapter.pipedOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Packet packet) {
        PacketCodec.INSTANCE.send(packet, pipedOutputStream);
        //MainActivity.stateHandler.info("sended");
    }

    public Packet get() {
        return PacketCodec.INSTANCE.get(pipedInputStream);
    }

    public void close() {}


    /*
    public void write(byte[] b) {
        try {
            pipedOutputStream.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void read() {
    }
    */
}
