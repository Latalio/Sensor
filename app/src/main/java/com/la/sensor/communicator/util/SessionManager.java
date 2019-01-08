package com.la.sensor.communicator.util;




import com.la.sensor.communicator.attribute.Attributes;
import com.la.sensor.communicator.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class SessionManager {
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static final List<String> sensorGroup = new ArrayList<>();
    public static final List<String> monitorGroup = new ArrayList<>();

    //bind channel to session[userId, userName, userType]
    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unbindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            userIdChannelMap.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
            System.out.println(session + " 退出登录!");
        }
    }

    public static boolean hasLogin(Channel channel) {

        return getSession(channel) != null;
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChannelMap.get(userId);
    }

}
