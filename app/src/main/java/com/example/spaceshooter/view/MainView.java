package com.example.spaceshooter.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.spaceshooter.constant.ConstantUtil;
import com.example.spaceshooter.constant.DebugConstant;
import com.example.spaceshooter.constant.GameConstant;
import com.example.spaceshooter.factory.GameObjectFactory;
import com.example.spaceshooter.myplane.R;
import com.example.spaceshooter.object.RedBulletGoods;
import com.example.spaceshooter.object.GameObject;
import com.example.spaceshooter.object.LifeGoods;
import com.example.spaceshooter.object.MissileGoods;
import com.example.spaceshooter.object.PurpleBulletGoods;
import com.example.spaceshooter.plane.BigPlane;
import com.example.spaceshooter.plane.BossPlane;
import com.example.spaceshooter.plane.EnemyPlane;
import com.example.spaceshooter.plane.MiddlePlane;
import com.example.spaceshooter.plane.MyPlane;
import com.example.spaceshooter.plane.SmallPlane;
import com.example.spaceshooter.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewConstructor")
public class MainView extends BaseView {
	private int missileCount;
	private int middlePlaneScore; 
	private int bigPlaneScore; 
	private int bossPlaneScore; 
	private int missileScore; 
	private int lifeScore; 
	private int bulletScore; 
	private int bulletScore2; 
	private int sumScore; 
	private static int speedTime; 
	private float bg_y; 
	private float bg_y2;
	private float play_bt_w;
	private float play_bt_h;
	private float missile_bt_y;
	private boolean isPlay; 
	private boolean isTouchPlane; 

	private Bitmap background; 
	private Bitmap background2; 
	private Bitmap playButton; 
	private Bitmap missile_bt; 
	private Bitmap life_amount;
	private Bitmap boom;
	private Bitmap plane_shield;

	private MyPlane myPlane; 
	private BossPlane bossPlane; 
	private List<EnemyPlane> enemyPlanes;
	private MissileGoods missileGoods;
	private LifeGoods lifeGoods; 
	private PurpleBulletGoods purpleBulletGoods;
	private RedBulletGoods redBulletGoods;

	private int mLifeAmount;
	private GameObjectFactory factory;
	private MediaPlayer mMediaPlayer; 

	private List<BigPlane> bigPlanes;
	
