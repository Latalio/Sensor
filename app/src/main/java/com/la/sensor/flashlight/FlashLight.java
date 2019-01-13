package com.la.sensor.flashlight;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import com.la.sensor.displayer.MainActivity;

public class FlashLight {
    private MainActivity mainActivity;
    private CameraManager manager;

    public void initialize(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void on() {
        try {
            manager = (CameraManager) mainActivity.getSystemService(Context.CAMERA_SERVICE);
            manager.setTorchMode("0", true);// "0"是主闪光灯
        } catch (Exception e) {}

    }

    public void off() {
        try {
            if (manager == null) {
                return;
            }
            manager.setTorchMode("0", false);
        } catch (Exception e) {}

    }


}
