package me.terge.sdkcompare.stat;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;

/**
 * Created by terge on 16-8-17.
 */
@SuppressLint("ParcelCreator")
@AVClassName("init")
public class QihooInitStat extends BaseInitStat{
    private final String platform = "360";
    public QihooInitStat(){
        put("platform",platform);
    }
}
