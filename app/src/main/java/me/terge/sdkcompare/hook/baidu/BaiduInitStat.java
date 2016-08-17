package me.terge.sdkcompare.hook.baidu;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;

import me.terge.sdkcompare.hook.BaseInitStat;

/**
 * Created by terge on 16-8-17.
 */
@SuppressLint("ParcelCreator")
@AVClassName("init")
public class BaiduInitStat extends BaseInitStat{

    private final String platform = "baidu";
    public BaiduInitStat(){
        put("platform",platform);
    }
}
