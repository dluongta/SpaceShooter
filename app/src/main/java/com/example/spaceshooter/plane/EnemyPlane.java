package com.example.spaceshooter.plane;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.spaceshooter.object.GameObject;


public class EnemyPlane extends GameObject {
    protected int score;                         
    protected int blood;                         
    protected int bloodVolume;                   
    protected boolean isExplosion;             
    protected boolean isVisible;                 
    public int speedTime;

    public EnemyPlane(Resources resources) {
        super(resources);
        initBitmap(); 
    }


    @Override
    public void initial(int arg0, float arg1, float arg2) {
        speedTime = arg0;
    }

    @Override
    public void initBitmap() {

    }

    @Override
    public void drawSelf(Canvas canvas) {
    }

    @Override
    public void release() {

    }

    @Override
    public void logic() {
        if (object_y < screen_height) {
            object_y += speed;
        } else {
            isAlive = false;
        }
        if (object_y + object_height > 0) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }


    public void attacked(int harm) {
        blood -= harm;
        if (blood <= 0) {
            isExplosion = true;
        }
    }


    @Override
    public boolean isCollide(GameObject obj) {
        if (object_x <= obj.getObject_x()
                && object_x + object_width <= obj.getObject_x()) {
            return false;
        }
        else if (obj.getObject_x() <= object_x
                && obj.getObject_x() + obj.getObject_width() <= object_x) {
            return false;
        }
        else if (object_y <= obj.getObject_y()
                && object_y + object_height <= obj.getObject_y()) {
            return false;
        }
        else if (obj.getObject_y() <= object_y
                && obj.getObject_y() + obj.getObject_height() <= object_y) {
            return false;
        }
        return true;
    }


    public boolean isCanCollide() {
        return isAlive && !isExplosion && isVisible;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getBloodVolume() {
        return bloodVolume;
    }

    public void setBloodVolume(int bloodVolume) {
        this.bloodVolume = bloodVolume;
    }

    public boolean isExplosion() {
        return isExplosion;
    }
}

