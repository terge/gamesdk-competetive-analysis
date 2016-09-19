package me.terge.sdkcompare.hook;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.hook.platform.BaiduHooker;
import me.terge.sdkcompare.hook.platform.Qihoo360Hooker;
import me.terge.sdkcompare.hook.platform.TecentHooker;
import me.terge.sdkcompare.hook.platform.UCHooker;

/**
 * Created by terge on 16-8-16.
 */
public class HookDispatcher implements IXposedHookLoadPackage{


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            String pkgName = loadPackageParam.packageName;
            if(isInBlackList(pkgName))return;

            Log.d("terge", "xposed handleLoadPackage: " + pkgName);

            UCHooker.getInstance().dispachHook(loadPackageParam);
            Qihoo360Hooker.getInstance().dispachHook(loadPackageParam);
            BaiduHooker.getInstance().dispachHook(loadPackageParam);
            TecentHooker.getInstance().dispachHook(loadPackageParam);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean isInBlackList(String pkgName){
        if(pkgName.startsWith("com.google.android"))return true;
        if(pkgName.startsWith("com.android"))return true;
        if(pkgName.startsWith("me.terge"))return true;
        return false;
    }


}
