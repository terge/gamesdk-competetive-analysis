package me.terge.sdkcompare;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import me.terge.sdkcompare.hook.InitStat;
import me.terge.sdkcompare.hooksupport.HookConfig;

/**
 * Created by terge on 16-8-16.
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initLeanCloud(this);
    }

    public static void initLeanCloud(Context context){
        AVObject.registerSubclass(InitStat.class);
        AVObject.registerSubclass(HookConfig.class);
        AVOSCloud.initialize(context,"M3eWIBkM7cIRQIfXkkwwn0TI-gzGzoHsz","LGFFzabtnByk2cvHsPakwIYP");
    }
}