	private int bossAppearAgain_score;
	public MainView(Context context, GameSoundPool sounds) {
		super(context, sounds);
		isPlay = true;
		
		speedTime = GameConstant.GAMESPEED;
		mLifeAmount = GameConstant.LIFEAMOUNT;
		missileCount = GameConstant.MISSILECOUNT;

	
		mMediaPlayer = MediaPlayer.create(mainActivity, R.raw.game);
		mMediaPlayer.setLooping(true);
		if (!mMediaPlayer.isPlaying()) {
			mMediaPlayer.start();
		}

		factory = new GameObjectFactory(); 
		bigPlanes = new ArrayList<BigPlane>(); 
		enemyPlanes = new ArrayList<EnemyPlane>();
		myPlane = (MyPlane) factory.createMyPlane(getResources());
		myPlane.setMainView(this);

		for (int i = 0; i < SmallPlane.sumCount; i++) {
			SmallPlane smallPlane = (SmallPlane) factory
					.createSmallPlane(getResources());
			enemyPlanes.add(smallPlane);
		}
		for (int i = 0; i < MiddlePlane.sumCount; i++) {
			MiddlePlane middlePlane = (MiddlePlane) factory
					.createMiddlePlane(getResources());
			enemyPlanes.add(middlePlane);
		}
		for (int i = 0; i < BigPlane.sumCount; i++) {
			BigPlane bigPlane = (BigPlane) factory
					.createBigPlane(getResources());
			enemyPlanes.add(bigPlane);
			bigPlane.setMyPlane(myPlane);

			bigPlanes.add(bigPlane);
		}
		bossPlane = (BossPlane) factory.createBossPlane(getResources());
		bossPlane.setMyPlane(myPlane);
		enemyPlanes.add(bossPlane);
		missileGoods = (MissileGoods) factory
				.createMissileGoods(getResources());
		lifeGoods = (LifeGoods) factory.createLifeGoods(getResources());
		purpleBulletGoods = (PurpleBulletGoods) factory
				.createPurpleBulletGoods(getResources());
		redBulletGoods = (RedBulletGoods) factory
				.createRedBulletGoods(getResources());
		thread = new Thread(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		super.surfaceCreated(arg0);
		initBitmap(); 
		for (GameObject obj : enemyPlanes) {
			obj.setScreenWH(screen_width, screen_height);
		}
		missileGoods.setScreenWH(screen_width, screen_height);
		lifeGoods.setScreenWH(screen_width, screen_height);

		purpleBulletGoods.setScreenWH(screen_width, screen_height);
		redBulletGoods.setScreenWH(screen_width, screen_height);

		myPlane.setScreenWH(screen_width, screen_height);
		myPlane.setAlive(true);
		if (thread.isAlive()) {
			thread.start();
		} else {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		super.surfaceDestroyed(arg0);
		release();
		mMediaPlayer.stop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouchPlane = false;
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			if (x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h) {
				if (isPlay) {
					isPlay = false;
				} else {
					isPlay = true;
					synchronized (thread) {
						thread.notify();
					}
				}
				return true;
			}
			else if (x > myPlane.getObject_x()
					&& x < myPlane.getObject_x() + myPlane.getObject_width()
					&& y > myPlane.getObject_y()
					&& y < myPlane.getObject_y() + myPlane.getObject_height()) {
				if (isPlay) {
					isTouchPlane = true;
				}
				return true;
			}
			else if (x > 10 && x < 10 + missile_bt.getWidth()
					&& y > missile_bt_y
					&& y < missile_bt_y + missile_bt.getHeight()) {
				if (missileCount > 0) {
					missileCount--;
					myPlane.setMissileState(true);
					sounds.playSound(5, 0);

					for (EnemyPlane pobj : enemyPlanes) {
						if (pobj.isCanCollide()) {
							pobj.attacked(GameConstant.MISSILE_HARM); 
							if (pobj.isExplosion()) {
								addGameScore(pobj.getScore());
							}
						}
					}

					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(GameConstant.MISSILEBOOM_TIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} finally {
								myPlane.setMissileState(false);
							}

						}
					}).start();

				}
				return true;
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getPointerCount() == 1) {
			if (isTouchPlane) {
				float x = event.getX();
				float y = event.getY();
				if (x > myPlane.getMiddle_x() + 20) {
					if (myPlane.getMiddle_x() + myPlane.getSpeed() <= screen_width) {
						myPlane.setMiddle_x(myPlane.getMiddle_x()
								+ myPlane.getSpeed());
					}
				} else if (x < myPlane.getMiddle_x() - 20) {
					if (myPlane.getMiddle_x() - myPlane.getSpeed() >= 0) {
						myPlane.setMiddle_x(myPlane.getMiddle_x()
								- myPlane.getSpeed());
					}
				}
				if (y > myPlane.getMiddle_y() + 20) {
					if (myPlane.getMiddle_y() + myPlane.getSpeed() <= screen_height) {
						myPlane.setMiddle_y(myPlane.getMiddle_y()
								+ myPlane.getSpeed());
					}
				} else if (y < myPlane.getMiddle_y() - 20) {
					if (myPlane.getMiddle_y() - myPlane.getSpeed() >= 0) {
						myPlane.setMiddle_y(myPlane.getMiddle_y()
								- myPlane.getSpeed());
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void initBitmap() {
		playButton = BitmapFactory.decodeResource(getResources(),
				R.drawable.play);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_01);
		background2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_02);
		missile_bt = BitmapFactory.decodeResource(getResources(),
				R.drawable.missile_bt);

		life_amount = BitmapFactory.decodeResource(getResources(),
				R.drawable.life_amount);

		boom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
		plane_shield = BitmapFactory.decodeResource(getResources(), R.drawable.plane_shield);

		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		play_bt_w = playButton.getWidth();
		play_bt_h = playButton.getHeight() / 2;
		bg_y = 0;
		bg_y2 = bg_y - screen_height;
		missile_bt_y = screen_height - 10 - missile_bt.getHeight();

	}

	public void initObject() {
		for (EnemyPlane obj : enemyPlanes) {
			if (obj instanceof SmallPlane) {
				if (!obj.isAlive()) {
					obj.initial(speedTime, 0, 0);
					break;
				}
			}
			else if (obj instanceof MiddlePlane) {
				if (middlePlaneScore >= GameConstant.MIDDLEPLANE_APPEARSCORE) {
					if (!obj.isAlive()) {
						obj.initial(speedTime, 0, 0);
						break;
					}
				}
			}
			else if (obj instanceof BigPlane) {
				if (bigPlaneScore >= GameConstant.BIGPLANE_APPEARSCORE) {
					if (!obj.isAlive()) {
					obj.initial(speedTime, 0, 0);
						break;
					}
				}
			}
			else {
				if (bossPlaneScore >= GameConstant.BOSSPLANE_APPEARSCORE) {
					if (!obj.isAlive()) {
						obj.initial(speedTime, 0, 0);
						bossPlaneScore = 0;
						break;
					}
				}
			}
		}

		if (missileScore >= GameConstant.MISSILE_APPEARSCORE) {
			if (!missileGoods.isAlive()) {
				missileScore = 0;
				if (DebugConstant.MISSILEGOODS_APPEAR) {
					missileGoods.initial(0, 0, 0);
				}
			}
		}

		if (lifeScore >= GameConstant.LIFE_APPEARSCORE) {
			if (!lifeGoods.isAlive()) {
				lifeScore = 0;
				if (DebugConstant.LIFEGOODS_APPEAR) {
					lifeGoods.initial(0, 0, 0);
				}
			}
		}
		if (bulletScore >= GameConstant.BULLET1_APPEARSCORE) {
			if (!purpleBulletGoods.isAlive()) {
				bulletScore = 0;
				if (DebugConstant.BULLETGOODS1_APPEAR) {
					purpleBulletGoods.initial(0, 0, 0);
				}
			}
		}
		if (bulletScore2 >= GameConstant.BULLET2_APPEARSCORE) {
			if (!redBulletGoods.isAlive()) {
				bulletScore2 = 0;
				if (DebugConstant.BULLETGOODS2_APPEAR) {
					redBulletGoods.initial(0, 0, 0);
				}
			}
		}

		if (bossPlane.isAlive()) {
			if (!myPlane.getMissileState()) {
				bossPlane.initBullet();
			}
		}

		for (BigPlane big_plane : bigPlanes) {
			if (big_plane.isAlive()) {
				if (!myPlane.getMissileState()) {
					big_plane.initBullet();
				}
			}
		}

		myPlane.isBulletOverTime();
		myPlane.initBullet();
		if (sumScore >= speedTime * GameConstant.LEVELUP_SCORE && speedTime < GameConstant.MAXGRADE) {
			speedTime++;
		}
	}

	@Override
	public void release() {
		for (GameObject obj : enemyPlanes) {
			obj.release();
		}

		myPlane.release();
		missileGoods.release();
		lifeGoods.release();
		purpleBulletGoods.release();
		redBulletGoods.release();

		if (!playButton.isRecycled()) {
			playButton.recycle();
		}
		if (!background.isRecycled()) {
			background.recycle();
		}
		if (!background2.isRecycled()) {
			background2.recycle();
		}
		if (!missile_bt.isRecycled()) {
			missile_bt.recycle();
		}
		if (!life_amount.isRecycled()) {
			life_amount.recycle();
		}
		if (!boom.isRecycled()) {
			boom.recycle();
		}
		if (!plane_shield.isRecycled()) {
			plane_shield.recycle();
		}
	}

	@Override
	public void drawSelf() {
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); 
			canvas.save();
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, 0, bg_y, paint); 
			canvas.drawBitmap(background2, 0, bg_y2, paint); 
			canvas.restore();
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w, 10 + play_bt_h);
			if (isPlay) {
				canvas.drawBitmap(playButton, 10, 10, paint);
			} else {
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();

			paint.setTextSize(40);
			paint.setColor(Color.rgb(235, 161, 1));
			canvas.drawText("Score:" + String.valueOf(sumScore), 30 + play_bt_w,
					50, paint);
			canvas.drawText("Level X " + String.valueOf(speedTime),
					screen_width - 160, 50, paint);
			if (mLifeAmount > 0) {
				paint.setColor(Color.BLACK);
				canvas.drawBitmap(life_amount, screen_width - 150,
						screen_height - life_amount.getHeight() - 10, paint);
				canvas.drawText("X " + String.valueOf(mLifeAmount),
						screen_width - life_amount.getWidth(),
						screen_height - 25, paint);
			}

			if (myPlane.getMissileState()) {
				float boom_x = myPlane.getMiddle_x() - boom.getWidth() / 2;
				float boom_y = myPlane.getMiddle_y() - boom.getHeight() / 2;

				canvas.drawBitmap(boom, boom_x, boom_y, paint);

			}

			if (myPlane.isInvincible() && !myPlane.getDamaged()) {
				float plane_shield_x = myPlane.getMiddle_x() - plane_shield.getWidth() / 2;
				float plane_shield_y = myPlane.getMiddle_y() - plane_shield.getHeight() / 2;

				canvas.drawBitmap(plane_shield, plane_shield_x, plane_shield_y, paint);

			}

			if (missileCount > 0) {
				paint.setTextSize(40);
				paint.setColor(Color.BLACK);
				canvas.drawBitmap(missile_bt, 10, missile_bt_y, paint);
				canvas.drawText("X " + String.valueOf(missileCount),
						10 + missile_bt.getWidth(), screen_height - 25, paint);// 绘制文字
			}

			if (missileGoods.isAlive()) {
				if (missileGoods.isCollide(myPlane)) {
					if (missileCount < GameConstant.MISSILE_MAXCOUNT) {
						missileCount++;
					}
					missileGoods.setAlive(false);
					sounds.playSound(6, 0);
				} else
					missileGoods.drawSelf(canvas);
			}
			if (lifeGoods.isAlive()) {
				if (lifeGoods.isCollide(myPlane)) {
					if (mLifeAmount < GameConstant.LIFE_MAXCOUNT) {
						mLifeAmount++;
					}
					lifeGoods.setAlive(false);
					sounds.playSound(6, 0);
				} else
					lifeGoods.drawSelf(canvas);
			}
			if (purpleBulletGoods.isAlive()) {
				if (purpleBulletGoods.isCollide(myPlane)) {
					purpleBulletGoods.setAlive(false);
					sounds.playSound(6, 0);

					myPlane.setChangeBullet(true);
					myPlane.changeBullet(ConstantUtil.MYBULLET1);
					myPlane.setStartTime(System.currentTimeMillis());

				} else
					purpleBulletGoods.drawSelf(canvas);
			}
			if (redBulletGoods.isAlive()) {
				if (redBulletGoods.isCollide(myPlane)) {
					redBulletGoods.setAlive(false);
					sounds.playSound(6, 0);

					myPlane.setChangeBullet(true);
					myPlane.changeBullet(ConstantUtil.MYBULLET2);
					myPlane.setStartTime(System.currentTimeMillis());

				} else
					redBulletGoods.drawSelf(canvas);
			}

			for (EnemyPlane obj : enemyPlanes) {
				if (obj.isAlive()) {
					obj.drawSelf(canvas);
					if (obj.isCanCollide() && myPlane.isAlive()) {
						if (obj.isCollide(myPlane) && !myPlane.isInvincible()
								&& !myPlane.getMissileState()) {
							myPlane.setAlive(false);
						}
					}
				}
			}
			if (!myPlane.isAlive()) {
				sounds.playSound(4, 0); 

				if (mLifeAmount > 0) {
					mLifeAmount--;
					myPlane.setAlive(true);
					new Thread(new Runnable() {

						@Override
						public void run() {
							myPlane.setDamaged(true);
							myPlane.setInvincibleTime(GameConstant.BOOM_TIME);
							myPlane.setDamaged(false);
							myPlane.setInvincibleTime(GameConstant.INVINCIBLE_TIME);
						}
					}).start();

				} else {
					if (DebugConstant.ETERNAL) {
						threadFlag = true;
						myPlane.setAlive(true);

						new Thread(new Runnable() {

							@Override
							public void run() {
								myPlane.setDamaged(true);
								myPlane.setInvincibleTime(GameConstant.BOOM_TIME);
								myPlane.setDamaged(false);
								myPlane.setInvincibleTime(GameConstant.INVINCIBLE_TIME);
							}
						}).start();

					} else {
						threadFlag = false;
						if (mMediaPlayer.isPlaying()) {
							mMediaPlayer.stop();
						}
					}

				}

			}
			myPlane.drawSelf(canvas); 
			myPlane.shoot(canvas, enemyPlanes);
			sounds.playSound(1, 0); 

		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	public void viewLogic() {
		if (bg_y > bg_y2) {
			bg_y += 10;
			bg_y2 = bg_y - background.getHeight();
		} else {
			bg_y2 += 10;
			bg_y = bg_y2 - background.getHeight();
		}
		if (bg_y >= background.getHeight()) {
			bg_y = bg_y2 - background.getHeight();
		} else if (bg_y2 >= background.getHeight()) {
			bg_y2 = bg_y - background.getHeight();
		}
	}

	public void addGameScore(int score) {
		middlePlaneScore += score; 
		bigPlaneScore += score; 
		bossPlaneScore += score; 
		missileScore += score; 
		lifeScore += score;
		bulletScore += score; 
		bulletScore2 += score; 
		sumScore += score; 
		
	}

	public void playSound(int key) {
		sounds.playSound(key, 0);
	}

	@Override
	public void run() {
		while (threadFlag) {
			long startTime = System.currentTimeMillis();
			initObject();
			drawSelf();
			viewLogic(); 
			long endTime = System.currentTimeMillis();

			if (!isPlay) {
				mMediaPlayer.pause();

				synchronized (thread) {
					try {
						thread.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (!mMediaPlayer.isPlaying()) {
					mMediaPlayer.start();
				}
			}

			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message message = new Message();
		message.what = ConstantUtil.TO_END_VIEW;
		message.arg1 = Integer.valueOf(sumScore);
		mainActivity.getHandler().sendMessage(message);
	}
}
