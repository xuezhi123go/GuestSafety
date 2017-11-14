package com.gkzxhn.xjyyzs.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * Created by zhengneng on 2015/12/18.
 */
public class SystemUtil {

    private static final String TAG = "SystemUtil";

    /**
     * 得到versionCode
     *
     * @return
     */
    public static String getVersionName(Context context) {
        // 包管理器
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 得到versionCode
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        // 包管理器
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 是否在主线程
     * @param context
     * @return
     */
    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = SystemUtil.getSystemProcessName(context);
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    public static final String getSystemProcessName(Context context) {
        String processName = null;
        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }
            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 获取应用包信息
     * @param path
     * @param context
     * @return
     */
    public static PackageInfo getApkInfo(String path, Context context){
        Log.d(TAG, "apk checkHasDownloadNewApk file : " + path);
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path,
                PackageManager.GET_ACTIVITIES);
        if(info != null){
            return info;
        }
        return null;
    }

    /**
     * 网络是否可用
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 网络是否是wifi链接
     * @return
     */
    public static boolean isWifiContected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    if(info.getType() == ConnectivityManager.TYPE_WIFI) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
