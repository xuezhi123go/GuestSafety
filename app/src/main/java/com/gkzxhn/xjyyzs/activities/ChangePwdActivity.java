package com.gkzxhn.xjyyzs.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * created by huangzhengneng on 2016.8.10
 * description:修改密码
 */
public class ChangePwdActivity extends BaseActivity {

    private static final java.lang.String TAG = "ChangePwdActivity";
    @BindView(R.id.old_pwd) EditText oldPwd;
    @BindView(R.id.new_pwd) EditText newPwd;
    @BindView(R.id.confirm_new_pwd) EditText confirmNewPwd;
    @BindView(R.id.bt_ok) Button btOk;

    private String old;// 原密码
    private String newText;// 新密码
    private String confirm;// 确认新密码

    private SweetAlertDialog dialog;// 提交对话框

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_change_pwd, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        setTitleText("修改密码");
        setBackVisibility();
        setBackPressListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBackPress();
            }
        });
    }

    /**
     * 检查输入框  弹出提醒
     */
    private void checkBackPress() {
        if(checkEditText()){
            showReminderDialog();
        }else {
            ChangePwdActivity.this.finish();
        }
    }

    /**
     * 显示提醒对话框
     */
    private void showReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePwdActivity.this);
        builder.setTitle("提示").setMessage("确定放弃编辑吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ChangePwdActivity.this.finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if(checkEditText()){
            checkBackPress();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 检查输入框是否为空
     * @return
     */
    private boolean checkEditText(){
        old = oldPwd.getText().toString().trim();
        newText = newPwd.getText().toString().trim();
        confirm = confirmNewPwd.getText().toString().trim();
        return !(TextUtils.isEmpty(old) || TextUtils.isEmpty(newText) || TextUtils.isEmpty(confirm));
    }

    @OnClick(R.id.bt_ok)
    public void onClick() {
        if(!checkEditText()){
            showToastShortMsg("不能为空");
        }else if(!old.equals(SPUtil.get(this, "password", ""))){
            showToastShortMsg("密码不正确");
        }else if(newText.length() < 6 || confirm.length() < 6){
            showToastShortMsg("密码长度不能低于六位");
        }else if(!newText.equals(confirm)){
            showToastShortMsg("密码不匹配");
        }else {
            changePwd();
        }
    }

    /**
     * 修改密码
     */
    private void changePwd() {
        initAndShowDialog();
        commitData();
    }

    /**
     * 提交数据
     */
    private void commitData() {
        String pwd = "{\"user\":{\"newPassword\":\"" + newText + "\"}}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), pwd);
        RequestMethods.setNumber((String) SPUtil.get(this, "token", "")
                , body, new Subscriber<ResponseBody>() {
                    @Override public void onCompleted() {}
                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "change password failed : " + e.getMessage());
                        showChangeFailed();
                    }

                    @Override public void onNext(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            Log.i(TAG, "change password success : " + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        showChangeSuccess();
                    }
                });
    }

    /**
     * 修改成功
     */
    private void showChangeSuccess() {
        dialog.getProgressHelper().setBarColor(R.color.success_stroke_color);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitleText("修改成功，请重新登录").setConfirmText("确定").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                saveNewPassword();
                sweetAlertDialog.dismiss();
                SPUtil.clear(ChangePwdActivity.this);
                Intent intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                ChangePwdActivity.this.finish();
            }
        });
    }

    /**
     * 保存新密码
     */
    private void saveNewPassword() {
        SPUtil.put(ChangePwdActivity.this, "password", newText);
    }

    /**
     * 修改失败
     */
    private void showChangeFailed() {
        dialog.getProgressHelper().setBarColor(R.color.error_stroke_color);
        dialog.setTitleText("修改失败，请稍后再试").setConfirmText("确定").changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
    }

    /**
     * 初始化并且显示对话框
     */
    private void initAndShowDialog() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在提交...");
        dialog.setCancelable(false);
        dialog.show();
    }
}
