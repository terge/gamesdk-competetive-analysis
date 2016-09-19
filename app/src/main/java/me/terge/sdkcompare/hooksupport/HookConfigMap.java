package me.terge.sdkcompare.hooksupport;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by terge on 16-8-18.
 *
 * 存储HookConfig
 * 以pkgName + platform　作为key
 * 方便业务层直接获取自身对应的配置
 */
public class HookConfigMap {
    private final ConcurrentHashMap<String,HookConfig> mMap = new ConcurrentHashMap<>();
    public void add(HookConfig config){
        if(config == null)return;
        String key = genKey(config.getPkgName(),config.getPlatform());
        Log.d("terge","add config "+key+":"+config);
        mMap.put(key,config);
    }

    public void add(List<HookConfig> configList){
        if(configList == null) return;
        for(HookConfig config:configList){
            add(config);
        }
    }

    public HookConfig get(String pkgName,String platform){
        String key = genKey(pkgName,platform);
        return mMap.get(key);
    }

    public int size(){
        return mMap.size();
    }

    public void clear(){
        mMap.clear();
    }

    private String genKey(String pkgName,String platform){
        return pkgName+platform;
    }
}
