package com.gem.online.sdk.zjjx;

import com.android.gemstone.sdk.online.proxy.GemOnlineApplication;

/**
 * 作者：solowei
 * 时间：2018/7/30 11:06
 * 描述：
 */
public class MyApplication extends GemOnlineApplication {
    @Override
    public void onCreate() {
        super.onCreate();
         // JSDataOnlineGame.getInstance().onApplicationCreate(this);
    }
}
