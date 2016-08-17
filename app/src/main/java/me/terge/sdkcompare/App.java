package me.terge.sdkcompare;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import me.terge.sdkcompare.hook.remoteconfig.HookConfig;

/**
 * Created by terge on 16-8-16.
 */
@AVClassName("Init")
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initLeanCloud(this);
    }

    public static void initLeanCloud(Context context){
        AVObject.registerSubclass(UCInitStat.class);
        AVObject.registerSubclass(QihooInitStat.class);
        AVObject.registerSubclass(BaiduInitStat.class);
        AVObject.registerSubclass(HookConfig.class);
        AVOSCloud.initialize(context,"M3eWIBkM7cIRQIfXkkwwn0TI-gzGzoHsz","LGFFzabtnByk2cvHsPakwIYP");
    }
}
