package me.terge.sdkcompare.hook.platform;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.App;
import me.terge.sdkcompare.hooksupport.ConfigService;
import me.terge.sdkcompare.hooksupport.HookConfig;

/**
 * Created by terge on 16-8-16.
 */
public abstract class BaseHooker {
    protected final String TAG = getClass().getSimpleName();
    protected  String mPlatform = null;
    protected HookConfig mHookConfig;
//    private  static Map<String,HookConfig> hookConfigMap;
    protected BaseHooker(){
//        if(hookConfigMap == null)hookConfigMap = HookConfigStore.read();
    }


    public void dispachHook(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        hookAplicationOnCreate(loadPackageParam);
//        hookInit(loadPackageParam);
    }

    protected abstract void hookInit(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException;

    protected boolean isFirstInit = false;
    private void hookAplicationOnCreate(final XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.thisObject;
                startHookConfigService(context,loadPackageParam);
            }
        });
    }


    private void startHookConfigService(final Context context, final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Log.d("terge","startHookConfigService pkgName:"+loadPackageParam.packageName);
        Intent intent = new Intent(context, ConfigService.class);
        intent.setAction(ConfigService.class.getName());
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ConfigService.ConfigBinder binder = (ConfigService.ConfigBinder) iBinder;
                String pkgName = loadPackageParam.packageName;
                mHookConfig = binder.getHookConfig(pkgName,mPlatform);
                Log.d("terge","onServiceConnected - "+pkgName+":"+mHookConfig);
                if(mHookConfig != null) {
                    Log.d("terge",mPlatform+" care "+pkgName);

                    App.initLeanCloud(context);
                    isFirstInit = context.getSharedPreferences(prefName(),Context.MODE_PRIVATE).getAll().size() ==0;

                    try {
                        hookInit(loadPackageParam);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        },Context.BIND_AUTO_CREATE);

    }

    protected abstract String prefName();

//    public  boolean care(String pkgName){
//        boolean isCare = false;
//        if(hookConfigMap == null || hookConfigMap.size() == 0){
//            Log.e("terge","hook config is empty");
//            return false;
//        }
//
//        if(mPlatform == null)throw new RuntimeException("Dit not set Hooker GamePlatform");
//        mHookConfig = hookConfigMap.get(pkgName+mPlatform);
//        isCare = mHookConfig!=null;
//        return isCare;
//    }
//
//
//
//    protected HookConfig getHookConfig(String pkgName){
//        if(mHookConfig != null) return mHookConfig;
//
//        hookConfigMap = HookConfigStore.read();
//        mHookConfig = hookConfigMap.get(pkgName+mPlatform);
//        return mHookConfig;
//    }


}
