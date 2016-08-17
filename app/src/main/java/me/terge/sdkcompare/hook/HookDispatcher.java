package me.terge.sdkcompare.hook;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.terge.sdkcompare.hook.baidu.BaiduHooker;
import me.terge.sdkcompare.hook.qihoo.Qihoo360Hooker;
import me.terge.sdkcompare.hook.uc.UCHooker;

/**
 * Created by terge on 16-8-16.
 */
public class HookDispatcher implements IXposedHookLoadPackage{


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            String pkgName = loadPackageParam.packageName;
            Log.d("terge", "xposed handleLoadPackage: " + pkgName);

            UCHooker ucHooker = UCHooker.getInstance();
            if (ucHooker.care(pkgName)) {
                Log.d("terge","uc care:"+pkgName);
                ucHooker.dispachHook(loadPackageParam);
                return;
            }

            Qihoo360Hooker qhHooker = Qihoo360Hooker.getInstance();
            if(qhHooker.care(pkgName)){
                Log.d("terge","360 care:"+pkgName);
                qhHooker.dispachHook(loadPackageParam);
                return;
            }

            BaiduHooker bdHooker = BaiduHooker.getInstance();
            if(bdHooker.care(pkgName)){
                Log.d("terge","baidu care:"+pkgName);
                bdHooker.dispachHook(loadPackageParam);
                return;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
