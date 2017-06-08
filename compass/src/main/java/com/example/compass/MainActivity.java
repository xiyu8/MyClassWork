package com.example.compass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_LOW;
import static android.hardware.SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, SpeechSynthesizerListener {

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialEnv();
        initialTts();
        init();
        initSener();

    }


    ImageView tin;
    SurfaceView surfaceView;
    TextView ar_text;
    SurfaceHolder surfaceHolder;
    LinearLayout arView;
    public void init() {
        tin = (ImageView) findViewById(R.id.tin);
        ar_text = (TextView) findViewById(R.id.ar_position);
        surfaceView = (SurfaceView) findViewById(R.id.ar_surface);
        arView = (LinearLayout) findViewById(R.id.arView);



/////////////////////////////surfacaView 相关////////////////////////////////////
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        surfaceHolder.addCallback(this); // 回调接口

    }

    public void initSener() {
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(myListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(myListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        calculateOrientation();
    }

    private SensorManager sm;
    //需要两个Sensor
    private Sensor aSensor;
    private Sensor mSensor;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private static final String TAG = "sensor";

    public void changeTin() {
        final RotateAnimation animation =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(200);
        tin.startAnimation(animation);
    }



    //传感器 监听回调
    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = sensorEvent.values;

            calculateOrientation();
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            if (accuracy<SensorManager.SENSOR_STATUS_ACCURACY_LOW)
                popDialog(true);
            if (accuracy>=SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM)
                popDialog(false);

        }
    };
    //处理 监听回调的数据
    float currentDegree=0.0f;
    Boolean isVertical=false;
    RotateAnimation animation;
    String tmp="xx";
    private void calculateOrientation() {


        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);  //getOrientation得到的values数组：[0]方向角 [1]俯仰角 [2]翻转角
        // 要经过一次数据格式的转换，转换为度
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        Boolean isCurrentVertical;
        if(isVertical)
            isCurrentVertical = (Math.abs(values[1]) > 30);
        else
            isCurrentVertical= (Math.abs(values[1]) >50);


        if (!isCurrentVertical) {
            isVertical = false;
            arView.setVisibility(View.GONE);
            surfaceHolder.removeCallback(this);
            //转指针
            animation =new RotateAnimation(currentDegree,-values[0],Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(200);
            tin.startAnimation(animation);
            currentDegree = -values[0];
        }else {
            isVertical = true;

            SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);
            SensorManager.getOrientation(R, values);  //getOrientation得到的values数组：[0]方向角 [1]俯仰角 [2]翻转角
            // 要经过一次数据格式的转换，转换为度
            values[0] = (float) Math.toDegrees(values[0]);
            values[1] = (float) Math.toDegrees(values[1]);
            values[2] = (float) Math.toDegrees(values[2]);

//            RotateAnimation ra = new RotateAnimation(0.0f,1.0f);
//            ra.setDuration(200);
            AlphaAnimation aa = new AlphaAnimation(0f, 1f);
            aa.setDuration(200);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(animation);
            set.addAnimation(aa);
            tin.startAnimation(set);

            arView.setVisibility(View.VISIBLE);
            String temp = getDicString(values[0]);
//            if(!tmp.equals(temp)){
//                tmp = temp;
//                mSpeechSynthesizer.speak(tmp);
//            }
            a = getDicString(values[0]);
            ar_text.setText(a);

            surfaceHolder.addCallback(this); // 回调接口

//            if(!orientation.equals(a)){
//                orientation=a;
//                mSpeechSynthesizer.speak(orientation);
//            }
        }



//        if(values[0] >= -5 && values[0] < 5){
//            Log.i(TAG, "正北");
//
//            //Toast.makeText(this, "正北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
//        else if(values[0] >= 5 && values[0] < 85){
//            Log.i(TAG, "东北");
//
//            //Toast.makeText(this, "东北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
//        else if(values[0] >= 85 && values[0] <=95){
//            Log.i(TAG, "正东");
//
//            //Toast.makeText(this, "正东xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
//        else if(values[0] >= 95 && values[0] <175){
//            Log.i(TAG, "东南");
//
//            //Toast.makeText(this, "东南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
//        else if((values[0] >= 175 && values[0] <= 180) || (values[0]) >= -180 && values[0] < -175){
//            Log.i(TAG, "正南");
//
//            //Toast.makeText(this, "正南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
//        else if(values[0] >= -175 && values[0] <-95){
//            Log.i(TAG, "西南");
//
//            //Toast.makeText(this, "西南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
//        else if(values[0] >= -95 && values[0] < -85){
//            Log.i(TAG, "正西");
//        }
//        else if(values[0] >= -85 && values[0] <-5){
//            Log.i(TAG, "西北");
//
//            //Toast.makeText(this, "西北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
//        }
    }

    String orientation="xx",a="mm";
    public String getDicString(float values) {
        String a = "";

        if(values>= -5 && values < 5){
            Log.i(TAG, "正北");
            a="正北";

            //Toast.makeText(this, "正北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        else if(values >= 5 && values < 85){
            Log.i(TAG, "东北");
            a="东北";

            //Toast.makeText(this, "东北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        else if(values>= 85 && values <=95){
            Log.i(TAG, "正东");
            a="正东";

            //Toast.makeText(this, "正东xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        else if(values>= 95 && values<175){
            Log.i(TAG, "东南");
            a="东南";

            //Toast.makeText(this, "东南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        else if((values>= 175 && values <= 180) || (values) >= -180 && values < -175){
            Log.i(TAG, "正南");
            a="正南";

            //Toast.makeText(this, "正南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        else if(values>= -175 && values <-95){
            Log.i(TAG, "西南");
            a="西南";

            //Toast.makeText(this, "西南xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        else if(values >= -95 && values < -85){
            Log.i(TAG, "正西");
            a="正西";
        }
        else if(values>= -85 && values <-5){
            Log.i(TAG, "西北");
            a="西北";

            //Toast.makeText(this, "西北xxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
        }
        return a;
    }





