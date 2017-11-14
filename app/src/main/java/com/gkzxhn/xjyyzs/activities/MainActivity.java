package com.gkzxhn.xjyyzs.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.app.AppBus;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.entities.events.ClearMsg;
import com.gkzxhn.xjyyzs.entities.events.ClearMsgResult;
import com.gkzxhn.xjyyzs.entities.events.KickoutEvent;
import com.gkzxhn.xjyyzs.fragments.HomeFragment;
import com.gkzxhn.xjyyzs.requests.Constant;
import com.gkzxhn.xjyyzs.requests.bean.UpdateInfo;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.service.LoginNimService;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.SystemUtil;
import com.gkzxhn.xjyyzs.utils.UpdateUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:主Activity
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.fl_container)
    FrameLayout fl_container;
    private FragmentManager manager;
    private FragmentTransaction transaction = null;
    private HomeFragment homeFragment = null;

    private long mExitTime;//add by hzn 退出按键时间间隔

    private AlertDialog logout_dialog;// 注销对话框
//    private SimilarityDegreeDialog mSimilarityDegreeDialog;

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_main, null);
        ButterKnife.bind(this, view);
        AppBus.getInstance().register(this);
        return view;
    }

    @Override
    protected void initData() {

        //removeTitleBar();//隐藏标题栏

        setSupportActionBar(tool_bar);
        setTitleText("预约助手");
        initFragment();
        checkOnlineStatus();// 检查云信id在线状态
    }

    /**
     * 注销对话框
     */
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View logout_dialog_view = View.inflate(MainActivity.this, R.layout.msg_ok_cancel_dialog, null);
        builder.setView(logout_dialog_view);
        TextView tv_cancel = (TextView) logout_dialog_view.findViewById(R.id.tv_cancel);
        final AlertDialog dialog = builder.create();
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tv_ok = (TextView) logout_dialog_view.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                SPUtil.clear(MainActivity.this);
                startActivity(intent);
                NIMClient.getService(AuthService.class).logout();
            }
        });
        dialog.show();
    }

    /**
     * 检查在线状态
     */
    private void checkOnlineStatus() {
        StatusCode code = NIMClient.getStatus();
        Log.i(TAG, code.name());
        if (code == StatusCode.KICKOUT) {
            //  弹出提示框  重新登录
            showReLoginDialog();
            return;
        }
        // 延时5秒判断是否云信账号在线状态  不在线重新开启一次登录服务
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NIMClient.getStatus() != StatusCode.LOGINED) {
                    // 云信账号未登录
                    loginNim();
                }
            }
        }, 5000);
    }

    /**
     * 登录云信
     */
    private void loginNim() {
        Intent intent = new Intent(MainActivity.this, LoginNimService.class);
        startService(intent);
    }

    /**
     * 显示重新登录的提示框
     */
    public void showReLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("账号下线提示");
        builder.setCancelable(false);
        builder.setMessage("您的账号" + SPUtil.get(this, "username", "") + "在其他设备登录，点击重新登录。");
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                SPUtil.clear(MainActivity.this);
                startActivity(intent);
                NIMClient.getService(AuthService.class).logout();
            }
        });
        logout_dialog = builder.create();
        logout_dialog.show();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        homeFragment = new HomeFragment();
        transaction.replace(R.id.fl_container, homeFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (logout_dialog != null) {
            if (logout_dialog.isShowing())
                logout_dialog.dismiss();
            logout_dialog = null;
        }
//        if(mSimilarityDegreeDialog!=null&&mSimilarityDegreeDialog.isShowing())mSimilarityDegreeDialog.dismiss();
        AppBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            showLogoutDialog();
        } else if (id == R.id.change_pwd) {
            Intent intent = new Intent(this, ChangePwdActivity.class);
            startActivity(intent);
        }
//        else if(id == R.id.change_phone){
//            Intent intent = new Intent(this, SetWorkerPhoneActivity.class);
//            startActivity(intent);
//        }
        else if (id == R.id.update) {
            if (SystemUtil.isNetworkAvailable(this)) {
                checkUpdate();// 检查更新
            } else {
                showToastShortMsg("网络不可用");
            }
        } else if (id == R.id.clear_msg) {
            clearMsg();
//        }else if(id==R.id.setting_compare_value){//设置人脸识别相似度值
//            if(mSimilarityDegreeDialog==null){
//                mSimilarityDegreeDialog=new SimilarityDegreeDialog(this);
//            }
//            if(!mSimilarityDegreeDialog.isShowing())mSimilarityDegreeDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 清除消息
     */
    private void clearMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定清除所有系统消息吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppBus.getInstance().post(new ClearMsg());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Subscribe
    public void clearMsgReuslt(ClearMsgResult result) {
        if (result.isSuccess()) {
            showToastShortMsg("已清空");
        } else {
            showToastShortMsg("清除失败，请稍后再试！");
        }
    }

    @Subscribe
    public void kickout(KickoutEvent event) {
        showReLoginDialog();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        showToastShortMsg("正在检查...");
        RequestMethods.checkUpdate(new Subscriber<UpdateInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "check update failed :" + e.getMessage());
                showToastShortMsg("服务器错误");
            }

            @Override
            public void onNext(UpdateInfo updateInfo) {
                int getVersionCode = updateInfo.getVersionCode();
                int myVersionCode = SystemUtil.getVersionCode(MainActivity.this);

                Log.e(TAG, "新版本：" + getVersionCode + "      旧版本：" + myVersionCode);

                if (updateInfo.getVersionCode() > SystemUtil.getVersionCode(MainActivity.this)) {
                    showUpdateDialog(updateInfo.getContent());
                } else {
                    showToastShortMsg("当前已是最新版本");
                }
            }
        });
    }

    /**
     * 显示更新提示对话框
     *
     * @param content 新版本内容简要
     */
    private void showUpdateDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("有新版本");
        builder.setMessage(content);
        builder.setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!SystemUtil.isWifiContected(MainActivity.this)) {
                    showNotWifiReminder();// 不是wifi提示
                } else {
                    downLoadNewApk();
                }
            }
        }).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 下载新版本
     */
    private void downLoadNewApk() {
        UpdateUtil.getInstance(this).
                start(Constant.NEW_APK,
                        getResources().getString(R.string.app_name) + "新版本");
    }

    /**
     * 提示不是wifi是否继续下载
     */
    private void showNotWifiReminder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(R.string.wifi_reminder);
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadNewApk();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
