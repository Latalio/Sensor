package com.la.sensor.communicator.protocol.response;


import com.la.sensor.communicator.protocol.Packet;


import static com.la.sensor.communicator.protocol.command.Command.LOGIN_RESPONSE;

public class LoginResponsePacket extends Packet {
    private String userId;
    private String userName;
    private String reason;

    private boolean success;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getReason() { return reason; }

    public boolean isSuccess() { return success; }


    public void setUserId(String userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setReason(String reason) { this.reason = reason; }

    public void setSuccess(boolean success) { this.success = success; }


}
