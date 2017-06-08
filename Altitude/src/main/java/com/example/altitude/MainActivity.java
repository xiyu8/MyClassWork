package com.example.altitude;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    TextView mAltitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAltitude = (TextView) findViewById(R.id.mAltitude);
        dd();
    }


    SensorManager sensorManager;
    Sensor mPressure;
    public void dd() {
        sensorManager = null;
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        sensorManager.registerListener(pressureListener, mPressure,SensorManager.SENSOR_DELAY_NORMAL);

        if(mPressure == null)
        {
//            mPressureVal.setText("您的手机不支持气压传感器，无法使用本软件功能.");
            return;
        }
        Sensor mAccelerate = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


    SensorEventListener pressureListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            float sPV = event.values[0];
            double height = 44330000*(1-(Math.pow(sPV/1013.25,1.0/5255.0)));
            Log.e("ff", "^^^^^^^^^^^^^^^^^^^^^^^^^^^"+height);
            mAltitude.setText(""+height);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(pressureListener!=null){
            sensorManager.unregisterListener(pressureListener);
        }
        super.onPause();
    }
}