//    private void releaseCamera(){
//        if(camera != null){
//            camera.setPreviewCallback(null);
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // SurfaceView尺寸发生改变时（首次在屏幕上显示同样会调用此方法），初始化mCamera参数，启动Camera预览


//        mCamera = getCamera(0);


        Camera.Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
        Camera.Size largestSize = getBestSupportedSize(parameters.getSupportedPreviewSizes());
        parameters.setPreviewSize(largestSize.width, largestSize.height);// 设置预览图片尺寸
        largestSize = getBestSupportedSize(parameters.getSupportedPictureSizes());// 设置捕捉图片尺寸
        parameters.setPictureSize(largestSize.width, largestSize.height);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);

        try {
            mCamera.startPreview();
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        // SurfaceView创建时，建立Camera和SurfaceView的联系
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceView销毁时，取消Camera预览
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }









    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes) {
        // 取能适用的最大的SIZE
        Camera.Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }


    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        // 开启相机
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
            // i=0 表示后置相机
        } else
            mCamera = Camera.open();
    }

    //再次强调：注意activity暂停的时候释放
    public void onPause(){
        sm.unregisterListener(myListener); // 释放相机
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }



















    AlertDialog alertDialog;
    public void popDialog(Boolean b) {
        if (alertDialog == null) {
            alertDialog=new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("有干扰")
                    // .setPositiveButton("确定", null)
                    .create();

        }
        if (b) {
            alertDialog.show();
        } else {
            alertDialog.dismiss();
        }

    }











    EditText text;
//    private void initView() {
//        text = (EditText) findViewById(R.id.myText);
//    }

//    public void onClick(View v){
//        switch (v.getId()) {
//            case R.id.sp:
//                mSpeechSynthesizer.speak(text.getText().toString()); break;
//            default: break;
//        }
//    }

    // 语音合成客户端
    private SpeechSynthesizer mSpeechSynthesizer;

    String mSampleDirPath;
    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }
    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void initialTts() {


        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);


        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(this);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(MainActivity.this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
                + LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId("9632601"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey("6gesDegdKjNhCzclIuGG1g2q",
                "9b2ee130265b1a635607785b1d0afd9a"/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);

        if (authInfo.isSuccess()) {
            // toPrint("auth success");
            mSpeechSynthesizer.initTts(TtsMode.MIX);
            mSpeechSynthesizer.speak("百度语音合成示例程序正在运行");
        } else {
            Log.e("HH", "HHHHHHHHHHHHHHHHHHHHHHHHH");// 授权失败
        }

    }


    // 初始化语音合成客户端并启动
    private void startTTS() {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(this);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("6gesDegdKjNhCzclIuGG1g2q", "9b2ee130265b1a635607785b1d0afd9a");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("9632601");
        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, "assets:///bd_etts_ch_text.dat");
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, "assets:///bd_etts_ch_speech_female.dat");
        // 设置语音合成声音授权文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, "assets:///your_licence_path");
        // 获取语音合成授权信息
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
        if (authInfo.isSuccess()) {
            mSpeechSynthesizer.initTts(TtsMode.MIX);
            mSpeechSynthesizer.speak("百度语音合成示例程序正在运行");
            Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT");// 授权失败
        } else {
            Log.e("HH", "HHHHHHHHHHHHHHHHHHHHHHHHH");// 授权失败
        }

    }

    public void onError(String arg0, SpeechError arg1) {
        // 监听到出错，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT1");// 授权失败
    }

    public void onSpeechFinish(String arg0) {
        // 监听到播放结束，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT2");// 授权失败
    }

    public void onSpeechProgressChanged(String arg0, int arg1) {
        // 监听到播放进度有变化，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT3");// 授权失败
    }

    public void onSpeechStart(String arg0) {
        // 监听到合成并播放开始，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT4");// 授权失败
    }

    public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
        // 监听到有合成数据到达，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT5");// 授权失败
    }

    public void onSynthesizeFinish(String arg0) {
        // 监听到合成结束，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT6");// 授权失败
    }

    public void onSynthesizeStart(String arg0) {
        // 监听到合成开始，在此添加相关操作
        Log.e("HH", "TTTTTTTTTTTTTTTTTTTTTTTTTTTT7");// 授权失败
    }




    private ScheduledExecutorService scheduledExecutorService;  //定时器
    @Override
    public void onStart() {
//        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 8, TimeUnit.SECONDS);
        super.onStart();
    }


    @Override
    protected void onDestroy() {

        scheduledExecutorService.shutdown();
        super.onDestroy();
    }


    private class ScrollTask implements Runnable {

        public void run() {
            runOnUiThread(new Runnable(){

                @Override
                public void run() {

                    if(!orientation.equals(a)){
                        orientation=a;
                        mSpeechSynthesizer.speak(orientation);
                    }

                }
            });
        }
    }
}
