package me.terge.sdkcompare.stat;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVObject;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
public class BaseInitStat extends AVObject{

    public BaseInitStat setInitCost(long time){
        put("initCost",time);
        return this;
    }

    public BaseInitStat setInitInvokeCost(long time){
        put("initInvokeCost",time);
        return this;
    }

    public BaseInitStat setPkgName(String pkgName){
        put("pkgName",pkgName);
        return this;
    }

    public BaseInitStat setGameName(String gameName){
        put("gameName",gameName);
        return this;
    }

    public BaseInitStat setIsFirtInit(boolean isFirstInit){
        put("isFirstInit",isFirstInit);
        return this;
    }

}
