package me.terge.sdkcompare.hook;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.App;

/**
 * Created by terge on 16-8-16.
 */
public abstract class BaseHooker {
    protected String mPlatform = null;
    protected static final Map<String,HookParam> hookParamMap = new HashMap<>();
    public  boolean care(String pkgName){
        boolean isCare = false;
        if(hookParamMap.size() == 0){
            return true;
        }

        HookParam hookParam = hookParamMap.get(pkgName);
        if(mPlatform == null)throw new RuntimeException("Dit not set Hooker GamePlatform");
        if(hookParam !=null && hookParam.getPlatform().equals(mPlatform)){
            isCare = true;
        }
        return isCare;
    }


    protected void initLeanCloud(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.thisObject;
                App.initLeanCloud(context);
                AVQuery<HookParam> query = new AVQuery<HookParam>("HookParam");
                List<HookParam> hookParamList = null;
                try {
                    hookParamList = query.find();
                } catch (AVException e) {
                    e.printStackTrace();
                }
                if(hookParamList == null){
                    hookParamList = new ArrayList<HookParam>();
                    Log.e("terge","hookParamList is null");
                }
                hookParamMap.clear();
                for(HookParam hparam:hookParamList){
                    Log.d("terge","hook param:"+hparam.toString());
                    hookParamMap.put(hparam.getPkgName(),hparam);
                }
            }
        });
    }
}
