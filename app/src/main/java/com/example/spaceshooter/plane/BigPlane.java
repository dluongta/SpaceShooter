package com.example.spaceshooter.plane;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.spaceshooter.bullet.BigPlaneBullet;
import com.example.spaceshooter.bullet.Bullet;
import com.example.spaceshooter.constant.GameConstant;
import com.example.spaceshooter.factory.GameObjectFactory;
import com.example.spaceshooter.myplane.R;
import com.example.spaceshooter.object.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BigPlane extends EnemyPlane {
	private static int currentCount = 0; 
	public static int sumCount = GameConstant.BIGPLANE_COUNT; 
	private Bitmap bigPlane; 
	private int direction;
	private boolean isFire; 
	private List<Bullet> bullets; 
	private MyPlane myplane;
	private int interval; 

	public BigPlane(Resources resources) {
		super(resources);
		this.score = GameConstant.BIGPLANE_SCORE; 

		interval = 1;
		bullets = new ArrayList<Bullet>();

		GameObjectFactory factory = new GameObjectFactory();
		for (int i = 0; i < 3; i++) {
			BigPlaneBullet bullet = (BigPlaneBullet) factory
					.createBigPlaneBullet(resources);
			bullets.add(bullet);
		}
	}

	@Override
	public void initial(int arg0, float arg1, float arg2) {
		super.initial(arg0, arg1, arg2);
		
		speed = GameConstant.BIGPLANE_SPEED;
		bloodVolume = GameConstant.BIGPLANE_BLOOD;
		blood = bloodVolume;
		isFire = false;
		isAlive = true;

		Random ran = new Random();
		object_x = ran.nextInt((int) (screen_width - object_width));
		object_y = -object_height;

		currentCount++;
		if (currentCount >= sumCount) {
			currentCount = 0;
		}
	}

	@Override
	public void initBitmap() {
		bigPlane = BitmapFactory.decodeResource(resources, R.drawable.big);
		object_width = bigPlane.getWidth();
		object_height = bigPlane.getHeight() / 5;
	}

	@Override
	public void drawSelf(Canvas canvas) {
		if (isAlive) {

			if (!isExplosion) {
			
				if (speedTime > 3) {
					canvas.save();
					canvas.clipRect(object_x, object_y,
							object_x + object_width, object_y + object_height);
					canvas.drawBitmap(bigPlane, object_x, object_y
							- object_height, paint);
					canvas.restore();
				}

				else {
					canvas.save();
					canvas.clipRect(object_x, object_y,
							object_x + object_width, object_y + object_height);
					canvas.drawBitmap(bigPlane, object_x, object_y, paint);
					canvas.restore();
				}
				logic();
				shoot(canvas);
			} else {
				int y = (int) (currentFrame * object_height); // ��õ�ǰ֡�����λͼ��Y����
				canvas.save();
				canvas.clipRect(object_x, object_y, object_x + object_width,
						object_y + object_height);
				canvas.drawBitmap(bigPlane, object_x, object_y - y, paint);
				canvas.restore();
				currentFrame++;
				if (currentFrame >= 5) {
					currentFrame = 1;
					isExplosion = false;
					isAlive = false;
				}
			}
		}
	}

	
		@Override
		public void setScreenWH(float screen_width,float screen_height){
			super.setScreenWH(screen_width, screen_height);
			for(Bullet obj:bullets){	
				obj.setScreenWH(screen_width, screen_height);
			}
		}
	
	public void initBullet() {
		
		if (isFire) {
			if (interval == 1) {
				for (GameObject obj : bullets) {
					if (!obj.isAlive()) {
						obj.initial(0, object_x + object_width / 2,
								object_y + object_height*2/3);
						break;
					}
				}
			}

			interval++;
			if (interval >= 72/speedTime) {
				interval = 1;
			}
			
		}
		
	}

	public boolean shoot(Canvas canvas) {
		if (isFire && !myplane.getMissileState()) {
			for (Bullet obj : bullets) {
				if (obj.isAlive()) {
					obj.drawSelf(canvas);
					if (obj.isCollide(myplane) && !myplane.isInvincible()) {
						myplane.setAlive(false);
						return true;
					}
				}
			}
		} 
		return false;
	}

	public void setMyPlane(MyPlane myplane) {
		this.myplane = myplane;
	}

	@Override
	public void logic() {

		if (!isFire) {
			isFire = true;
		}

		if (object_y < screen_height) {

			if (speedTime < 4) {
				object_y += speed;
				object_x += Math.tan(object_y);
			} else {
				speed = 11;
				object_y += speed;
				object_x -= Math.tan(object_y);
			}

		} else {
			isAlive = false;
		}

		if (object_y + object_height > 0) {
			isVisible = true;
		} else {
			isVisible = false;
		}

	}

	@Override
	public void release() {
		if (!bigPlane.isRecycled()) {
			bigPlane.recycle();
		}

		for (Bullet obj : bullets) {
			obj.release();
		}
	}

}
