package com.la.sensor.displayer;

import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.la.sensor.R;
import com.la.sensor.communicator.util.LogString;
import com.la.sensor.communicator.Communicator;
import com.la.sensor.obtainer.Obtainer;


public class MainActivity extends AppCompatActivity {
    private Obtainer obtainer;

    public static DisplayHandler infoHandler;
    public static DisplayHandler stateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Layout initialize
        TextView sensorInfoView = findViewById(R.id.sensorInfo);
        TextView sensorStateView = findViewById(R.id.sensorState);

        //Logic initialize
        infoHandler = new DisplayHandler(sensorInfoView);
        stateHandler = new DisplayHandler(sensorStateView);

        //Operate
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        obtainer = new Obtainer(sensorManager);


        Communicator communicator = new Communicator();

        //Start sensor info update thread

        obtainer.initialize(communicator.cAdapter);
        obtainer.start();

        communicator.initialize(obtainer.oAdapter);

    }

    @Override
    protected void onResume() {
        obtainer.register();
        super.onResume();
    }

    @Override
    protected void onPause() {
        obtainer.unregister();
        super.onPause();
    }

    public static class DisplayHandler extends Handler {
        private TextView textView;

        private static final int UPDATE_SET = 1;
        private static final int UPDATE_APPEND = 2;

        private DisplayHandler(TextView textView) { this.textView = textView; }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SET:
                    textView.setText((String)msg.obj);
                    break;
                case UPDATE_APPEND:
                    textView.append((String)msg.obj);
                    break;
                default:
                    break;
            }
        }

        public void printre(String string) {
            Message message = new Message();
            message.what = UPDATE_SET;
            message.obj = string;
            this.sendMessage(message);
        }

        public void println(String string) {
            Message message = new Message();
            message.what = UPDATE_APPEND;
            message.obj = string;
            this.sendMessage(message);
        }

        public void info(String string) {
            println(LogString.info(string));
        }


    }
}


