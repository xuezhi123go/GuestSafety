package com.gkzxhn.xjyyzs.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gkzxhn.xjyyzs.utils.ToastUtil;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:Fragment基类、所有Fragment须继承此类
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    //view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }

    //填充数据
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 子类必须实现  即布局
     *
     * @return
     */
    protected abstract View initView();

    /**
     * 子类要想显示动态数据必须实现
     */
    protected abstract void initData();

    /**
     * 弹出Toast 显示时长short
     */
    protected void showToastMsgShort(String pMsg) {
        ToastUtil.showShortToast(getActivity(), pMsg);
    }

    /**
     * 弹出Toast 显示时长long
     */
    protected void showToastMsgLong(String pMsg) {
        ToastUtil.showLongToast(getActivity(), pMsg);
    }
}
