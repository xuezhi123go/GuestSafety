package com.gkzxhn.xjyyzs.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseActivity;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.StringUtils;
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
 * 设置工作人员手机号码
 */
public class SetWorkerPhoneActivity extends BaseActivity {

    private static final java.lang.String TAG = "SetWorkerPhoneActivity";
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.bt_ok)
    Button btOk;

    private SweetAlertDialog dialog;
    private String phone;

    @Override
    public View initView() {
        View view = View.inflate(this, R.layout.activity_set_worker_phone, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        setTitleText("设置");
        setBackVisibility();
        setBackPressListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBackPress();
            }
        });
    }

    /**
     * 检查返回按钮
     */
    private void checkBackPress() {
        if(!TextUtils.isEmpty(phone)){
            showReminderDialog();
        }else {
            SetWorkerPhoneActivity.this.finish();
        }
    }

    /**
     * 提醒对话框
     */
    private void showReminderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetWorkerPhoneActivity.this);
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
                SetWorkerPhoneActivity.this.finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        checkBackPress();
    }

    @OnClick(R.id.bt_ok)
    public void onClick() {
        phone = etPhoneNumber.getText().toString().trim();
        if(!StringUtils.isMobileNO(phone)){
            showToastShortMsg("请输入正确的手机号码");
        }else {
            setWorkerPhone();
        }
    }

    /**
     * 设置号码
     */
    private void setWorkerPhone() {
        initAndShowDialog();
        commitData();
    }

    /**
     * 提交数据
     */
    private void commitData() {
        String pwd = "{\"user\":{\"phone\":\"" + phone + "\"}}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), pwd);
        RequestMethods.setNumber((String) SPUtil.get(this, "token", "")
                , body, new Subscriber<ResponseBody>() {
                    @Override public void onCompleted() {}
                    @Override public void onError(Throwable e) {
                        Log.e(TAG, "set worker phone failed : " + e.getMessage());
                        showChangeFailed();
                    }

                    @Override public void onNext(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            Log.i(TAG, "set worker phone success : " + result);
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
        dialog.setTitleText("修改成功").setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                savePhone();
                sweetAlertDialog.dismiss();
                SetWorkerPhoneActivity.this.finish();
            }
        });
    }

    /**
     * 保存新密码
     */
    private void savePhone() {
        SPUtil.put(SetWorkerPhoneActivity.this, "workerphone", phone);
    }

    /**
     * 修改失败
     */
    private void showChangeFailed() {
        dialog.getProgressHelper().setBarColor(R.color.error_stroke_color);
        dialog.setTitleText("修改失败，请稍后再试").setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
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
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在提交...");
        dialog.setCancelable(false);
        dialog.show();
    }
}
