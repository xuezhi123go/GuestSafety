package com.gkzxhn.xjyyzs.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.gkzxhn.xjyyzs.requests.bean.Apply;
import com.gkzxhn.xjyyzs.requests.bean.FamilyBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Author: Huang ZN
 * Date: 2016/10/8
 * Email:943852572@qq.com
 * Description:u相关工具类
 */

public class UIUtil {

    private static final String TAG = UIUtil.class.getName();

    /**
     * show进队条对话框
     *
     * @param context
     * @param title
     * @param msg
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgressDialog(Context context, String title,
                                                    String msg, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        if (!TextUtils.isEmpty(title))
            dialog.setTitle(title);
        if (!TextUtils.isEmpty(msg))
            dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }

    /**
     * 检查三个输入框信息完整性
     *
     * @param name
     * @param ic_card_number
     * @param phone
     * @return 都不为空返回true
     */
    public static String checkInfoComplete(String name, String ic_card_number,
                                           String phone) {
        if (TextUtils.isEmpty(name))
            return "姓名为空";
        if (TextUtils.isEmpty(ic_card_number))
            return "身份证号码为空";
        if (TextUtils.isEmpty(phone))
            return "电话号码为空";
        if (PhoneNumberUtil.checkNumber(phone).getType() == PhoneNumberUtil.PhoneType.INVALIDPHONE)
            return "电话号码不合法";
        try {
            if (!StringUtils.IDCardValidate(ic_card_number).equals(""))
                return "身份证号不合法";
        } catch (Exception e) {
            e.printStackTrace();
            return "身份证号不合法";
        }
        return "";
    }

    /**
     * 获取请求实体类
     *
     * @return
     */
    public static RequestBody getRequestBody(Context context, String name, String phone, String uuid,
                                             String apply_date, List<FamilyBean> familyBeen) {
        Apply apply = new Apply();
        Apply.ApplyBean bean = apply.new ApplyBean();
        bean.setPhone(phone);

        bean.setUuid(StringUtils.getEncodedUuid(uuid));

        //这是一条测试方法
        //bean.setUuid(uuid);

        bean.setName(name);
        bean.setOrgCode((String) SPUtil.get(context, "organizationCode", ""));
        bean.setApplyDate(apply_date);
        List<Apply.ApplyBean.Family> list = new ArrayList<>();
        if (familyBeen.size() > 0) {
            for (FamilyBean bean1 : familyBeen) {
                Apply.ApplyBean.Family family = bean.new Family();
                family.setPhone(bean1.getPhone());
                family.setUuid(StringUtils.getEncodedUuid(bean1.getUuid()));
                family.setName(bean1.getName());
                list.add(family);
            }
        }
        bean.setFamily(list);
        apply.setApply(bean);
        String apply_json = new Gson().toJson(apply);
        Log.i(TAG, "apply body : " + apply_json);
        return RequestBody.create(MediaType.
                parse("application/json; charset=utf-8"), apply_json);
    }

}

