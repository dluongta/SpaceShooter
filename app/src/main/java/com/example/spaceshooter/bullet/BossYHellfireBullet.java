package com.example.spaceshooter.bullet;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.spaceshooter.myplane.R;
import com.example.spaceshooter.object.GameObject;

import java.util.Random;


public class BossYHellfireBullet extends EnemyBullet {

	private Bitmap bullet;

	public BossYHellfireBullet(Resources resources) {
		super(resources);
	}

	@Override
	public void initial(int arg0, float arg1, float arg2) {
		isAlive = true;
		Random random = new Random();
		speed = random.nextInt(5) + 5;
		object_x = arg1 - object_width / 2;
		object_y = arg1 + 2*object_height;
		
	}

	@Override
	public void initBitmap() {
		bullet = BitmapFactory.decodeResource(resources, R.drawable.boss_bullet_hellfire_yellow);
		object_width = bullet.getWidth();
		object_height = bullet.getHeight();
	}

	@Override
	public void drawSelf(Canvas canvas) {
		if (isAlive) {
			canvas.save();
			canvas.clipRect(object_x, object_y, object_x + object_width,
					object_y + object_height);
			canvas.drawBitmap(bullet, object_x, object_y, paint);
			canvas.restore();
			logic();
		}
	}

	@Override
	public void release() {
		if (!bullet.isRecycled()) {
			bullet.recycle();
		}
	}

	@Override
	public void logic() {
		if (object_y >= 0) {
			object_y -= speed;
			object_x += Math.tan(System.currentTimeMillis()/1000);
			
		} else {
			isAlive = false;
		}
	}

	@Override
	public boolean isCollide(GameObject obj) {
		return super.isCollide(obj);
	}

}
