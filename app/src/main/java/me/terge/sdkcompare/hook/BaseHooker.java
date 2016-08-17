package me.terge.sdkcompare.hook;

import android.util.Log;

import java.util.Map;

/**
 * Created by terge on 16-8-16.
 */
public abstract class BaseHooker {
    protected String mPlatform = null;
    protected  HookConfig mHookConfig;
    private  Map<String,HookConfig> hookConfigMap;
    protected BaseHooker(){
        hookConfigMap = HookParamStore.read();
    }

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

    protected void resetConfig(String pkgName){
        hookConfigMap = HookParamStore.read();
        mHookConfig = hookConfigMap.get(pkgName+mPlatform);
    }


}
