package com.gkzxhn.xjyyzs.app;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.gkzxhn.xjyyzs.BuildConfig;
import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.activities.MainActivity;
import com.gkzxhn.xjyyzs.entities.Message;
import com.gkzxhn.xjyyzs.entities.events.KickoutEvent;
import com.gkzxhn.xjyyzs.entities.events.SystemMsg;
import com.gkzxhn.xjyyzs.utils.CrashHandler;
import com.gkzxhn.xjyyzs.utils.DBUtils;
import com.gkzxhn.xjyyzs.utils.DensityUtil;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.SystemUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:application自定义类
 */

public class MyApp extends Application {

    private static final String TAG = "MyApp";
    private int NOTIFICATION_ID = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        //setLogToggle();// 设置log开关
        //initCrashCatch();// 初始化crash捕获

        // 云信sdk初始化
        NIMClient.init(MyApp.this, loginInfo(), Options());
        if (SystemUtil.inMainProcess(this)) {
            // 监督在线状态
            observeOnlineStatus();
            observeCustomNotification();
        }
    }

    /**
     * 初始化crash捕获
     */
    private void initCrashCatch() {
        // bugly初始化
        CrashReport.initCrashReport(getApplicationContext(), "900059226", false);
        //CrashReport.testJavaCrash();
        // 本地crash日志
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    /**
     * 设置log开关
     */
    private void setLogToggle() {
        Log.isDebug = BuildConfig.DEBUG;// debug模式下才输入日志
    }

    /**
     * 监听系统通知
     */
    private void observeCustomNotification() {

        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
            @Override
            public void onEvent(CustomNotification customNotification) {

                Log.i(TAG, "custom notification Content : " + customNotification.getContent());// custom notification Content : {'name':'hello, ketty'}
                Log.i(TAG, "custom notification FromAccount : " + customNotification.getFromAccount());//custom notification FromAccount : 99999999
                Log.i(TAG, "custom notification SessionId : " + customNotification.getSessionId());//custom notification SessionId : 99999999
                Log.i(TAG, "custom notification Time : " + customNotification.getTime());//custom notification Time : 1475118947077
                Log.i(TAG, "custom notification SessionType : " + customNotification.getSessionType());//custom notification SessionType : P2P

                Intent intent = new Intent(MyApp.this, MainActivity.class);
                intent.putExtra("type", "notification");

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("有新消息啦")
                        .setContentText(customNotification.getContent())//getContent是云信获取消息内容的方法
                        .setContentIntent(PendingIntent.getActivity(MyApp.this, 0, intent, 0));
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MyApp.this);
                Notification notification = builder.build();
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                managerCompat.notify(NOTIFICATION_ID, notification);
//                NOTIFICATION_ID ++;
                Message msg = new Message();
                msg.setId(System.currentTimeMillis());
                msg.setAccount((String) SPUtil.get(MyApp.this, "cloudId", ""));
                msg.setFromaccount(customNotification.getFromAccount());
                msg.setTime(customNotification.getTime());
                msg.setContent(customNotification.getContent());
                boolean isSuccess = DBUtils.getInstance(MyApp.this).insert(msg);//insert()是向sqlite插入数据的方法

                if (isSuccess)

                    AppBus.getInstance().post(new SystemMsg());

            }
        }, true);
    }

    /**
     * 观察在线状态
     */
    private void observeOnlineStatus() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {
                Log.i(TAG, "User status changed to: " + statusCode);
                if (statusCode.wontAutoLogin()) {
                    Log.i(TAG, "User status changed to: " + statusCode);
                    // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                    Activity activity = AppManager.getInstance().getCurrentActivity();
                    if (activity != null && activity instanceof MainActivity) {
                        AppBus.getInstance().post(new KickoutEvent());
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        }, true);
    }

    /**
     * 如果返回值为 null，则全部使用默认参数。
     */
    private SDKOptions Options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = DensityUtil.getScreenWidthHeight(this)[0] / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.avatar_def;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    /**
     * 自动检索是否曾经登录成功  是  自动登录
     *
     * @return
     */
    private LoginInfo loginInfo() {
        String username = (String) SPUtil.get(this, "cloudId", "");
        String password = (String) SPUtil.get(this, "cloudToken", "");

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            return new LoginInfo(username, password);
        } else {
            return null;
        }
    }
}
