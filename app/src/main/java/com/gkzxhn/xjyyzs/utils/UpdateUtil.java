package com.gkzxhn.xjyyzs.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/8.
 * description:版本更新工具类
 */

public class UpdateUtil {

    private DownloadManager dm;// 下载管理者
    private Context context;// 上下文
    private static UpdateUtil instance;
    private static final String KEY_DOWNLOAD_ID = "downloadId"; // downloadid保存key
    private static final String TAG = UpdateUtil.class.getSimpleName();

    /**
     * 通过getInstance获取实例
     * @param context
     */
    private UpdateUtil(Context context){
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        this.context = context;
    }

    /**
     * 获取实例
     * @param context
     * @return
     */
    public static UpdateUtil getInstance(Context context){
        if (instance == null){
            instance = new UpdateUtil(context);
        }
        return instance;
    }

    /**
     * 开始下载任务
     * @param url  下载文件的url
     * @param title 通知栏标题
     * @param description 描述
     * @return checkHasDownloadNewApk id  唯一的id
     */
    private long startDownload(String url, String title, String description){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "yyzs-update.apk");
        request.setTitle(title);
        request.setDescription(description);
        return dm.enqueue(request);
    }

    /**
     * 获取文件保存路径
     * @param downloadId
     * @return 返回路径
     */
    private String getDownloadPath(long downloadId){
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = dm.query(query);
        if(cursor != null){
            try {
                if(cursor.moveToFirst()){
                    return cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            }finally {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 获取文件保存的路径Uri
     * @param downloadId
     * @return
     */
    private Uri getDownloadUri(long downloadId){
        return dm.getUriForDownloadedFile(downloadId);
    }
    /**
     * 获取download manager
     * @return
     */
    private DownloadManager getDownloadManager(){
        return dm;
    }

    /**
     * 获取下载状态
     * @param downloadId
     * @return
     */
    private int getDownloadStatus(long downloadId){
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = dm.query(query);
        if(cursor != null){
            try {
                if(cursor.moveToFirst()){
                    return cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            }finally {
                cursor.close();
            }
        }
        return -1;
    }

    /**
     * 检查是否有已下载好的新版本apk
     */
    public void checkHasDownloadNewApk(){
        long downloadId = (long) SPUtil.get(context, KEY_DOWNLOAD_ID, -1L);
        if (downloadId != -1L){
            int status = getDownloadStatus(downloadId);
            if(status == DownloadManager.STATUS_SUCCESSFUL){
                Uri uri = getDownloadUri(downloadId);
                if(uri != null){
                    if(compare(SystemUtil.getApkInfo(uri.getPath(), context))){
                        startInstall(uri);
                    } else {
                        dm.remove(downloadId);
                    }
                }
            } else {
                Log.i(TAG, "apk is already downloading");
            }
        }else {
            Log.i(TAG, "sp has no checkHasDownloadNewApk id");
        }
    }

    /**
     * 开始下载
     * @param url
     * @param title
     */
    public void start(String url, String title) {
        long downloadId = startDownload(url, title, "下载完成后点击打开");
        SPUtil.put(context, KEY_DOWNLOAD_ID, downloadId);
        Log.d(TAG, "apk start checkHasDownloadNewApk :" + downloadId);
    }

    /**
     * 开始安装
     * @param uri
     */
    private void startInstall(Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 比较已下载的apk和当前安装的apk包信息
     * @param info
     * @return 包名相同并且新apk版本号大于已安装的版本号返回true
     */
    private boolean compare(PackageInfo info){
        if (info == null){
            return false;
        }
        String localPackage = context.getPackageName();
        if(info.packageName.equals(localPackage)){
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (info.versionCode > packageInfo.versionCode){
                    return true;
                }else {
                    Log.i(TAG, "apk's versioncode <= app's versionCode");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "apk's package not match app's package");
        return false;
    }
}
