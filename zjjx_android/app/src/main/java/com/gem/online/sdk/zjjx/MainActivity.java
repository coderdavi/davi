package com.gem.online.sdk.zjjx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.gemstone.sdk.online.GemGameEvent;
import com.android.gemstone.sdk.online.GemGameExitCallback;
import com.android.gemstone.sdk.online.GemGameRole;
import com.android.gemstone.sdk.online.GemInitCallback;
import com.android.gemstone.sdk.online.GemOnlineApi;
import com.android.gemstone.sdk.online.GemPayment;
import com.android.gemstone.sdk.online.GemPurchaseCallback;
import com.android.gemstone.sdk.online.GemResultCode;
import com.android.gemstone.sdk.online.GemUser;
import com.android.gemstone.sdk.online.GemUserStateCallback;
import com.android.gemstone.sdk.online.utils.GemLog;

import org.egret.runtime.launcherInterface.INativePlayer;
import org.egret.egretnativeandroid.EgretNativeAndroid;
import org.json.JSONException;
import org.json.JSONObject;

//Android项目发布设置详见doc目录下的README_ANDROID.md

public class MainActivity extends Activity {
    private final String TAG = "GemOnlineSDK";
    private EgretNativeAndroid nativeAndroid;
    private Activity mActivity;
    private  GemGameExitCallback exitCallback ;
    private GemUserStateCallback userCallback;
    private ImageView launchScreenImageView = null;
    private FrameLayout rootLayout = null;
    //定义变量
    JSONObject initInfo;
    JSONObject payInfo;
    JSONObject loginInfo;
    JSONObject roleInfo;
    JSONObject roleUpInfo;

    public final static String appError = "error";
    // 加载首页失败
    public final static String errorIndexLoadFailed = "load";
    // 启动引擎失败
    public final static String errorJSLoadFailed = "start";
    // 引擎停止运行
    public final static String errorJSCorrupted = "stopRunning";
    public final static String appState = "state";
    // 正在启动引擎
    public final static String stateEngineStarted = "starting";
    // 引擎正在运行
    public final static String stateEngineRunning = "running";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nativeAndroid = new EgretNativeAndroid(this);
        if (!nativeAndroid.checkGlEsVersion()) {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        nativeAndroid.config.showFPS = false;
        nativeAndroid.config.fpsLogTime = 10;
        nativeAndroid.config.disableNativeRender = true;
        nativeAndroid.config.clearCache = false;
        nativeAndroid.config.loadingTimeout = 0;
        mActivity = this;

        //sdk init
        init();
        //sdk notifier
        initNotifier();
        setExternalInterfaces();
        GemOnlineApi.getInstance().onCreate(this, savedInstanceState);

        //生成随机数
        int num = (int)(Math.random()*999+1);
        String agent =android.os.Build.BRAND+android.os.Build.MODEL;
        String gameUrl = "http://www.jyhdhh.com/ZJJX/Zheng/index.html?zjjxH5Key=3756116261&zjjxAgent=gamegsolch055";
        String NewGameUrl = gameUrl+"&v="+num+"&agent="+agent;

        if (!nativeAndroid.initialize(NewGameUrl)) {
            Toast.makeText(this, "Initialize native failed.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(nativeAndroid.getRootFrameLayout());
        rootLayout = nativeAndroid.getRootFrameLayout();
        showLoadingView();
    }
    /**
     *
     * @param name 资源名称
     * @param type 资源类型 如 id, layout, style
     * @return 资源 id
     */
    private int getID(String name, String type){
        return  getApplicationContext().getResources().getIdentifier(name,  type,
                getPackageName());
    }
    private void showLoadingView() {
        launchScreenImageView = new ImageView(this);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(getID("logo","drawable"));
        launchScreenImageView.setImageDrawable(drawable);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        rootLayout.addView(launchScreenImageView, params);
    }
    private void hideLoadingView() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rootLayout.removeView(launchScreenImageView);
                Drawable drawable = launchScreenImageView.getDrawable();
                launchScreenImageView.setImageDrawable(null);
                drawable.setCallback(null);
                launchScreenImageView = null;
            }
        });
    }
//    private int getID(String name, String type){
//        return getApplicationContext().getResources().getIdentifier(name, type, getPackageName());
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        nativeAndroid.pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        nativeAndroid.resume();
//    }

