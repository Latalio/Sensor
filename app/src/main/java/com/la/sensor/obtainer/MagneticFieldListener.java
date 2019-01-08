package com.la.sensor.obtainer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class MagneticFieldListener implements SensorEventListener {
    private float data_x;
    private float data_y;
    private float data_z;

    //Build function
    public MagneticFieldListener() {}

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){

        data_x = sensorEvent.values[0];
        data_y = sensorEvent.values[1];
        data_z = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getData_x() {
        return data_x;
    }

    public float getData_y() {
        return data_y;
    }

    public float getData_z() {
        return data_z;
    }
}
