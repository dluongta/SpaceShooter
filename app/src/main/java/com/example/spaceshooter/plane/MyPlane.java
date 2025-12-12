package com.example.spaceshooter.plane;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;

import com.example.spaceshooter.bullet.Bullet;
import com.example.spaceshooter.bullet.MyBlueBullet;
import com.example.spaceshooter.bullet.MyPurpleBullet;
import com.example.spaceshooter.bullet.MyRedBullet;
import com.example.spaceshooter.constant.ConstantUtil;
import com.example.spaceshooter.constant.DebugConstant;
import com.example.spaceshooter.constant.GameConstant;
import com.example.spaceshooter.factory.GameObjectFactory;
import com.example.spaceshooter.interfaces.IMyPlane;
import com.example.spaceshooter.myplane.R;
import com.example.spaceshooter.object.GameObject;
import com.example.spaceshooter.view.MainView;

import java.util.ArrayList;
import java.util.List;


public class MyPlane extends GameObject implements IMyPlane {
    private static final boolean Random = false;
    private float middle_x;
    private float middle_y;
    private long startTime; 
    private long endTime; 
    private boolean isChangeBullet; 
    private Bitmap mPlane;
    private Bitmap mPlaneExplosion;
    private List<Bullet> bullets; 
    private MainView mainView;
    private GameObjectFactory factory;
    private boolean isInvincible; 
    private boolean isDamaged; 
    private int bulletType;
    private boolean isMissileBoom;

