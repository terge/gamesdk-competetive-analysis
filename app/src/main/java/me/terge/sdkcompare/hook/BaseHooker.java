package me.terge.sdkcompare.hook;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.App;
import me.terge.sdkcompare.hook.remoteconfig.HookConfig;
import me.terge.sdkcompare.hook.remoteconfig.HookConfigStore;

/**
 * Created by terge on 16-8-16.
 */
public abstract class BaseHooker {
    protected final String TAG = getClass().getSimpleName();
    protected String mPlatform = null;
    private HookConfig mHookConfig;
    private  static Map<String,HookConfig> hookConfigMap;
    protected BaseHooker(){
        if(hookConfigMap == null)hookConfigMap = HookConfigStore.read();
    }


    public void dispachHook(XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException {
        hookAplicationOnCreate(loadPackageParam);
        hookInit(loadPackageParam);
    }

    protected abstract void hookInit(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws ClassNotFoundException;

    protected boolean isFirstInit = false;
    private void hookAplicationOnCreate(final XC_LoadPackage.LoadPackageParam loadPackageParam){
        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.thisObject;
                App.initLeanCloud(context);
                isFirstInit = context.getSharedPreferences(prefName(),Context.MODE_PRIVATE).getAll().size() ==0;
            }

        });
    }

    protected abstract String prefName();

    public  boolean care(String pkgName){
        boolean isCare = false;
        if(hookConfigMap == null || hookConfigMap.size() == 0){
            Log.e("terge","hook config is empty");
            return false;
        }

        if(mPlatform == null)throw new RuntimeException("Dit not set Hooker GamePlatform");
        mHookConfig = hookConfigMap.get(pkgName+mPlatform);
        isCare = mHookConfig!=null;
        return isCare;
    }



    protected HookConfig getHookConfig(String pkgName){
        if(mHookConfig != null) return mHookConfig;

        hookConfigMap = HookConfigStore.read();
        mHookConfig = hookConfigMap.get(pkgName+mPlatform);
        return mHookConfig;
    }


}
