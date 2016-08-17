package me.terge.sdkcompare.hook.uc;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;

import me.terge.sdkcompare.hook.BaseInitStat;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
@AVClassName("init")
public class UCInitStat extends BaseInitStat {
    private final String platform = "UC";
    public UCInitStat(){
        put("platform",platform);
    }

}
