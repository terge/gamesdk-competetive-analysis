package me.terge.sdkcompare.hooksupport;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by terge on 16-8-18.
 */
public class ConfigService extends Service{

    private final  ConfigBinder mConfigBinder = new ConfigBinder();
    private static final HookConfigMap mConfigMap = new HookConfigMap();
    @Override
    public void onCreate() {
        super.onCreate();
        syncGetHookConfig();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mConfigBinder;
    }

    public class ConfigBinder extends Binder{
        public HookConfig getHookConfig(String pkgName,String platform){
            return mConfigMap.get(pkgName,platform);
        }
    }


    /**
     * 获取远端的hook配置，有网络操作，并且是同步获取
     */
    private void syncGetHookConfig() {
        Log.d("terge","syncGetHookConfig");
        AVQuery<HookConfig> query = AVQuery.getQuery(HookConfig.class);
        List<HookConfig> list = null;
        final CountDownLatch cdt = new CountDownLatch(1);
        query.findInBackground(new FindCallback<HookConfig>() {
            @Override
            public void done(List<HookConfig> list, AVException e) {
                if(list == null){
                    Log.e("terge","hookParamList is null");
                    return;
                }

                mConfigMap.add(list);
                cdt.countDown();
            }
        });

        try {
            cdt.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
