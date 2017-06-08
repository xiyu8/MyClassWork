package com.example.playaudiotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recorder();
//        initializeAudio();
        initView();


    }

    private void initView() {
        Button play = (Button) findViewById(R.id.play);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
    }
////////////////////////////////////////play//////////////////////////////////////////////////////////////
    private void initMediaPlayer() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        } else {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), "music.aac");

//                mediaPlayer.setDataSource(file.getPath()); // ???????????��??
                mediaPlayer.setDataSource(voicePath); // ???????????��??
                mediaPlayer.prepare(); // ??MediaPlayer?????????
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "????????????��???", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // ???????
                }
                break;
            case R.id.pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // ???????
                }
                break;
            case R.id.stop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset(); // ??????
                    initMediaPlayer();
                }
                break;
            case R.id.recorder:
                initializeAudio(); break;
            case R.id.stop_recorder:
                recorder.stop();// 停止刻录
                // recorder.reset(); // 重新启动MediaRecorder.
                recorder.release(); // 刻录完成一定要释放资源
                // recorder = null;

                 break;


            default:
                break;
        }
    }

    //////////////////////////////////////////////recorder///////////////////////////////////////////////////////////


    private MediaRecorder mRecorder,recorder;

    String voicePath;
    public void recorder() {
        mRecorder = new MediaRecorder();

        File file = new File(Environment.getExternalStorageDirectory(), "ione.pcm");
        voicePath = file.getPath();
        Log.e("dd", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+voicePath);
//        voicePath=Environment.getExternalStorageDirectory().toString()+"/ione.pcm";
        //设置音源为Micphone
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置封装格式
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(voicePath);
        //设置编码格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("cc", "prepare() failed");
        }

    }

    public void startRecorder() {
        //录音
        mRecorder.start();
    }

    public boolean stopRecorder() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        return false;
    }

    private void initializeAudio() {
        recorder = new MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        recorder.setOutputFile("/sdcard/peipei.amr");
        // 设置录制好的音频文件保存路径
        try {
            recorder.prepare();// 准备录制
            recorder.start();// 开始录制
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





























    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
