package me.terge.sdkcompare.stat;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVObject;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
public class BaseStat extends AVObject{

    public BaseStat setInitCost(long time){
        put("initCost",time);
        return this;
    }

    public BaseStat setInitInvokeCost(long time){
        put("initInvokeCost",time);
        return this;
    }

    public BaseStat setPkgName(String pkgName){
        put("pkgName",pkgName);
        return this;
    }

    public BaseStat setGameName(String gameName){
        put("gameName",gameName);
        return this;
    }

    public BaseStat setIsFirtInit(boolean isFirstInit){
        put("isFirstInit",isFirstInit);
        return this;
    }

}