    public MyPlane(Resources resources) {
        super(resources);
        initBitmap();
        this.speed = GameConstant.MYPLANE_SPEED;
        isInvincible = false;
        isChangeBullet = false;
        isDamaged = false;
        isMissileBoom = false;

        factory = new GameObjectFactory();
        bullets = new ArrayList<Bullet>();
        changeBullet(ConstantUtil.MYBULLET);
        bulletType = ConstantUtil.MYBULLET;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);
        object_x = screen_width / 2 - object_width / 2;
        object_y = screen_height - object_height;
        middle_x = object_x + object_width / 2;
        middle_y = object_y + object_height / 2;
    }

    @Override
    public void initBitmap() {
        mPlane = BitmapFactory.decodeResource(resources, R.drawable.myplane);
        mPlaneExplosion = BitmapFactory.decodeResource(resources,
                R.drawable.myplaneexplosion);

        object_width = mPlane.getWidth() / 3;
        object_height = mPlane.getHeight();
    }

    @Override
    public void drawSelf(Canvas canvas) {
        if (isDamaged) {
            drawExplosion(canvas);
        } else {
            drawPlane(canvas);
        }
    }


    private void drawPlane(Canvas canvas) {
        int x = (int) (currentFrame * object_width);
        canvas.save();
        canvas.clipRect(object_x, object_y, object_x + object_width,
                object_y + object_height);
        canvas.drawBitmap(mPlane, object_x - x, object_y, paint);
        canvas.restore();

        if (isInvincible) {
            currentFrame++;
            if (currentFrame >= 3) {
                currentFrame = 0;
            }
        } else if (isAlive) {
            if (bulletType == ConstantUtil.MYBULLET) {
                currentFrame = 0;
            } else if (bulletType == ConstantUtil.MYBULLET1) {
                currentFrame = 1;
            } else if (bulletType == ConstantUtil.MYBULLET2) {
                currentFrame = 2;
            }
        }
    }



    private void drawExplosion(Canvas canvas) {
        int x = (int) (currentFrame * object_width);
        canvas.save();
        canvas.clipRect(object_x, object_y, object_x + object_width,
                object_y + object_height);
        canvas.drawBitmap(mPlaneExplosion, object_x - x, object_y, paint);
        canvas.restore();

        if (bulletType == ConstantUtil.MYBULLET) {
            currentFrame++;
            if (currentFrame >= 2) {
                currentFrame = 0;
            }
        } else if (bulletType == ConstantUtil.MYBULLET1) {
            currentFrame++;
            if (currentFrame >= 4) {
                currentFrame = 2;
            }
        } else if (bulletType == ConstantUtil.MYBULLET2) {
            currentFrame++;
            if (currentFrame >= 6) {
                currentFrame = 4;
            }
        }
    }

    @Override
    public void release() {
        for (Bullet obj : bullets) {
            obj.release();
        }
        if (!mPlane.isRecycled()) {
            mPlane.recycle();
        }
        if (!mPlaneExplosion.isRecycled()) {
            mPlaneExplosion.recycle();
        }

    }


    @Override
    public void shoot(Canvas canvas, List<EnemyPlane> planes) {
        for (Bullet bullet : bullets) {
            if (bullet.isAlive()) {
                bullet.drawSelf(canvas);
                checkAttacked(planes, bullet);
            }
        }
    }


    private void checkAttacked(List<EnemyPlane> planes, Bullet bullet) {
        for (EnemyPlane enemyPlane : planes) {
            boolean isCollide = enemyPlane.isCanCollide() && bullet.isCollide(enemyPlane);
            if (isCollide) {
                attackedEnemyPlane(bullet, enemyPlane);
                break;
            }
        }
    }


    private void attackedEnemyPlane(Bullet bullet, EnemyPlane plane) {
        plane.attacked(bullet.getHarm());
        if (plane.isExplosion()) {
            mainView.addGameScore(plane.getScore());
            if (plane instanceof SmallPlane) {
                mainView.playSound(2);
            } else if (plane instanceof MiddlePlane) {
                mainView.playSound(3);
            } else if (plane instanceof BigPlane) {
                mainView.playSound(4);
            } else {
                mainView.playSound(5);
            }
        }
    }


    @Override
    public void initBullet() {
        for (Bullet obj : bullets) {
            if (!obj.isAlive()) {
                obj.initial(0, middle_x, middle_y);
                break;
            }
        }
    }


    @Override
    public void changeBullet(int type) {
        bulletType = type;
        bullets.clear();
        if (isChangeBullet) {
            if (type == ConstantUtil.MYBULLET1) {
                for (int i = 0; i < 6; i++) {
                    MyPurpleBullet bullet1 = (MyPurpleBullet) factory
                            .createMyPurpleBullet(resources);
                    bullets.add(bullet1);
                }
            } else if (type == ConstantUtil.MYBULLET2) {
                for (int i = 0; i < 4; i++) {
                    MyRedBullet bullet2 = (MyRedBullet) factory
                            .createMyRedBullet(resources);
                    bullets.add(bullet2);
                }
            }

        } else {
            for (int i = 0; i < 4; i++) {
                MyBlueBullet bullet = (MyBlueBullet) factory.createMyBlueBullet(resources);
                bullets.add(bullet);
            }
        }
    }


    public void isBulletOverTime() {
        if (isChangeBullet) {
            endTime = System.currentTimeMillis();
            if (endTime - startTime > GameConstant.MYSPECIALBULLET_DURATION) {
                isChangeBullet = false;
                startTime = 0;
                endTime = 0;
                changeBullet(ConstantUtil.MYBULLET);
            }
        }
    }


    public void setInvincibleTime(long time) {
        if (DebugConstant.INVINCIBLE) {
            isInvincible = true;
            SystemClock.sleep(time);
            isInvincible = false;
        }
    }


    public boolean isInvincible() {
        return isInvincible;
    }


    public void setMissileState(boolean isBoom) {
        isMissileBoom = isBoom;
    }


    public boolean getMissileState() {
        return isMissileBoom;
    }


    public void setDamaged(boolean arg) {
        isDamaged = arg;
    }


    public boolean getDamaged() {
        return isDamaged;

    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean isChangeBullet() {
        return isChangeBullet;
    }

    @Override
    public void setChangeBullet(boolean isChangeBullet) {
        this.isChangeBullet = isChangeBullet;
    }

    @Override
    public float getMiddle_x() {
        return middle_x;
    }

    @Override
    public void setMiddle_x(float middle_x) {
        this.middle_x = middle_x;
        this.object_x = middle_x - object_width / 2;
    }

    @Override
    public float getMiddle_y() {
        return middle_y;
    }

    @Override
    public void setMiddle_y(float middle_y) {
        this.middle_y = middle_y;
        this.object_y = middle_y - object_height / 2;
    }
}
