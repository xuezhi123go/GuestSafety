package com.gkzxhn.xjyyzs.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;

/**
 * apk安装接收者  用于版本更新
 */
public class ApkInstallerReceiver extends BroadcastReceiver {

    private static final String TAG = ApkInstallerReceiver.class.getSimpleName();
    private static final String KEY_DOWNLOAD_ID = "downloadId";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            //下载完成
            Log.d(TAG, "new version apk checkHasDownloadNewApk completed");
            long downloadId = (long) SPUtil.get(context, KEY_DOWNLOAD_ID, -1L);
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = dm.getUriForDownloadedFile(downloadId);
            if(uri != null){
                Log.d(TAG, uri.toString());
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(uri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }else {
                Log.e(TAG, "checkHasDownloadNewApk error");
            }
        }
    }
}
