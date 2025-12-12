package com.example.spaceshooter.plane;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.spaceshooter.bullet.BossFlameBullet;
import com.example.spaceshooter.bullet.BossSunBullet;
import com.example.spaceshooter.bullet.BossTriangleBullet;
import com.example.spaceshooter.bullet.BossGThunderBullet;
import com.example.spaceshooter.bullet.BossYHellfireBullet;
import com.example.spaceshooter.bullet.BossRHellfireBullet;
import com.example.spaceshooter.bullet.BossDefaultBullet;
import com.example.spaceshooter.bullet.Bullet;
import com.example.spaceshooter.constant.ConstantUtil;
import com.example.spaceshooter.constant.GameConstant;
import com.example.spaceshooter.factory.GameObjectFactory;
import com.example.spaceshooter.myplane.R;
import com.example.spaceshooter.object.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BossPlane extends EnemyPlane {
    private static int currentCount = 0; 
    private static int sumCount = GameConstant.BOSSPLANE_COUNT;
    private Bitmap boosPlane;
    private Bitmap boosPlaneBomb;
    private Bitmap bossPlane_crazy;
    private int direction; 
    private int interval; 
    private float leftBorder; 
    private float rightBorder; 
    private float upBorder; 
    private float downBorder;
    private boolean isFire; 
    private boolean isAnger;
    private boolean isCrazy; 
    private boolean isLimit;
    private List<Bullet> bullets; 
    private MyPlane myplane;

    private int bulletType;

    private GameObjectFactory factory;

    private static final int STATE_NORMAL = 0; 
    private static final int STATE_ANGER = 1; 
    private static final int STATE_CRAZY = 2; 
    private static final int STATE_LIMIT = 3; 


    private long bossappear_interval;

    public BossPlane(Resources resources) {
        super(resources);

        this.score = GameConstant.BOSSPLANE_SCORE;
        interval = 1;

        bullets = new ArrayList<Bullet>();
        factory = new GameObjectFactory();

        // bulletType = ConstantUtil.BOSSBULLET_DEFAULT;
        changeBullet(bulletType);

    }

    public void setMyPlane(MyPlane myplane) {
        this.myplane = myplane;
    }

    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);

        for (Bullet obj : bullets) {
            obj.setScreenWH(screen_width, screen_height);
        }

        leftBorder = -object_width / 2;
        rightBorder = screen_width - object_width / 2;
        upBorder = 0;
        downBorder = screen_height * 2 / 3;
    }


    @Override
    public void initial(int arg0, float arg1, float arg2) {
        super.initial(arg0, arg1, arg2);

        isAlive = true;
        isVisible = true;
        isAnger = false;
        isCrazy = false;
        isLimit = false;
        isFire = false;

        speed = 15;
        bloodVolume = GameConstant.BOSSPLANE_BLOOD;
        blood = bloodVolume;
        direction = ConstantUtil.DIR_RIGHT;

        Random ran = new Random();
        object_x = ran.nextInt((int) (screen_width - object_width));
        object_y = -object_height * (arg0 * 2 + 1);

        currentCount++;
        if (currentCount >= sumCount) {
            currentCount = 0;
        }

    }


    @Override
    public void initBitmap() {
        boosPlane = BitmapFactory.decodeResource(resources,
                R.drawable.boosplane);
        boosPlaneBomb = BitmapFactory.decodeResource(resources,
                R.drawable.bossplane_bomb);
        bossPlane_crazy = BitmapFactory.decodeResource(resources,
                R.drawable.bossplane_crazy);
        object_width = boosPlane.getWidth(); 
        object_height = boosPlane.getHeight() / 2; 
    }


    public void initBullet() {
        if (!isFire) return;

        if (interval == 1) {
            for (GameObject obj : bullets) {
                if (!obj.isAlive()) {
                    obj.initial(0, object_x + object_width / 2,
                            object_y + object_height);
                    break;
                }
            }
        }

        interval++;
        if (bulletType == ConstantUtil.BOSSBULLET_DEFAULT) {
            if (interval >= 2) {
                interval = 1;
            }
        } else {
            if (interval >= 30 / speedTime + 5) {
                interval = 1;
            }
        }

    }



    @Override
    public void drawSelf(Canvas canvas) {
        if (!isAlive) return;

        if (isExplosion) {
            drawExplosion(canvas);
        } else {
            drawBoss(canvas);
        }
    }


    private void drawExplosion(Canvas canvas) {
        int y = (int) (currentFrame * object_height);
        canvas.save();
        canvas.clipRect(object_x, object_y, object_x + object_width,
                object_y + object_height);
        canvas.drawBitmap(boosPlaneBomb, object_x, object_y - y, paint);
        canvas.restore();

        currentFrame++;
        if (currentFrame >= 5) {
            currentFrame = 0;
            isExplosion = false;
            isAlive = false;
            if (bulletType != ConstantUtil.BOSSBULLET_DEFAULT) {
                changeBullet(ConstantUtil.BOSSBULLET_DEFAULT);
            }
        }
    }


    private void drawBoss(Canvas canvas) {
        if (isLimit) {
            int y = (int) (currentFrame * object_height);
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(bossPlane_crazy, object_x, object_y - y,
                    paint);
            canvas.restore();
            currentFrame++;
            if (currentFrame >= 2) {
                currentFrame = 0;
            }

        }

        else if (isCrazy) {
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(bossPlane_crazy, object_x, object_y
                    - object_height, paint);
            canvas.restore();
        }

        else if (isAnger) {
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(boosPlane, object_x, object_y
                    - object_height, paint);
            canvas.restore();

        }

        else {
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(boosPlane, object_x, object_y, paint);
            canvas.restore();
        }

        logic();
        shoot(canvas); 
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


    @Override
    public void release() {
        for (Bullet obj : bullets) {
            obj.release();
        }
        if (!boosPlane.isRecycled()) {
            boosPlane.recycle();
        }
        if (!boosPlaneBomb.isRecycled()) {
            boosPlaneBomb.recycle();
        }
        if (!bossPlane_crazy.isRecycled()) {
            bossPlane_crazy.recycle();
        }
    }

    @Override
    public boolean isCollide(GameObject obj) {
        return super.isCollide(obj);
    }


    @Override
    public void logic() {

        if (object_y < 0) {
            object_y += speed;
        } else {

            if (!isFire) {
                isFire = true;
            }

            if (blood <= GameConstant.BOSSPLANE_ANGER_BLOOD
                    && blood > GameConstant.BOSSPLANE_CRAZY_BLOOD) {
                if (!isAnger) {
                    isAnger = true;
                    if (bulletType != ConstantUtil.BOSSBULLET_ANGER) {
                        changeBullet(ConstantUtil.BOSSBULLET_ANGER);
                    }
                }
            }

            if (blood <= GameConstant.BOSSPLANE_CRAZY_BLOOD
                    && blood > GameConstant.BOSSPLANE_LIMIT_BLOOD) {
                if (isAnger) {
                    isAnger = false;
                }

                if (!isCrazy) {
                    isCrazy = true;
                    speed = 20 + 3 * speedTime;
                    if (bulletType != ConstantUtil.BOSSBULLET_CRAZY) {
                        changeBullet(ConstantUtil.BOSSBULLET_CRAZY);
                    }
                }
            }

            if (blood <= GameConstant.BOSSPLANE_LIMIT_BLOOD) {
                if (isAnger) {
                    isAnger = false;
                }

                if (isCrazy) {
                    isCrazy = false;
                }

                if (!isLimit) {
                    isLimit = true;
                    speed = 30 + 5 * speedTime;
                    if (bulletType != ConstantUtil.BOSSBULLET_LIMIT) {
                        changeBullet(ConstantUtil.BOSSBULLET_LIMIT);
                    }
                }

            }

            moveLogic();

        }
    }

    public void moveLogic() {
        if (isCrazy || isLimit) {
            if (direction == ConstantUtil.DIR_RIGHT) {
                direction = ConstantUtil.DIR_LEFT;
            }
            if (object_x < rightBorder && object_y < downBorder
                    && direction == ConstantUtil.DIR_RIGHT_DOWN) {
                object_x += speed;
                object_y += speed;
                if (object_x >= rightBorder || object_y >= downBorder) {
                    direction = ConstantUtil.DIR_LEFT;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_LEFT) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT_UP;
                }
            }
            if (object_x < rightBorder && object_y > upBorder
                    && direction == ConstantUtil.DIR_RIGHT_UP) {
                object_x += speed;
                object_y -= speed;
                if (object_x >= rightBorder || object_y <= upBorder) {
                    direction = ConstantUtil.DIR_TEMP;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_TEMP) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT_DOWN;
                }
            }
        } else if (isAnger) {

            if (object_y < downBorder) {
                object_y += speed;
                if (object_y >= downBorder) {
                    direction = ConstantUtil.DIR_RIGHT;
                }
            }

            if (object_x < rightBorder && direction == ConstantUtil.DIR_RIGHT) {
                object_x += speed;
                if (object_x >= rightBorder) {
                    direction = ConstantUtil.DIR_LEFT;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_LEFT) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT;
                }
            }
        } else {
            if (object_x < rightBorder && direction == ConstantUtil.DIR_RIGHT) {
                object_x += speed;
                if (object_x >= rightBorder) {
                    direction = ConstantUtil.DIR_LEFT;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_LEFT) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT;
                }
            }

        }
    }


    public void changeBullet(int type) {
        bulletType = type;

        bullets.clear();


        if (bulletType == ConstantUtil.BOSSBULLET_DEFAULT) { // 普通状态
            normalShooting();
        } else if (bulletType == ConstantUtil.BOSSBULLET_ANGER) { // 愤怒状态
            angerShooting();
        } else if (bulletType == ConstantUtil.BOSSBULLET_CRAZY) { // 疯狂状态
            crazyShooting();
        } else if (bulletType == ConstantUtil.BOSSBULLET_LIMIT) { // 极限状态
            limitShooting();
        } else {
            for (int i = 0; i < 5; i++) {
                BossFlameBullet bullet = (BossFlameBullet) factory
                        .createBossFlameBullet(resources);
                bullets.add(bullet);

            }
        }

    }


    private void limitShooting() {
        int clip = speedTime + 5;
        for (int i = 0; i < clip; i++) {
            if (speedTime == 1) {
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 2) {
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);

                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 3) {
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 4) {
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);

                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else {

                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);

                BossDefaultBullet bullet_default = (BossDefaultBullet) factory
                        .createBossBulletDefault(resources);
                bullets.add(bullet_default);

            }

        }
    }


    private void crazyShooting() {
        int clip = speedTime + 4;
        for (int i = 0; i < clip; i++) {
            if (speedTime == 1) {
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);
            } else if (speedTime == 2) {
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 3) {
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else {
                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);

                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);

                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);

            }

        }
    }


    private void angerShooting() {
        for (int i = 0; i < 8; i++) {
            if (speedTime <= 2) {
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                BossTriangleBullet bullet2 = (BossTriangleBullet) factory
                        .createBossTriangleBullet(resources);
                bullets.add(bullet2);
            } else if (speedTime <= 4) {
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);
            } else {
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                BossTriangleBullet bullet2 = (BossTriangleBullet) factory
                        .createBossTriangleBullet(resources);
                bullets.add(bullet2);

                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);
            }

        }
    }

    private void normalShooting() {
        for (int i = 0; i < 100; i++) {
            BossDefaultBullet bullet_default = (BossDefaultBullet) factory
                    .createBossBulletDefault(resources);
            bullets.add(bullet_default);

            if (speedTime >= 3) {
                if (speedTime == 3) {
                    BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                            .createBossYHellfireBullet(resources);
                    bullets.add(bullet4);
                } else if (speedTime == 4) {
                    BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                            .createBossRHellfireBullet(resources);
                    bullets.add(bullet5);
                } else {
                    BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                            .createBossYHellfireBullet(resources);
                    bullets.add(bullet4);

                    BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                            .createBossRHellfireBullet(resources);
                    bullets.add(bullet5);
                }
            }

        }
    }

}
