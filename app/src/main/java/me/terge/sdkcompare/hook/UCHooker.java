package me.terge.sdkcompare.hook;

import android.app.Activity;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.GamePlatform;
import me.terge.sdkcompare.hook.remoteconfig.HookConfig;

/**
 * Created by terge on 16-8-15.
 */
public class UCHooker extends BaseHooker {
    private UCHooker(){
        super();
        mPlatform = "UC";
    };
    private static volatile UCHooker mHooker;
    private static final String SDK_ENTRANCE = "cn.uc.gamesdk.UCGameSdk";
    public static UCHooker getInstance(){
        if(mHooker == null){
            synchronized (UCHooker.class){
                if(mHooker == null) {
                    mHooker = new UCHooker();
                }
            }
        }

        return mHooker;
    }




    private long initInvokeStartTime = 0l;
    private long initInvokeEndTime = 0l;
    private long initCallbackTime = 0l;
    @Override
    protected void hookInit(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        /**
         * 初始化接口如下：
         * public void initSdk(
         *      final Activity activity,
         *      final UCLogLevel logLevel,
         *      final boolean debugMode,
         *      final GameParamInfo gameParams,
         *      final UCCallbackListener<String> callback){....}
         *
         */
        Class<?> logLevelClz = loadPackageParam.classLoader.loadClass("cn.uc.gamesdk.open.UCLogLevel");
        Class<?> gameParamInfoClz = loadPackageParam.classLoader.loadClass("cn.uc.gamesdk.open.GameParamInfo");
        Class<?> callbackClz = loadPackageParam.classLoader.loadClass("cn.uc.gamesdk.open.UCCallbackListener");


        final XC_MethodHook initFinishListener =  new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                int initResultCode = (int) param.args[0];
                //初始化失败,不统计耗时
                if(initResultCode !=0) return;

                initCallbackTime = System.currentTimeMillis();
                long initCost = initCallbackTime-initInvokeStartTime;
                long initInvokeCost = initInvokeEndTime-initInvokeStartTime;
                XposedBridge.log(TAG+" init callback at："+initCallbackTime+" code:"+param.args[0]);
                XposedBridge.log(TAG+" init-cost:"+initCost);
                XposedBridge.log(TAG+" init-invoke-cost:"+initInvokeCost);

                HookConfig config = getHookConfig(loadPackageParam.packageName);
                new InitStat(GamePlatform.UC)
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
                XposedHelpers.findAndHookMethod(config.getInitCallbackClz(),loadPackageParam.classLoader,"callback",//
                        int.class,Object.class,initFinishListener);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                initInvokeEndTime = System.currentTimeMillis();
                XposedBridge.log(TAG+" init end at："+initInvokeEndTime);
            }
        };


        //hook初始化调用
        XposedHelpers.findAndHookMethod(SDK_ENTRANCE, loadPackageParam.classLoader, "initSdk", //
                Activity.class,
                logLevelClz,
                boolean.class,
                gameParamInfoClz,
                callbackClz,
                initHookListener);
    }

    @Override
    protected String prefName() {
        return "cn.uc.gamesdk.pref";
    }
}
