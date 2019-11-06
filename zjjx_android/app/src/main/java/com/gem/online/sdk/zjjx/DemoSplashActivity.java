package com.gem.online.sdk.zjjx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.gemstone.sdk.online.GemSplashActivity;


/**
 * Created by DIY on 2016/8/23.
 */
public class DemoSplashActivity extends GemSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void splashEnd() {
//        setContentView(R.layout.activity_splash);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent mainIntent =new Intent(DemoSplashActivity.this,MainActivity.class);
//                DemoSplashActivity.this.startActivity(mainIntent);
//                DemoSplashActivity.this.finish();
//            }
//        },3000);
//        Thread myThread=new Thread(){//创建子线程
//            @Override
//            public void run() {
//                try{
//                    sleep(5000);//使程序休眠五秒
//                    Intent it=new Intent(getApplicationContext(),MainActivity.class);//启动MainActivity
//                    startActivity(it);
//                    finish();//关闭当前活动
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        myThread.start();//启动线程
        //闪屏结束后，启动游戏的主Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
