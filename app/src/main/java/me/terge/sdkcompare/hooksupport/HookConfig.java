package me.terge.sdkcompare.hooksupport;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
@AVClassName("hook_config")
public class HookConfig extends AVObject{


    public void setPlatform(String platform) {
        put("platform",platform);
    }

    public void setPkgName(String pkgName) {
        put("pkgName",pkgName);
    }

    public void setGameName(String gameName) {
        put("gameName",gameName);
    }

    public void setInitCallbackClz(String initCallbackClz) {
        put("initCallbackClz",initCallbackClz);
    }

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
