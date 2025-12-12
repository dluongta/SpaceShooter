package com.example.spaceshooter.bullet;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.spaceshooter.object.GameObject;
import com.example.spaceshooter.plane.SmallPlane;


public class Bullet extends GameObject {
    protected int harm;

    public Bullet(Resources resources) {
        super(resources);
        initBitmap();
    }

    @Override
    protected void initBitmap() {

    }

    @Override
    public void drawSelf(Canvas canvas) {

    }

    @Override
    public void release() {

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
            if (obj instanceof SmallPlane) {
                if (object_y - speed < obj.getObject_y()) {
                    isAlive = false;
                    return true;
                }
            } else
                return false;
        }
        isAlive = false;
        return true;
    }


    public int getHarm() {
        return harm;
    }

    public void setHarm(int harm) {
        this.harm = harm;
    }
}
