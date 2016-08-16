package me.terge.sdkcompare.hook;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by terge on 16-8-16.
 */
public class HookDispatcher implements IXposedHookLoadPackage{


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            String pkgName = loadPackageParam.packageName;
            Log.e("terge", "xposed handleLoadPackage: " + pkgName);

            UCHooker ucHooker = UCHooker.getInstance();
            if (ucHooker.care(pkgName)) {
                ucHooker.dispachHook(loadPackageParam);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}