package com.la.sensor.adapter;

import com.la.sensor.adapter.packet.InsideCommandPacket;
import com.la.sensor.adapter.packet.InsideMessagePacket;
import com.la.sensor.communicator.protocol.Packet;

public class AdapterTest {
    public static void main(String[] args) {
        Adapter AAdapter = new Adapter();
        Adapter BAdapter = new Adapter();

        AAdapter.connect(BAdapter);
        BAdapter.connect(AAdapter);

        //InsideMessagePacket insideMessagePacket = new InsideMessagePacket();
        //insideMessagePacket.setMessage("MDZZ");
        //AAdapter.send(insideMessagePacket);
        InsideCommandPacket tag = new InsideCommandPacket();
        Byte button = 1;
        tag.setButton(button);
        AAdapter.send(tag);

        InsideCommandPacket packet = (InsideCommandPacket)BAdapter.get();
        System.out.println(packet.getButton());
        //InsideMessagePacket packet = (InsideMessagePacket)BAdapter.get();
        //System.out.println(packet.getMessage());
    }
}
