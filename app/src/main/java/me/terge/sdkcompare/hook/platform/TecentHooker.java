package me.terge.sdkcompare.hook.platform;

import android.app.Activity;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.GamePlatform;
import me.terge.sdkcompare.hook.InitStat;

/**
 * Created by terge on 16-8-18.
 */
public class TecentHooker extends BaseHooker{
    private static final String SDK_ENTRANCE = "com.tencent.ysdk.api.YSDKApi";
    private static final String INIT_METHOD = "onCreate";

    private TecentHooker(){
        super();
        mPlatform = GamePlatform.TENCENT;
    };
    private static volatile TecentHooker mHooker;
    public static TecentHooker getInstance(){
        if(mHooker == null){
            synchronized (TecentHooker.class){
                if(mHooker == null) {
                    mHooker = new TecentHooker();
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

//                        HookConfig config = getHookConfig(loadPackageParam.packageName);
                        long initCost = mInitEndTime - mInitStartTime;
                        new InitStat(GamePlatform.TENCENT)
                                .setInitCost(initCost)//
                                .setInitInvokeCost(initCost)//
                                .setPkgName(loadPackageParam.packageName)//
                                .setGameName(mHookConfig==null?"":mHookConfig.getGameName())//
                                .setIsFirtInit(isFirstInit)//
                                .saveInBackground();

                    }
                });
    }

    @Override
    protected String prefName() {
        return "bugly_data";
    }
}
