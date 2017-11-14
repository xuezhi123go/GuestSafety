package com.gkzxhn.xjyyzs.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.entities.BookResult;
import com.gkzxhn.xjyyzs.requests.bean.FamilyBean;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.DateUtils;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.PhoneNumberUtil;
import com.gkzxhn.xjyyzs.utils.SPUtil;
import com.gkzxhn.xjyyzs.utils.StringUtils;
import com.gkzxhn.xjyyzs.utils.UIUtil;
import com.gkzxhn.xjyyzs.view.decoration.DividerItemDecoration;
import com.gkzxhn.xjyyzs.view.decoration.MyLinearLayoutManager;
import com.gkzxhn.xjyyzs.view.dialog.SweetAlertDialog;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:给家属预约
 */
public class BookFragment extends BaseFragment {

    private static final String[] DATE_LIST = DateUtils.afterNDay(30).
            toArray(new String[DateUtils.afterNDay(30).size()]);// 时间选择;
    private static final String TAG = "BookFragment";

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_ic_card_number)
    EditText et_ic_card_number;
    @BindView(R.id.sp_date)
    Spinner sp_date;
    @BindView(R.id.bt_remote_meeting)
    Button bt_remote_meeting;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.ll_added)
    LinearLayout ll_added;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private ArrayAdapter<String> date_adapter;// 预约日期适配器
    private String name;// 姓名
    private String phone; // 电话号码
    private String uuid; // 身份证
    private String apply_date; // 申请日期
    private SweetAlertDialog apply_dialog; // 进度条对话框
    private List<FamilyBean> familyList = new ArrayList<>();
    private AddBookAdapter addBookAdapter;
    private boolean isDisable = false;

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_book, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        date_adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, DATE_LIST);
        sp_date.setAdapter(date_adapter);
    }

    /**
     * 申请
     */
    private void apply() {
        initAndShowDialog();// 进度条对话框
        RequestMethods.bookMeeting((String) SPUtil.get(getActivity(), "token", ""),
                UIUtil.getRequestBody(getActivity(), name, phone, uuid, apply_date, familyList),
                new Subscriber<BookResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "apply failed : " + e);
                        showApplyFailedDialog(getString(R.string.apply_failed));
                    }

                    @Override
                    public void onNext(BookResult bookResult) {
                        // {code=404, msg='@#*@S%&BB@*@*D#P&S,@#*P&&&BB%*&*%QQ@x'}
                        Log.i(TAG, "apply success : " + bookResult.toString());
                        int code = bookResult.getCode();
                        if (code == 200) {
                            showApplySuccessDialog();
                        } else if (code == 404) {
                            String msg = getMessageContent(bookResult);
                            showApplyFailedDialog(msg);
                        } else {
                            showApplyFailedDialog(bookResult.getMsg());
                        }
                    }
                });
    }

    /**
     * @param bookResult
     * @return
     */
    private String getMessageContent(BookResult bookResult) {
        String msg = bookResult.getMsg();
        String content = "身份证尾号为 ";
        if (msg.contains(",")) {
            String[] ids = msg.split(",");
            for (int i = 0; i < ids.length; i++) {
                if (i == ids.length - 1) {
                    content += StringUtils.decryptUuid(ids[i]).substring(ids[i].length() - 4);
                } else {
                    content += StringUtils.decryptUuid(ids[i]).substring(ids[i].length() - 4) + ",";
                }
            }
            content += "等";
        } else {
            content += StringUtils.decryptUuid(msg).substring(msg.length() - 4);
        }
        Log.i(TAG, content);
        return content + " 用户没有会见权限";
    }

    /**
     * 清空输入框以及列表数据，隐藏已添加布局
     */
    private void clearEditTextAndList() {
        et_name.setText("");
        et_ic_card_number.setText("");
        et_phone.setText("");
        if (addBookAdapter != null && ll_added.getVisibility() == View.VISIBLE) {
            ll_added.setVisibility(View.GONE);
            familyList.clear();
            addBookAdapter.removeAllItem();
        }
    }

    /**
     * 申请成功
     */
    private void showApplySuccessDialog() {
        apply_dialog.getProgressHelper().setBarColor(R.color.success_stroke_color);
        apply_dialog.setTitleText("申请成功").setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        apply_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                // 清空输入框以及列表
                clearEditTextAndList();
            }
        });
        delayDismissDialog(true, 2000);
    }

    /**
     * 若用户没有手动点确定  延迟两秒自动dismiss
     */
    private void delayDismissDialog(final boolean isClearable, long timeout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (apply_dialog.isShowing()) {
                    apply_dialog.dismiss();
                    // 清空输入框以及列表
                    if (isClearable)
                        clearEditTextAndList();
                }
            }
        }, timeout);
    }

    /**
     * 申请失败对话框
     */
    private void showApplyFailedDialog(String titleText) {
        apply_dialog.getProgressHelper().setBarColor(R.color.error_stroke_color);
        apply_dialog.setTitleText(titleText).setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        apply_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        delayDismissDialog(false, 3500);
    }

    /**
     * 初始化并且显示对话框
     */
    private void initAndShowDialog() {
        apply_dialog = new SweetAlertDialog(getActivity(),
                SweetAlertDialog.PROGRESS_TYPE);
        apply_dialog.setTitleText("正在提交...").setCancelable(false);
        apply_dialog.show();
    }

    /**
     * 检查输入框
     *
     * @return
     */
    private boolean checkEditText() {
        name = et_name.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        uuid = et_ic_card_number.getText().toString().trim();
        apply_date = DATE_LIST[sp_date.getSelectedItemPosition()];
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(uuid) || TextUtils.isEmpty(apply_date));
    }

    @OnClick({R.id.add, R.id.bt_remote_meeting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                // 添加预约条目
                showAddItemDialog();
                break;
            case R.id.bt_remote_meeting:
                checkText(et_ic_card_number.getText().toString().trim());// 检查文本

                //以下两条是测试方法
               // checkEditText();
               // apply();


                break;
        }
    }

    /**
     * 检查输入框文本
     */
    private void checkText(String uuid) {
        if (!checkEditText()) {
            showToastMsgShort("请填写完整信息");
        } else try {
            PhoneNumberUtil.PhoneType type = PhoneNumberUtil.checkNumber(phone).getType();
            if (type == PhoneNumberUtil.PhoneType.INVALIDPHONE) {
                // 不是手机或者固话
                showToastMsgShort("电话号码不合法");
            } else if (!StringUtils.IDCardValidate(uuid).equals("")) {
                showToastMsgShort("身份证号不合法");
            } else {
                apply();// 申请
            }
        } catch (ParseException e) {
            e.printStackTrace();
            showToastMsgShort("身份证号不合法");
        }
    }

    /**
     * 显示对话框  添加预约的人数
     */
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.add_item_dialog, null);
        builder.setView(view);
        final EditText et_name = (EditText) view.findViewById(R.id.et_name);
        final EditText et_ic_card_number = (EditText) view.findViewById(R.id.et_ic_card_number);
        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!checkEditText()) {
                    showToastMsgShort("请先填写已有表单再添加");
                    disableClose((Dialog) dialog);
                    return;
                }
                String name = et_name.getText().toString().trim();
                String ic_card_number = et_ic_card_number.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                if (ic_card_number.equals(uuid) || isAlreadyAdd(ic_card_number)) {
                    showToastMsgShort("该身份证已添加");
                    disableClose((Dialog) dialog);
                    return;
                }
                String result = UIUtil.checkInfoComplete(name, ic_card_number, phone);
                Log.i(TAG, "check result : " + result);
                if (result.equals("")) {
                    // check全通过  添加成功
                    setAddedUIAndData(name, ic_card_number, phone);
                    enableClose((Dialog) dialog);
                    dialog.dismiss();
                } else {
                    showToastMsgShort(result);
                    disableClose((Dialog) dialog);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enableClose((Dialog) dialog);
            }
        });
        AlertDialog _dialog = builder.create();
        _dialog.setCancelable(false);
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.show();
    }

    /**
     * 设置添加的ui和数据
     *
     * @param name
     * @param ic_card_number
     * @param phone
     */
    private void setAddedUIAndData(String name, String ic_card_number, String phone) {
        ll_added.setVisibility(View.VISIBLE);
        if (familyList.size() < 4) {
            FamilyBean bean = addItemToList(name, ic_card_number, phone);
            if (addBookAdapter == null) {
                Log.i(TAG, bean.toString() + "----" + familyList.get(0).toString());
                addBookAdapter = new AddBookAdapter(context, familyList);
                recycler_view.setLayoutManager(new MyLinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                recycler_view.addItemDecoration(new DividerItemDecoration(context,
                        LinearLayoutManager.VERTICAL));
                recycler_view.setAdapter(addBookAdapter);
                Log.i(TAG, addBookAdapter.getItemCount() + "");
            } else {
                addBookAdapter.insert(bean);
            }
        } else {
            showToastMsgShort("最多添加4人");
        }
    }

    /**
     * 是否已经添加
     *
     * @param ic_card_number
     * @return
     */
    private boolean isAlreadyAdd(String ic_card_number) {
        boolean isAlreadyAdded = false;
        if (familyList.size() > 0) {
            for (FamilyBean bean : familyList) {
                if (ic_card_number.equals(bean.getUuid())) {
                    isAlreadyAdded = true;
                    break;
                }
            }
        }
        return isAlreadyAdded;
    }

    /**
     * 添加item数据到集合
     *
     * @param name
     * @param ic_card_number
     * @param phone
     */
    private FamilyBean addItemToList(String name, String ic_card_number,
                                     String phone) {
        FamilyBean familyBean = new FamilyBean();
        familyBean.setName(name);
        familyBean.setUuid(ic_card_number);
        familyBean.setPhone(phone);
        familyList.add(familyBean);
        return familyBean;
    }

    public class AddBookAdapter extends RecyclerView.Adapter<AddBookAdapter.MyViewHolder> {

        private static final String TAG = "AddBookAdapter";
        private List<FamilyBean> list;
        private Context mContext;

        public AddBookAdapter(Context context, List<FamilyBean> list) {
            this.mContext = context;
            this.list = new ArrayList<>();
            this.list.addAll(list);
        }

        @Override
        public AddBookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Log.i(TAG, "onCreateViewHolder()");
            return new AddBookAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.add_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final AddBookAdapter.MyViewHolder holder, int position) {
//            Log.i(TAG, list.get(position).toString());
            holder.tv_name.setText(list.get(position).getName());
            holder.tv_id_card_number.setText(getResources().getString(R.string.id_num) + list.get(position).getUuid());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(holder.getAdapterPosition());
                    familyList.remove(holder.getAdapterPosition());
                    AddBookAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                    if (familyList.size() == 0) {
                        // 删除第0个即隐藏ll_add
                        ll_added.setVisibility(View.GONE);
                    }
                }
            });
        }

        /**
         * 添加item
         *
         * @param familyBean
         */
        public void insert(FamilyBean familyBean) {
//            Log.i(TAG, "insert book info");
            list.add(familyBean);
            notifyItemInserted(getItemCount());
        }

        @Override
        public int getItemCount() {
//            Log.i(TAG, "getItemCount() == " + list.size());
            return list.size();
        }

        public void removeAllItem() {
            list.clear();
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.iv_delete)
            ImageView iv_delete;
            @BindView(R.id.tv_name)
            TextView tv_name;
            @BindView(R.id.tv_id_card_number)
            TextView tv_id_card_number;

            public MyViewHolder(View itemView) {
                super(itemView);
//                Log.i("MyViewHolder", "MyViewHolder");
                ButterKnife.bind(this, itemView);
            }
        }
    }

    private void disableClose(Dialog dialog) {
        isDisable = true;
        try {
            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void enableClose(Dialog dialog) {
        isDisable = false;
        try {
            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, TAG + "----onDestroyView --");
        super.onDestroyView();
        // 保存数据
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, TAG + "----onViewStateRestored --" + savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
        restorView();
    }

    /**
     * 恢复view状态（这里主要是添加的申请条目）
     */
    private void restorView() {
        Log.i(TAG, TAG + "----restorView --" + familyList.size());
        if (familyList.size() > 0 && ll_added.getVisibility() != View.VISIBLE) {
            Log.i(TAG, TAG + "----restorView --" + familyList.size());
            ll_added.setVisibility(View.VISIBLE);
            if (addBookAdapter != null) {
                addBookAdapter = new AddBookAdapter(context, familyList);
                recycler_view.setLayoutManager(new MyLinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                recycler_view.addItemDecoration(new DividerItemDecoration(context,
                        LinearLayoutManager.VERTICAL));
                recycler_view.setAdapter(addBookAdapter);
                Log.i(TAG, addBookAdapter.getItemCount() + "");
            }
        }
    }
}
