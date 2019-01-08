package com.la.sensor.communicator.protocol.request;


import com.la.sensor.communicator.protocol.Packet;


import static com.la.sensor.communicator.protocol.command.Command.LOGIN_REQUEST;

public class LoginRequestPacket extends Packet {
    private String userName;
    private String password;
    private String type;

    @Override
    public Byte getCommand() {

        return LOGIN_REQUEST;
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getType() { return type; }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setType(String type) { this.type = type; }
}
