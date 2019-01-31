package com.xiaolan.daemomsprite.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class DevicesUtil {

    /**
     * 获取APP版本号
     *
     * @return
     */
//    public synchronized static int getVersionCode() {
//        int versionCode = 0;
//        PackageManager manager = LdlApplication.getInstance().getPackageManager();
//        try {
//            PackageInfo info = manager.getPackageInfo(LdlApplication.getInstance().getPackageName(), 0);
//            versionCode = info.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionCode;
//    }

    /**
     * 获取APP版本名
     *
     * @return
     */
//    public synchronized static String getVersionName() {
//        String versionName = "";
//        PackageManager manager = LdlApplication.getInstance().getPackageManager();
//        try {
//            PackageInfo info = manager.getPackageInfo(LdlApplication.getInstance().getPackageName(), 0);
//            versionName = info.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return versionName;
//    }

    /**
     * 获取Android唯一标示
     *
     * @return
     */
    public static String getUniquenessCode() {
        Long time = System.currentTimeMillis();
        return getRandom(6) + time + getRandom(6);
    }

    /**
     * 生成随机数
     * @param count 随机数位数
     * @return
     */
    public static String getRandom(int count) {
        String strRand = "";
        for (int i = 0; i < count; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }
        return strRand;
    }

    /**
     * 跳转到桌面
     * @param context
     */
    public static void goToDesktop(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    /*判断app是否在前台*/
    public static boolean isBackground(Context context) {
        boolean isShow = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    isShow = true;
                    return isShow;
                } else {
                    isShow = false;
                    return isShow;
                }
            }
        }
        return isShow;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am != null ? am.getRunningTasks(1) : null;
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return className.equals(cpn.getClassName());
        }
        return false;
    }

    /**
     * 判断手机是否拥有Root权限。
     * @return 有root权限返回true，否则返回false。
     */
    public static boolean isRoot() {
        boolean bool = false;
        try {
            bool = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 获取本地IP
     */
    public synchronized static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    InetAddress inet = enIp.nextElement();
                    if (!inet.isLoopbackAddress() && (inet instanceof Inet4Address)) {
                        return inet.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getDisplayInfomation(Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        Log.e("分辨率","the screen size is "+point.toString());
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        Log.e("分辨率","the screen real size is "+point.toString());

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        float density = dm.densityDpi;
        float fdensity = dm.density;
        Log.e("分辨率", width + " = " + height + " = " + xdpi + " = " + ydpi + " = " + density + " = " + fdensity + " = ");
    }
}
