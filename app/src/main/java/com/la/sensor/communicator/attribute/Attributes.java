package com.la.sensor.communicator.attribute;

import com.la.sensor.communicator.session.Session;

import io.netty.util.AttributeKey;


public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
