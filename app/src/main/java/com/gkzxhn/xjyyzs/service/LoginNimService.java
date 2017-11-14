package com.gkzxhn.xjyyzs.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:云信登录service
 */

public class LoginNimService extends IntentService {

    private static final String TAG = "LoginNimService";

    public LoginNimService() {
        super(null);
    }

    public LoginNimService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loginNim();
    }

    /**
     * 登录云信
     */
    private void loginNim() {
        String cloudId = (String) SPUtil.get(this, "cloudId", "");
        String cloudToken = (String) SPUtil.get(this, "cloudToken", "");
        LoginInfo info = new LoginInfo(cloudId, TextUtils.isEmpty(cloudToken) ? "123456" : cloudToken);
        Log.i(info.getAccount() + "--" + info.getToken());
        RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                Log.i(TAG, "login nim success");
            }

            @Override
            public void onFailed(int code) {
                Log.e(TAG, "login nim failed, error code : " + code);
            }

            @Override
            public void onException(Throwable exception) {
                Log.e(TAG, "login nim exception, description : " + exception.getMessage());
            }
        };
        if (!TextUtils.isEmpty(cloudId) && !TextUtils.isEmpty(cloudToken)) {
            NIMClient.getService(AuthService.class).login(info).setCallback(callback);
        } else {
            Log.i("cloudId or cloudToken is Empty!");
        }
    }
}
