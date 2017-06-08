package com.example.testsurface.mode;

import android.util.Log;

/**
 * Created by 晞余 on 2017/5/25.
 */

public class Brick {
    public int left_bottom_x;
    public int left_bottom_y;
    public int right_top_x;
    public int right_top_y;

    public Brick(int screenHeight,int screenWith,int l_b_x,int l_b_y,int r_t_x,int r_t_y) {
        right_top_y= r_t_y;
        left_bottom_y=l_b_y;
        left_bottom_x=l_b_x;
        right_top_x = r_t_x;
    }

}
