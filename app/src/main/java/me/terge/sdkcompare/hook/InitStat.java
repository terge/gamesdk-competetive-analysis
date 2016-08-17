package me.terge.sdkcompare.hook;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
@AVClassName("init")
public class InitStat extends AVObject{
    public InitStat(){}
    public InitStat(String platform){
        put("platform",platform);
    }

    public InitStat setInitCost(long time){
        put("initCost",time);
        return this;
    }

    public InitStat setInitInvokeCost(long time){
        put("initInvokeCost",time);
        return this;
    }

    public InitStat setPkgName(String pkgName){
        put("pkgName",pkgName);
        return this;
    }

    public InitStat setGameName(String gameName){
        put("gameName",gameName);
        return this;
    }

    public InitStat setIsFirtInit(boolean isFirstInit){
        put("isFirstInit",isFirstInit);
        return this;
    }

}
