package com.xiaolan.daemomsprite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SilenceInstallReceiver extends BroadcastReceiver {
    private static final String TAG = "SilenceInstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            String packageName = intent.getDataString();
            Log.e(TAG,"升级了:" + packageName + "包名的程序");
            startApp(context);
        }
        // 接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            Log.e(TAG,"安装了:" + packageName + "包名的程序");
            startApp(context);
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
            Log.e(TAG,"卸载了:" + packageName + "包名的程序");
        }
    }

    private void startApp(Context context) {
        // 根据包名打开安装的apk
        Intent resolveIntent = context.getPackageManager().getLaunchIntentForPackage("com.xiaolan.daemomsprite");
        context.startActivity(resolveIntent);
    }
}
