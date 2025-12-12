package com.example.spaceshooter.bullet;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.example.spaceshooter.object.GameObject;


public class EnemyBullet extends Bullet {

	public EnemyBullet(Resources resources) {
		super(resources);
		this.harm = 1;
	}

	@Override
	public void initial(int arg0, float arg1, float arg2) {
		
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
		if (object_y >= 0) {
			object_y -= speed;
		} else {
			isAlive = false;
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
		isAlive = false;
		return true;
	}
}
