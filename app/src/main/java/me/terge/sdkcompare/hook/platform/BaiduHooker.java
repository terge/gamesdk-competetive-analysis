package me.terge.sdkcompare.hook.platform;

import android.app.Activity;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.GamePlatform;
import me.terge.sdkcompare.hook.BaseHooker;
import me.terge.sdkcompare.hook.InitStat;
import me.terge.sdkcompare.hook.remoteconfig.HookConfig;

/**
 * Created by terge on 16-8-17.
 */
public class BaiduHooker extends BaseHooker {
    private BaiduHooker(){
        super();
        mPlatform = "Baidu";
    };
    private static volatile BaiduHooker mHooker;
    private static final String SDK_ENTRANCE = "com.baidu.gamesdk.BDGameSDK";
    private static final String INIT_METHOD = "init";
    public static BaiduHooker getInstance(){
        if(mHooker == null){
            synchronized (BaiduHooker.class){
                if(mHooker == null) {
                    mHooker = new BaiduHooker();
                }
            }
        }
        return mHooker;
    }

    long initCallbackTime = 0l;
    long initInvokeStartTime = 0l;
    long initInvokeEndTime = 0l;
    @Override
    protected void hookInit(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        /**
         * 初始化接口如下：
         * static void init(
         *      Activity activity,
         *      BDGameSDKSetting setting,   //com.baidu.gamesdk.BDGameSDKSetting
         *      IResponse<Void> responser)   //com.baidu.gamesdk.IResponse
         *
         */
        Class<?> settingClz = loadPackageParam.classLoader.loadClass("com.baidu.gamesdk.BDGameSDKSetting");
        Class<?> respClz = loadPackageParam.classLoader.loadClass("com.baidu.gamesdk.IResponse");


        final XC_MethodHook initFinishListener =  new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int initResultCode = (int) param.args[0];
                XposedBridge.log(TAG+" init callback at："+initCallbackTime+" code:"+initResultCode);
                //初始化失败,不统计耗时
                if(initResultCode !=0) return;

                initCallbackTime = System.currentTimeMillis();
                long initCost = initCallbackTime-initInvokeStartTime;
                long initInvokeCost = initInvokeEndTime-initInvokeStartTime;


                HookConfig config = getHookConfig(loadPackageParam.packageName);
                new InitStat(GamePlatform.BAIDU)
                        .setInitCost(initCost)//
                        .setInitInvokeCost(initInvokeCost)//
                        .setPkgName(loadPackageParam.packageName)//
                        .setGameName(config==null?"":config.getGameName())//
                        .setIsFirtInit(isFirstInit)//
                        .saveInBackground();
            }

        };


        final XC_MethodHook initHookListener =  new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                initInvokeStartTime = System.currentTimeMillis();
                XposedBridge.log(TAG+" init start at："+initInvokeStartTime);
                //hook初始化成功的回调
                HookConfig config = getHookConfig(loadPackageParam.packageName);
                if(config == null){
                    Log.e("terge","hook config is null");
                    return;
                }
                XposedHelpers.findAndHookMethod(config.getInitCallbackClz(),loadPackageParam.classLoader,"onResponse",//
                        int.class,String.class,Object.class,//
                        initFinishListener);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                initInvokeEndTime = System.currentTimeMillis();
                XposedBridge.log(TAG+" init end at："+initInvokeEndTime);
            }
        };


        //hook初始化调用
        XposedHelpers.findAndHookMethod(SDK_ENTRANCE, loadPackageParam.classLoader, INIT_METHOD, //
                Activity.class,
                settingClz,
                respClz,
                initHookListener);
    }

    @Override
    protected String prefName() {
        return "channelid";
    }
}