//    @Override
//    public boolean onKeyDown(final int keyCode, final KeyEvent keyEvent) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            nativeAndroid.exitGame();
//        }
//
//        return super.onKeyDown(keyCode, keyEvent);
//    }

    private void setExternalInterfaces() {
        nativeAndroid.setExternalInterface("@onError", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String message) {
                String str = "Native get onError message: ";

                try{
                    JSONObject jsonObject = new JSONObject(message);
                    String error = jsonObject.getString(appError);
                    handleErrorEvent(error);
                }
                catch (JSONException e) {
                    Log.e(TAG, "onError message failed to analyze");
                    return;
                }

                str += message;
                Log.e(TAG, str);
            }
        });
        nativeAndroid.setExternalInterface("@onState", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String message) {
                String str = "Native get onState message: ";

                try{
                    JSONObject jsonObject = new JSONObject(message);

                    String state = jsonObject.getString(appState);
                    handleStateEvent(state);
                 //   hideLoadingView();
                }
                catch (JSONException e) {
                    Log.e(TAG, " onState message failed to analyze");
                }

                str += message;
                Log.e(TAG, str);
            }
        });
        nativeAndroid.setExternalInterface("initGame", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                Log.d("Egret initGame", s);
                try {
                    initInfo = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        nativeAndroid.setExternalInterface("login", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                GemOnlineApi.getInstance().login(mActivity, userCallback);
                hideLoadingView();
                Log.d("Egret login", s);
            }

        });
        nativeAndroid.setExternalInterface("logout", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                GemOnlineApi.getInstance().logout(mActivity);
                Log.d("Egret logout", s);
            }

        });
        nativeAndroid.setExternalInterface("createRole", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                GemOnlineApi.getInstance().updateExtendData(mActivity, GemGameEvent.ROLE_CREATE, getRoleInfo());
                Log.d("Egret CreateRole", s);
            }
        });
        nativeAndroid.setExternalInterface("roleLoginSuccess", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                try {
                    loginInfo = new JSONObject(s);
//                    Log.d(TAG, roleInfo.getString("playerName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GemOnlineApi.getInstance().updateExtendData(mActivity, GemGameEvent.ENTER_SERVER, getRoleInfo());
                Log.d("Egret roleLoginSuccess", s);
            }
        });
        nativeAndroid.setExternalInterface("roleLevelUp", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                GemOnlineApi.getInstance().updateExtendData(mActivity, GemGameEvent.LEVEL_UP, getRoleInfo());
                try {
                    roleUpInfo = new JSONObject(s);
//                    Log.d(TAG,roleUpInfo.getString("level"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Egret RoleLevelUp", s);
            }
        });
        nativeAndroid.setExternalInterface("pay", new INativePlayer.INativeInterface() {
            @Override
            public void callback(String s) {
                try {
                    payInfo = new JSONObject(s);
                    roleInfo = payInfo.getJSONObject("roleInfo");
                    Log.d("pay roleinfo", String.valueOf(roleInfo));
                    Log.d(TAG,s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GemOnlineApi.getInstance().pay(mActivity, getPayment(), getRoleInfo(), new GemPurchaseCallback() {
                    @Override
                    public void payResult(GemResultCode resultCode, GemPayment payment) {
                        GemLog.i("pay result is " + resultCode + " server orderid is " + payment.getServerOrderID());
                        if (GemResultCode.PAY_SUCCESS.equals(resultCode)) {
                            // 支付成功回调.
                            // 但充值是否到账还需以服务器间通知为准，不可将此作为判断充值是否成功的标志。
                            // 该回调仅代表用户已发起支付操作，不代表是否充值成功，具体充值是否到账需以服务器间通知为准；
                            // 可以在此处做支付成功数据统计.
                            // 部分渠道网游支付本地没有支付成功回调~！
                        }

                    }
                });
                Log.d("Egret pay", s);
            }
        });
//        nativeAndroid.setExternalInterface("sendToNative", new INativePlayer.INativeInterface() {
//            @Override
//            public void callback(String message) {
//                String str = "Native get message: ";
//                str += message;
//                Log.d(TAG, str);
//                nativeAndroid.callExternalInterface("sendToJS", str);
//            }
//        });
    }

    private void handleErrorEvent(String error) {

        switch (error) {
            case MainActivity.errorIndexLoadFailed:
                Log.e(TAG, "errorIndexLoadFailed");
                break;
            case MainActivity.errorJSLoadFailed:
                Log.e(TAG, "errorJSLoadFailed");
                break;
            case MainActivity.errorJSCorrupted:
                Log.e(TAG, "errorJSCorrupted");
                break;
            default:
                break;
        }

    }
    private void handleStateEvent(String state) {

        switch (state) {
            case MainActivity.stateEngineStarted:
                Log.e(TAG, "stateEngineStarted");
                break;
            case MainActivity.stateEngineRunning:
                Log.e(TAG, "stateEngineRunning");
                break;
            default:
                break;
        }
    }
    //DataSdk设置回调
    private void initNotifier() {
        userCallback = new GemUserStateCallback() {
            @Override
            public void onUserLogin(GemUser loginUser, GemResultCode resultCode) {
                if (resultCode.equals(GemResultCode.LOG_SUCCESS)) {
                    // 登录成功逻辑
                    GemLog.i("LOG_SUCCESS");
                    Log.d(TAG, "LOG_SUCCESS1");
                    GemLog.i(String.format("login user : \n token : %s \n uid : %s ", loginUser.getGamegs_token() + "", loginUser.getUid() + ""));
                    JSONObject object = new JSONObject();
                    try {
                        object.put("token", loginUser.getGamegs_token());
                        object.put("uid", loginUser.getUid());
                        object.put("egret", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    nativeAndroid.callExternalInterface("Login", object.toString());
                    Log.d(TAG, object.toString());

                } else if (resultCode.equals(GemResultCode.SWITCH_SUCCESS)) {
                    // 账号切换成功逻辑.
                    // 切换成功-- 这里是已经登录成功, 游戏需要把界面切换到登录成功页面,并将游戏内的角色信息换成最新角色信息
                    nativeAndroid.callExternalInterface("androidLogout", "");
                    GemLog.i("SWITCH_SUCCESS");
                    GemLog.i(String.format("login user : \n token : %s \n uid : %s ", loginUser.getGamegs_token() + "", loginUser.getUid() + ""));

                } else if (GemResultCode.SWIICH_FAILED.equals(resultCode)) {
                    // 账号切换失败逻辑
                    GemLog.i("SWIICH_FAILED");

                } else {
                    // 登录失败逻辑.

                }

            }

            @Override
            public void onUserLogout() {
                // 注销逻辑
                GemLog.i("user logout ");
                GemOnlineApi.getInstance().updateExtendData(mActivity, GemGameEvent.EXIT_SERVER, getRoleInfo());
                nativeAndroid.callExternalInterface("androidLogout", "");
                 GemOnlineApi.getInstance().logout(mActivity);
                Log.d(TAG,"sdk log out1");
                Toast.makeText(mActivity, "注销", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void realNameAuthenticate(GemResultCode resultCode) {
                // 实名制验证
                GemLog.i("authenticate result " + resultCode);


            }
        };
        exitCallback = new GemGameExitCallback(){
            public void onGameExit() {
                GemOnlineApi.getInstance().updateExtendData(mActivity, GemGameEvent.EXIT_SERVER, getRoleInfo());
                Toast.makeText(mActivity, "显示游戏自己的退出界面", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(mActivity).setTitle("  你确定要退出游戏吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mActivity.finish();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }

            @Override
            public void onChannelExit() {
                GemOnlineApi.getInstance().updateExtendData(mActivity, GemGameEvent.EXIT_SERVER, getRoleInfo());
                Toast.makeText(MainActivity.this, "渠道界面退出", Toast.LENGTH_SHORT).show();
                finish();
            }
        };

    }
    @Override
    public void onBackPressed() {
        GemOnlineApi.getInstance().quitGame(mActivity,exitCallback);
    }

    private GemPayment getPayment() {
        GemPayment payment = new GemPayment();
        try {
            payment.setProductName(payInfo.getString("goodsTitle"));// 商品名称
        } catch (JSONException e) {
            e.printStackTrace();
        }
        payment.setCount(1);// 商品数量填1即可
        try {
            payment.setDescription(payInfo.getString("productDescription"));//商品描述
        } catch (JSONException e) {
            e.printStackTrace();
        }
        payment.setExtendsData("扩展参数"); // 扩展参数
        try {
            payment.setGameOrderID(payInfo.getString("gameOrderId")); // 商品订单
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            payment.setAmountByCent((payInfo.getLong("price"))*100); // 游戏金额，填商品总金额(单位分)
        } catch (JSONException e) {
            e.printStackTrace();
        }
        payment.setGameMoneyName("元宝"); // 游戏货币名称
        payment.setRate(1);//比例: 填1即可
        payment.setGameMoney(100);// 游戏内货币，即本次交易购买的游戏内货币
        try {
            payment.setProductID(payInfo.getString("productID"));// 商品id,也就是计费点,每个商品id不能重复，
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 需要与某些渠道运营后台配置价格对应，否则无法唤起支付界面或价格错乱. 举个栗子:
        //  10块钱100钻石计费点，payment.setProductID("1001");
        //  50块钱买510钻石计费点，payment.setProductID("1002");
        return payment;
    }
    private GemGameRole getRoleInfo() {

        /**
         * GemGameRole 构造参数依次是:
         *  游戏名称 -- 必填
         *  角色id  --  必填
         *  角色名称 --  必填 (不能为空,不能为 null，若无，传入“无”)
         *  角色创建时间 --必填 角色创建时间(单位：秒)，长度10，获取服务器存储的时间，不可用手机本地时间
         *  角色性别  -- 必填 角色性别(不能为空,不能位null,若无，传入“无”)
         *
         */
        GemGameRole role = null;
        try {
            role = new GemGameRole(initInfo.getString("markerName"), Integer.valueOf(loginInfo.getString("gameUserNameId")), loginInfo.getString("uName"), System.currentTimeMillis(), "男");
            /**
             * 区服id -- 服务器ID（不能为空，必须为数字，若无，传入 1）
             * 区服名称 -- 服务器名称(不能为空，不能为 null，若无，传入“无”)
             */
            role.setZoneInfo(loginInfo.getInt("zone"), loginInfo.getString("serverID"));
            /**
             * 角色等级 -- (不能为空，必须为数字，且不能为 null，若无，传入 1)
             * 角色地位ID -- (不能为 null，若无，传入 0)
             * 余额    -- (不能为空，不能为 null，若无，传入 0)
             * vip等级 -- (不能为空，必须为数字,若无，传入 0)
             * 角色战斗力 -- 不能为空，必须为数字,不能为 null，若无，传入0
             */
            role.setRoleDynamicInfo(1, 0, 23, 0, 23000);

            /**
             * 所在帮会等级 -- 不能为空，不能为 null，若无，传入“无”
             * 所在帮会首领ID -- 不能为空，必须为数字,不能为 null，若无，传入 0
             */
            role.setGuildDynamicInfo(0, 0);

            /**
             * 所在帮会id -- 不能为空，不能为 null，若无，传入0
             * 所在帮会名称 -- 不能为空，不能为 null，若无，传入“无”
             * 帮派称号id -- 不能为空，不能为 null，若无，传入 0
             */
            role.setGuildInfo(0, "无", 0, "无");

            /**
             * 当前登录玩家的职业ID -- (不能为空，必须为数字，若无，传入 0)
             * 当前登录玩家的职业名称 -- (不能为空，不能为 null，若无，传入“无”)
             */
            role.setProfession(0, "无");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return role;
    }


    //sdk 初始化
    private void init() {
        GemOnlineApi.getInstance().initializeGemOnlineApi(this, new GemInitCallback() {
            @Override
            public void initSuccess() {
                //初始化成功之后才可调用其他接口
            }

            @Override
            public void initFailed() {
                //初始化失败
            }
        });
    }
@Override
protected void onStart() {
    GemOnlineApi.getInstance().onStart(this);
    super.onStart();
}

    @Override
    protected void onRestart() {
        super.onRestart();
        GemOnlineApi.getInstance().onRestart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GemOnlineApi.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GemOnlineApi.getInstance().onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GemOnlineApi.getInstance().onStop(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GemOnlineApi.getInstance().onDestroy(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        GemOnlineApi.getInstance().onNewIntent(this, intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        GemOnlineApi.getInstance().attachBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GemOnlineApi.getInstance().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        GemOnlineApi.getInstance().onConfigurationChanged(this, newConfig);
    }
}
