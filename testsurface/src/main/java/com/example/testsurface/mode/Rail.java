package com.example.testsurface.mode;

import android.util.Log;

/**
 * Created by 晞余 on 2017/5/25.
 */

public class Rail {
    public int left_bottom_x;
    public int left_bottom_y;
    public int right_top_x;
    public int right_top_y;

    public int width;
    private int height=30;
    private int screenHeight,screenWith;

    public Rail(int screenHeight,int screenWith) {

        this.screenWith=screenWith;
        this.screenHeight = screenHeight;

        width=600;
        right_top_y= screenHeight-(50+height);
        left_bottom_y=screenHeight-50;

        left_bottom_x=(screenWith-width)/2;
        Log.e("cc", "" + screenHeight + "((((((((((((((((((((((((((((((((((((((((((((" + screenWith);
        right_top_x = screenWith - left_bottom_x;
//        mCanvas.drawRect(50, getHeight()-50, getWidth()-50, getHeight()-100, myPa);//left bottom right top
    }

    public void move(int length){
            left_bottom_x=left_bottom_x+length;
            right_top_x=right_top_x+length;
    }
    public void changeHeight(int i) {
        height+=i;
        right_top_y= screenHeight-(50+height);
        left_bottom_y=screenHeight-50;
    }
}
