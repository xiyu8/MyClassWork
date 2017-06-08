package com.example.testsurface;

/**
 * Created by 晞余 on 2017/5/18.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;

import com.example.testsurface.mode.Brick;
import com.example.testsurface.mode.Rail;

import static android.content.Context.SENSOR_SERVICE;

public class MySurfaceView extends SurfaceView implements Runnable, Callback {

    MainActivity context;

    private SurfaceHolder mHolder; // 用于控制SurfaceView
    private Thread myTh; // 声明一条线程
    private volatile boolean flag; // 线程运行的标识，用于控制线程

    private Canvas mCanvas; // 声明一张画布
    private Paint myPa; // 声明一支画笔
    int m_circle_r = 30;
    private Rail rail;
    private Brick[] bricks;

    public MySurfaceView(Context context) {
        super(context);
        this.context = (MainActivity) context;

        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        myPa = new Paint(); // 创建一个画笔对象
        myPa.setColor(Color.RED); // 设置画笔的颜色为白色
        setFocusable(true); // 设置焦点




    }




    @Override
    public void run() {
        while (flag) {
            try {
//                synchronized (mHolder) {
                    Thread.sleep(1); // 让线程休息100毫秒
                    Draw(); // 调用自定义画画方法
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (mCanvas != null) {
//                     mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。

                }
            }
        }
    }

    /**
     * 自定义一个方法，在画布上画一个圆
     */
    int i=1, j = 400,beforeI,beforeJ;
    Boolean b1=false,b2=false;
    int speed=20;
    int crashY=0,crashX=0;
    boolean reCreatBricks=false;
    int currentBricksNumbers=6*9;

    Paint p3; // 创建一个画笔对象
    protected void Draw() {

        mCanvas=null;
        p3=null;
        mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
        mCanvas.drawColor(Color.WHITE);
        p3=new Paint(); // 创建一个画笔对象

            myPa.setColor(Color.RED);


//            while(mCanvas==null){
//                mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
//
//            }
         //draw rail
            mCanvas.drawRect(rail.left_bottom_x,rail.left_bottom_y, rail.right_top_x, rail.right_top_y, myPa);//left bottom right top
            //draw bricks
            for (int i=0;i<bricks.length;i++){
                if(bricks[i]!=null) {
                    mCanvas.drawRect(bricks[i].left_bottom_x, bricks[i].left_bottom_y, bricks[i].right_top_x, bricks[i].right_top_y, myPa);//left bottom right top
                }
            }



            //testfy crash bricks
                for (int m = 0; m < bricks.length; m++) {
                    if(bricks[m]!=null) {
                        if (i + m_circle_r > bricks[m].left_bottom_x+margin/2 && i + m_circle_r < bricks[m].right_top_x+margin/2 && j + m_circle_r > bricks[m].right_top_y+margin/2 && j + m_circle_r < bricks[m].left_bottom_y+margin/2) {
                            bricks[m] = null;
                            crashY+=1;
                            currentBricksNumbers-=1;
                        }
//                        if (i + m_circle_r > bricks[m].left_bottom_x && i + m_circle_r < bricks[m].right_top_x && j + m_circle_r > bricks[m].right_top_y && j + m_circle_r < bricks[m].left_bottom_y) {
//                            bricks[m] = null;
//                            b2 = true;
//                        }
                    }
                }


            // check crash rail
            if(i+m_circle_r>rail.left_bottom_x && i+m_circle_r<rail.right_top_x && j+m_circle_r>rail.right_top_y && j+m_circle_r<rail.left_bottom_y){


//                if(
//                ((Math.abs(i+m_circle_r-rail.left_bottom_x) <= Math.abs(i+m_circle_r-rail.right_top_x)) ?
//                        Math.abs(i+m_circle_r-rail.left_bottom_x):Math.abs(i+m_circle_r-rail.right_top_x)) >
//                        ((Math.abs(j+m_circle_r-rail.right_top_y)<=Math.abs(j+m_circle_r-rail.left_bottom_y)) ?
//                                Math.abs(j+m_circle_r-rail.right_top_y):Math.abs(j+m_circle_r-rail.left_bottom_y))
//                ? true : false){
//                    crashY += 1;
//                }else {
                    crashX+=1;
//                }

                crashY+=1;
            }


            //check crash edge
            if(i+m_circle_r>=width){ crashX+=1;b1=true;}
            if(j+m_circle_r>=hight){crashY+=1;b2=true;}
            if(i<=m_circle_r){b1=false;}
            if(j<=m_circle_r){b2=false;}

            if(j<=m_circle_r){crashY+=1;}
            if(i<=m_circle_r){crashX+=1;}

            if (b1) {i-=speed;}else {i += speed;}
//            if (b2) {j-=speed;}else {j += speed;}
            if (crashY%2!=0) {j-=speed;}else {j += speed;}
//            if (crashX%2!=0) {i-=speed;}else {i += speed;}

            //check rebuild bricks
//            if(currentBricksNumbers==1){
//                createBricks(6,15,10,5);
//                currentBricksNumbers=6*15;
//            }

            //draw ball
            myPa.setColor(Color.BLUE);
            mCanvas.drawCircle(i, j, m_circle_r, myPa);


            mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
        }

















    /**
     * 当屏幕被触摸时调用
     */
    float x1,y1,x2,y2;
    float lastX,lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {


//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            //当手指按下的时候
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            Log.e("dcd", "XXXXXXXXXXXXXXXXXXX" + (x2 - x1));
//            rail.move((int)(x2-x1));
//
//
////            if(y1 - y2 > 50) {
////                Toast.makeText(this, "向上滑", Toast.LENGTH_SHORT).show();
////            } else if(y2 - y1 > 50) {
////                Toast.makeText(MainActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
////            } else if(x1 - x2 > 50) {
////                Toast.makeText(MainActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
////            } else if(x2 - x1 > 50) {
////                Toast.makeText(MainActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
////            }
//        }








        // 获取当前触摸的绝对坐标
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 上一次离开时的坐标
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                // 两次的偏移量
                int offsetX = (int) (rawX - lastX);
                int offsetY =(int) (rawY - lastY);
                rail.move(offsetX);
                rail.changeHeight(-offsetY);
                // 不断修改上次移动完成后坐标
                lastX = rawX;
                lastY = rawY;
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当用户按键时调用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        surfaceDestroyed(mHolder);
        return super.onKeyDown(keyCode, event);
    }












    /**
     * 当SurfaceView创建的时候，调用此函数
     */
    int number=20;
    int numEchoRow=5;
    int margin=60;
    int brickWidth=80,brickHeight=60;
    int hight,width,rows=6,cols=9;
    @Override
    public void surfaceCreated(SurfaceHolder holder) {





        hight=getHeight();
        width = getWidth();
        rail = new Rail(hight, width);
        createBricks(rows,cols,10,5);


        myTh = new Thread(this); // 创建一个线程对象
        flag = true; // 把线程运行的标识设置成true
        myTh.start(); // 启动线程
    }

    public void createBricks(int rows,int cols,int marginVer,int marginHori) {
        int bricksNum=cols*rows;
        bricks=new Brick[bricksNum];
        int brickWidth=(width-cols*marginHori)/cols;
        for(int i = 0; i<bricksNum; i++) {
            int row=i/cols;
            int col=i%cols;
            if(col==0){
                bricks[i]=new Brick(hight,width,marginHori/2+(marginHori+brickWidth)*(col),(marginVer+brickHeight)*(row),marginHori/2+(marginHori+brickWidth)*(col)+brickWidth,(marginVer+brickHeight)*(row)-brickHeight);
            }else {
                bricks[i]=new Brick(hight,width,marginHori/2+(marginHori+brickWidth)*(col),(marginVer+brickHeight)*(row),marginHori/2+(marginHori+brickWidth)*(col)+brickWidth,(marginVer+brickHeight)*(row)-brickHeight);
            }
        }

    }

    /**
     * 当SurfaceView的视图发生改变的时候，调用此函数
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * 当SurfaceView销毁的时候，调用此函数
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false; // 把线程运行的标识设置成false
        mHolder.removeCallback(this);
    }


    //////////////////////////////////////////////////Sensor/////////////////////////////////////////////////////////////
//    private SensorManager mSensorManager;
//    private Sensor mSensor;
//    public void initSensor() {
//        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
//        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);// TYPE_GRAVITY
//        if (null == mSensorManager) {
//            Log.d("vv", "deveice not support SensorManager");
//        }
//        // 参数三，检测的精准度
//        mSensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
//
//    }
//
//    public void getSensorData() {
//
//    }
//
//    float lastSensorX,lastSensorY;
//    SensorEventListener sensorEventListener=new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//        }
//    };
}