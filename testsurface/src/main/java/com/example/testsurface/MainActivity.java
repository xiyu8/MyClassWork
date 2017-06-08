package com.example.testsurface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((FrameLayout)findViewById(R.id.dd)).addView(new MySurfaceView(this));
//        aboutSurface();
    }













































    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    public void aboutSurface() {

//        surfaceView = (SurfaceView) findViewById(R.id.xxsurface);
        surfaceHolder = surfaceView.getHolder();


        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        surfaceHolder.addCallback(this); // 回调接口


    }



















    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        surfaceHolder.removeCallback(this);
        super.onDestroy();
    }
}
