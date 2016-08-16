package me.terge.sdkcompare.hook;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.stat.UCStat;

/**
 * Created by terge on 16-8-15.
 */
public class UCHooker extends BaseHooker{
    private static final String TAG = "UCGameSdk";
    private UCHooker(){
        super();
        mPlatform = "UC";
    };
    private static volatile UCHooker mDispatcher;
    private static final String SDK_API = "cn.uc.gamesdk.UCGameSdk";
    public static UCHooker getInstance(){
        if(mDispatcher == null){
            synchronized (UCHooker.class){
                if(mDispatcher == null) {
                    mDispatcher = new UCHooker();
                }
            }
        }

        return mDispatcher;
    }

    public void dispachHook(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        if(hookParamMap.size() == 0)initLeanCloud(loadPackageParam);
        hookAplicationOnCreate(loadPackageParam);
        hookInit(loadPackageParam);
    }

    private boolean isFirstInit = false;
    private List<HookParam> hookParam;
    private void hookAplicationOnCreate(final XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.thisObject;
                isFirstInit = context.getSharedPreferences("cn.uc.gamesdk.pref",Context.MODE_PRIVATE).getAll().size() ==0;
            }

        });
    }


    private long initInvokeStartTime = 0l;
    private long initInvokeEndTime = 0l;
    private long initCallbackTime = 0l;
    private void hookInit(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
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
                initCallbackTime = System.currentTimeMillis();
                long initCost = initCallbackTime-initInvokeStartTime;
                long initInvokeCost = initInvokeEndTime-initInvokeStartTime;
                XposedBridge.log(TAG+" init callback at："+initCallbackTime+" code:"+param.args[0]);
                XposedBridge.log(TAG+" init-cost:"+initCost);
                XposedBridge.log(TAG+" init-invoke-cost:"+initInvokeCost);
                new UCStat()
                        .setInitCost(initCost)//
                        .setInitInvokeCost(initInvokeCost)//
                        .setPkgName(loadPackageParam.packageName)//
                        .setGameName(loadPackageParam.appInfo.name)//
                        .setIsFirtInit(isFirstInit)//
                        .saveInBackground();
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

            }
        };


        final XC_MethodHook initHookListener =  new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                initInvokeStartTime = System.currentTimeMillis();
                XposedBridge.log(TAG+" init start at："+initInvokeStartTime);
                //hook初始化成功的回调
                XposedHelpers.findAndHookMethod("com.sqwan.msdk.api.sdk.UC$2$1",loadPackageParam.classLoader,"callback",//
//                XposedHelpers.findAndHookMethod("cn.uc.gamesdk.demo.fragment.ApiFragment$2",loadPackageParam.classLoader,"callback",//
                        int.class,Object.class,initFinishListener);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                initInvokeEndTime = System.currentTimeMillis();
                XposedBridge.log(TAG+" init end at："+initInvokeEndTime);
            }
        };


        //hook初始化调用
        XposedHelpers.findAndHookMethod(SDK_API, loadPackageParam.classLoader, "initSdk", //
                Activity.class,
                logLevelClz,
                boolean.class,
                gameParamInfoClz,
                callbackClz,
                initHookListener);
    }
}
