package me.terge.sdkcompare.hook.qihoo;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;

import me.terge.sdkcompare.hook.BaseInitStat;

/**
 * Created by terge on 16-8-17.
 */
@SuppressLint("ParcelCreator")
@AVClassName("init")
public class QihooInitStat extends BaseInitStat {
    private final String platform = "360";
    public QihooInitStat(){
        put("platform",platform);
    }
}
