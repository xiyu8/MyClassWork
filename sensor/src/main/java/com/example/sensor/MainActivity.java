package com.example.sensor;

import java.util.List;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  getSensorList();
     //   SensorControl();
        //Toast.makeText(this, "xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        xx();
    }

    @SuppressLint("NewApi")
    private void getSensorList() {
        // 获取传感器管理器
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 获取全部传感器列表
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // 打印每个传感器信息
        StringBuilder strLog = new StringBuilder();
        int iIndex = 1;
        for (Sensor item : sensors) {
            strLog.append(iIndex + ".");
            strLog.append("	Sensor Type : " + item.getType() + "\r\n");
            strLog.append("	Sensor Name : " + item.getName() + "\r\n");
            strLog.append("	Sensor Version : " + item.getVersion() + "\r\n");
            strLog.append("	Sensor Vendor : " + item.getVendor() + "\r\n");
            strLog.append("	Maximum Range : " + item.getMaximumRange() + "\r\n");
            strLog.append("	Minimum Delay : " + item.getMinDelay() + "\r\n");
            strLog.append("	Power : " + item.getPower() + "\r\n");
            strLog.append("	Resolution : " + item.getResolution() + "\r\n");
            strLog.append("\r\n");
            iIndex++;
        }
        System.out.println(strLog.toString());
        ((TextView) findViewById(R.id.listSensor)).setText(strLog.toString());
    }


    public void SensorControl() {
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor lightSensor=sensorManager.getDefaultSensor(5);
        SensorEventListener sensorListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent event) {
                int sensorType = event.sensor.getType();
                //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
                float[] values = event.values;
                if(sensorType == android.hardware.Sensor.TYPE_ACCELEROMETER){


                    Log.e("sensor ", "============ values[0] = " + values[0]);
                    Log.e("sensor ", "============ values[1] = " + values[1]);
                    Log.e("sensor ", "============ values[2] = " + values[2]);


                    if((Math.abs(values[0])>17||Math.abs(values[1])>17||Math.abs(values[2])>17)){
                        Log.e("sensor ", "============ values[0] = " + values[0]);
                        Log.e("sensor ", "============ values[1] = " + values[1]);
                        Log.e("sensor ", "============ values[2] = " + values[2]);
//                        tv.setText("摇一摇"+"\n"+values[0]+"\n"+values[1]+"\n"+values[2]);
//                        //摇动手机后，再伴随震动提示~~
//                        vibrator.vibrate(500);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(sensorListener, lightSensor, 5);
        //sensorManager.unregisterListener(sensorListener, lightSensor);
    }


















        private SensorManager sm;
        //需要两个Sensor
        private Sensor aSensor;
        private Sensor mSensor;
        float[] accelerometerValues = new float[3];
        float[] magneticFieldValues = new float[3];
        private static final String TAG = "sensor";


        final SensorEventListener myListener = new SensorEventListener() {
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                    magneticFieldValues = sensorEvent.values;
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                    accelerometerValues = sensorEvent.values;
                calculateOrientation();
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };


    public void xx() {

        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        calculateOrientation();

    }

        private void calculateOrientation() {


            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
            SensorManager.getOrientation(R, values);
            // 要经过一次数据格式的转换，转换为度
            values[0] = (float) Math.toDegrees(values[0]);
            Log.i(TAG, values[0]+"");
            values[1] = (float) Math.toDegrees(values[1]);
            values[2] = (float) Math.toDegrees(values[2]);
            if(values[0] >= -5 && values[0] < 5){
                Log.i(TAG, "正北");

                //Toast.makeText(this, "正北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            else if(values[0] >= 5 && values[0] < 85){
                Log.i(TAG, "东北");

                //Toast.makeText(this, "东北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            else if(values[0] >= 85 && values[0] <=95){
                Log.i(TAG, "正东");

                //Toast.makeText(this, "正东xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            else if(values[0] >= 95 && values[0] <175){
                Log.i(TAG, "东南");

                //Toast.makeText(this, "东南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
                Log.i(TAG, "正南");

                //Toast.makeText(this, "正南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            else if(values[0] >= -175 && values[0] <-95){
                Log.i(TAG, "西南");

                //Toast.makeText(this, "西南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            else if(values[0] >= -95 && values[0] < -85){
                Log.i(TAG, "正西");
            }
            else if(values[0] >= -85 && values[0] <-5){
                Log.i(TAG, "西北");

                //Toast.makeText(this, "西北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
        }





    //再次强调：注意activity暂停的时候释放
    public void onPause(){
        sm.unregisterListener(myListener);
        super.onPause();
    }


    public void getOrientari() {

    }










    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
