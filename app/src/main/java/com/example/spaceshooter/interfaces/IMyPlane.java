package com.example.spaceshooter.interfaces;


import android.graphics.Canvas;

import com.example.spaceshooter.plane.EnemyPlane;

import java.util.List;


public interface IMyPlane {


    float getMiddle_x();


    void setMiddle_x(float middle_x);


    float getMiddle_y();


    void setMiddle_y(float middle_y);

    boolean isChangeBullet();


    void setChangeBullet(boolean isChangeBullet);


    void shoot(Canvas canvas, List<EnemyPlane> planes);

    void initBullet();

    void changeBullet(int type);
}
