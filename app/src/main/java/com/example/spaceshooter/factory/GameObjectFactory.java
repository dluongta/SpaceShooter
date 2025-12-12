package com.example.spaceshooter.factory;

import android.content.res.Resources;

import com.example.spaceshooter.plane.BigPlane;
import com.example.spaceshooter.bullet.BigPlaneBullet;
import com.example.spaceshooter.bullet.BossFlameBullet;
import com.example.spaceshooter.bullet.BossSunBullet;
import com.example.spaceshooter.bullet.BossTriangleBullet;
import com.example.spaceshooter.bullet.BossGThunderBullet;
import com.example.spaceshooter.bullet.BossYHellfireBullet;
import com.example.spaceshooter.bullet.BossRHellfireBullet;
import com.example.spaceshooter.bullet.BossDefaultBullet;
import com.example.spaceshooter.plane.BossPlane;
import com.example.spaceshooter.object.PurpleBulletGoods;
import com.example.spaceshooter.object.RedBulletGoods;
import com.example.spaceshooter.object.GameObject;
import com.example.spaceshooter.object.LifeGoods;
import com.example.spaceshooter.plane.MiddlePlane;
import com.example.spaceshooter.object.MissileGoods;
import com.example.spaceshooter.bullet.MyBlueBullet;
import com.example.spaceshooter.bullet.MyPurpleBullet;
import com.example.spaceshooter.bullet.MyRedBullet;
import com.example.spaceshooter.plane.MyPlane;
import com.example.spaceshooter.plane.SmallPlane;



public class GameObjectFactory {

    public GameObject createSmallPlane(Resources resources) {
        return new SmallPlane(resources);
    }

    public GameObject createMiddlePlane(Resources resources) {
        return new MiddlePlane(resources);
    }


    public GameObject createBigPlane(Resources resources) {
        return new BigPlane(resources);
    }


    public GameObject createBossPlane(Resources resources) {
        return new BossPlane(resources);
    }


    public GameObject createMyPlane(Resources resources) {
        return new MyPlane(resources);
    }


    public GameObject createMyBlueBullet(Resources resources) {
        return new MyBlueBullet(resources);
    }
    public GameObject createMyPurpleBullet(Resources resources) {
        return new MyPurpleBullet(resources);
    }
    public GameObject createMyRedBullet(Resources resources) {
        return new MyRedBullet(resources);
    }


    public GameObject createBossFlameBullet(Resources resources) {
        return new BossFlameBullet(resources);
    }

    public GameObject createBossSunBullet(Resources resources) {
        return new BossSunBullet(resources);
    }

    public GameObject createBossTriangleBullet(Resources resources) {
        return new BossTriangleBullet(resources);
    }

    public GameObject createBossGThunderBullet(Resources resources) {
        return new BossGThunderBullet(resources);
    }

    public GameObject createBossYHellfireBullet(Resources resources) {
        return new BossYHellfireBullet(resources);
    }

    public GameObject createBossRHellfireBullet(Resources resources) {
        return new BossRHellfireBullet(resources);
    }

    public GameObject createBossBulletDefault(Resources resources) {
        return new BossDefaultBullet(resources);
    }


    public GameObject createBigPlaneBullet(Resources resources) {
        return new BigPlaneBullet(resources);
    }


    public GameObject createMissileGoods(Resources resources) {
        return new MissileGoods(resources);
    }


    public GameObject createLifeGoods(Resources resources) {
        return new LifeGoods(resources);
    }



    public GameObject createPurpleBulletGoods(Resources resources) {
        return new PurpleBulletGoods(resources);
    }

    public GameObject createRedBulletGoods(Resources resources) {
        return new RedBulletGoods(resources);
    }
}
