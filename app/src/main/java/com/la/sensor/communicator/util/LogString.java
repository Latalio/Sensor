package com.la.sensor.communicator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogString {

    public static String info(String content) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //显示的格式
        String date = simpleDateFormat.format(new Date());
        return date + "INFO - " + content + "\n";
    }

    public static String err(String content) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss "); //显示的格式
        String date = simpleDateFormat.format(new Date());
        return date + "ERROR - " + content + "\n";
    }
}
