package com.la.sensor.obtainer;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.la.sensor.adapter.Adapter;
import com.la.sensor.communicator.CAdapter;
import com.la.sensor.displayer.MainActivity;


public class Obtainer {
    private SensorManager sensorManager;
    private AccelerometerListener accelerometerListener;
    private MagneticFieldListener magneticFieldListener;

    public OAdapter oAdapter = new OAdapter();

    public Obtainer(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public void initialize(CAdapter cAdapter) {
        accelerometerListener = new AccelerometerListener();
        magneticFieldListener = new MagneticFieldListener();

        oAdapter.initialize(cAdapter);
        oAdapter.start();
        MainActivity.stateHandler.info("Obtainer here");
    }

    public void register() {
        //
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(magneticFieldListener, magneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(magneticFieldListener);
    }

    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String info;
                String infoAccelerometer;
                String infoMagneticField;

                try {
                    while (true) {
                        if (oAdapter.getDataObtainTag() == 1) {
                            infoAccelerometer = "Accelerometer:\n" +
                                    String.format("[x-%f], [y-%f], [z-%f]\n",
                                            accelerometerListener.getData_x(),
                                            accelerometerListener.getData_y(),
                                            accelerometerListener.getData_z());
                            infoMagneticField = "MagneticField:\n" +
                                    String.format("[x-%f], [y-%f], [z-%f]\n",
                                            magneticFieldListener.getData_x(),
                                            magneticFieldListener.getData_y(),
                                            magneticFieldListener.getData_z());
                            info = infoAccelerometer + infoMagneticField;
                            MainActivity.infoHandler.printre(info);
                            Thread.sleep(100);
                        } else if (oAdapter.getDataObtainTag() == 0) {

                        } else {
                            System.out.println("Data Obtain Tag Error!");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
