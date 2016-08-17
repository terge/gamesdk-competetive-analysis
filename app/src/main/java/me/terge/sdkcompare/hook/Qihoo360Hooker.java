package me.terge.sdkcompare.hook;

import android.app.Activity;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.stat.QihooInitStat;

/**
 * Created by terge on 16-8-17.
 */
public class Qihoo360Hooker extends BaseHooker{
    private static final String SDK_ENTRANCE = "com.qihoo.gamecenter.sdk.matrix.Matrix";
    private static final String INIT_METHOD = "init";

    private Qihoo360Hooker(){
        super();
        mPlatform = "360";
    };
    private static volatile Qihoo360Hooker mHooker;
    public static Qihoo360Hooker getInstance(){
        if(mHooker == null){
            synchronized (Qihoo360Hooker.class){
                if(mHooker == null) {
                    mHooker = new Qihoo360Hooker();
                }
            }
        }
        return mHooker;
    }

    private long mInitStartTime = 0l;
    private long mInitEndTime = 0l;
    @Override
    protected void hookInit(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        //hook初始化调用
        XposedHelpers.findAndHookMethod(SDK_ENTRANCE, loadPackageParam.classLoader, INIT_METHOD, //
                Activity.class,
                new XC_MethodHook(){
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        mInitStartTime = System.currentTimeMillis();
                        XposedBridge.log(TAG+" init stat:"+mInitStartTime);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        mInitEndTime = System.currentTimeMillis();
                        XposedBridge.log(TAG+" init end:"+mInitEndTime);

                        HookConfig config = getHookConfig(loadPackageParam.packageName);
                        long initCost = mInitEndTime - mInitStartTime;
                        new QihooInitStat()
                                .setInitCost(initCost)//
                                .setInitInvokeCost(initCost)//
                                .setPkgName(loadPackageParam.packageName)//
                                .setGameName(config==null?"":config.getGameName())//
                                .setIsFirtInit(isFirstInit)//
                                .saveInBackground();

                    }
                });
    }

    @Override
    protected String prefName() {
        return "sdk_apk_info";
    }
}
