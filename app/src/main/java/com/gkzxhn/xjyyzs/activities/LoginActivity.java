package com.gkzxhn.xjyyzs.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.requests.bean.LoginInfo;
import com.gkzxhn.xjyyzs.requests.bean.LoginResult;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.service.LoginNimService;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Subscriber;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:login
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_device_identifier)
    TextView tv_device_identifier;
    private String username;
    private String password;
    private SweetAlertDialog loginDialog;

    /**
     * 初始化HttpLoggingInterceptor
     */
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            //打印retrofit日志
            Log.i("RetrofitLog", "retrofitBack = " + message);

        }
    });

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_staff_loading, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

        setTitleText("登录");

        btn_login.setOnClickListener(this);

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//retrofit日志请求级别

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    showToastShortMsg(getString(R.string.account_pwd_empty));
                } else if (password.length() < 6) {
                    showToastShortMsg(getString(R.string.pwd_less_6));
                } else {
                    doLogin();// 登录
                }
                break;
        }
    }

    /**
     * 登录
     */
    private void doLogin() {
        if (tv_device_identifier.isShown()) tv_device_identifier.setVisibility(View.GONE);
        initAndShowDialog();// 显示进度条对话框
        RequestMethods.login(getRequestBody(), new Subscriber<LoginResult>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                String error_msg = e.getMessage();
                Log.e(TAG, "login failed : " + error_msg);
                Log.e(TAG, "登录错误信息" + e);
                if (error_msg.contains(getString(R.string.code_404))) {
                    showLoginFailed(getString(R.string.account_pwd_error));
                } else if (error_msg.contains(getString(R.string.code_500))) {
                    showLoginFailed(getString(R.string.server_error));
                } else if (error_msg.contains(getString(R.string.code_400))) {
                    showLoginFailed(getString(R.string.account_pwd_error));
                } else if (error_msg.contains(getString(R.string.code_401))) {
                    //显示唯一标识码
                    showLoginFailed(getString(R.string.device_not_register));
                    if (!tv_device_identifier.isShown()) {
                        tv_device_identifier.setVisibility(View.VISIBLE);
                        tv_device_identifier.setText(getString(R.string.device_identifier) + ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
                    }
                } else {
                    showLoginFailed(getString(R.string.login_failed));
                }

            }

            @Override
            public void onNext(LoginResult result) {
                Log.i(TAG, "login success : " + result.toString());
                saveUserInfo(result);// save
                loginNim();// 登录云信
                showLoginSuccess();// 登录成功
            }
        });
    }

    /**
     * 登录云信
     */
    private void loginNim() {
        Intent intent = new Intent(LoginActivity.this, LoginNimService.class);
        startService(intent);
    }

    /**
     * 获取请求实体
     *
     * @return
     */
    @NonNull
    private RequestBody getRequestBody() {
        LoginInfo info = new LoginInfo();
        LoginInfo.LoginBean bean = info.new LoginBean();
        bean.setUserid(username);
        bean.setPassword(password);
        // bean.setDeviceId(((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
        bean.setDeviceId("864507030040801");//固定id
        info.setSession(bean);
        Log.i(TAG, "login info : " + info.toString());
        return RequestBody.create(MediaType.
                parse("application/json; charset=utf-8"), new Gson().toJson(info));
    }

    /**
     * 初始化并且显示进度条对话框
     */
    private void initAndShowDialog() {
        loginDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在登录...");
        loginDialog.setCancelable(false);
        loginDialog.show();
    }

    /**
     * 保存用户信息
     *
     * @param result
     */
    private void saveUserInfo(LoginResult result) {
        SPUtil.put(LoginActivity.this, "userid", result.getUser().getUserid());
        SPUtil.put(LoginActivity.this, "password", password);
        SPUtil.put(LoginActivity.this, "token", result.getUser().getToken());
        SPUtil.put(LoginActivity.this, "name", result.getUser().getName());
        SPUtil.put(LoginActivity.this, "cloudToken", result.getUser().getCloudMsg().getToken());
        SPUtil.put(LoginActivity.this, "cloudId", result.getUser().getCloudMsg().getCloudID());
        SPUtil.put(LoginActivity.this, "title", result.getUser().getOrgnization().getTitle());
        SPUtil.put(LoginActivity.this, "organizationCode", result.getUser().getOrgnization().getCode());
    }

    /**
     * 登录成功
     */
    private void showLoginSuccess() {
        loginDialog.getProgressHelper().setBarColor(R.color.success_stroke_color);
        loginDialog.setTitleText("登录成功").setConfirmText("确定").
                changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginDialog.dismiss();
                toNext();
            }
        }, 1000);
    }


    /**
     * 下一步 进入主页
     */
    private void toNext() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /**
     * 登录失败
     *
     * @param titleText
     */
    private void showLoginFailed(String titleText) {
        loginDialog.getProgressHelper().setBarColor(R.color.error_stroke_color);
        loginDialog.setTitleText(titleText)
                .setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        loginDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
    }

}
