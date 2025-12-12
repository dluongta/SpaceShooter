package com.example.spaceshooter.bullet;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.spaceshooter.constant.GameConstant;
import com.example.spaceshooter.myplane.R;
import com.example.spaceshooter.object.GameObject;


public class MyRedBullet extends Bullet {
	private Bitmap bullet; 		 
	private boolean attack;		
	
	public MyRedBullet(Resources resources) {
		super(resources);
		this.harm = GameConstant.MYBULLET2_HARM;
	}
	@Override
	public void initial(int arg0,float arg1,float arg2){
		isAlive = true;

		speed = GameConstant.MYBULLET2_SPEED;	
		object_x = arg1 - object_width / 2;
		object_y = arg2 - object_height;

	}
	@Override
	public void initBitmap() {
		bullet = BitmapFactory.decodeResource(resources, R.drawable.my_bullet_red);
		object_width = bullet.getWidth();
		object_height = bullet.getHeight();
	}
	@Override
	public void drawSelf(Canvas canvas) {
		if (isAlive) {
			canvas.save();
			canvas.clipRect(object_x, object_y, object_x + object_width,object_y + object_height);
			canvas.drawBitmap(bullet, object_x, object_y, paint);
			canvas.restore();
		}

		logic();
	}
	@Override
	public void release() {
		if(!bullet.isRecycled()){
			bullet.recycle();
		}
	}
	@Override
	public void logic() {
		if (object_y >= 0) {
			object_y -= speed;
			object_x += 100*(Math.sin(System.currentTimeMillis()));
		}
		else {
			isAlive = false;
		}

	}
	@Override
	public boolean isCollide(GameObject obj) {
		return super.isCollide(obj);
	}
	
	@Override
	public boolean isAlive() {
		return isAlive;
	}
	
}

