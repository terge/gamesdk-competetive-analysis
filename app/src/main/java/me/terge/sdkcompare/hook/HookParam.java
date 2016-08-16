package me.terge.sdkcompare.hook;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
@AVClassName("hook_param")
public class HookParam extends AVObject{
    public String platform;
    public String pkgName;
    public String gameName;
    public String initCallbackClz;


    public String getPlatform() {
        return getString("platform");
    }

    public String getPkgName() {
        return getString("pkgName");
    }

    public String getGameName() {
        return getString("gameName");
    }

    public String getInitCallbackClz() {
        return getString("initCallbackClz");
    }
}
