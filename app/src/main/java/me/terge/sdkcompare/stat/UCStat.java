package me.terge.sdkcompare.stat;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;

/**
 * Created by terge on 16-8-16.
 */
@SuppressLint("ParcelCreator")
@AVClassName("init")
public class UCStat extends BaseStat{
    private final String platform = "UC";
    public UCStat(){
        put("platform",platform);
    }

}
