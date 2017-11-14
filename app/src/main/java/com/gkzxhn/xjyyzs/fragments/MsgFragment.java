package com.gkzxhn.xjyyzs.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.adapter.MsgAdapter;
import com.gkzxhn.xjyyzs.app.AppBus;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.entities.Message;
import com.gkzxhn.xjyyzs.entities.events.ClearMsg;
import com.gkzxhn.xjyyzs.entities.events.ClearMsgResult;
import com.gkzxhn.xjyyzs.entities.events.SystemMsg;
import com.gkzxhn.xjyyzs.utils.DBUtils;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.UIUtil;
import com.squareup.otto.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:消息
 */
public class MsgFragment extends BaseFragment {

    @BindView(R.id.iv_no_msg) ImageView iv_no_msg;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    private List<Message> messageList;
    private MsgAdapter msgAdapter;
    private static final String TAG = "MsgFragment";

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_msg, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        recycler_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearMsg();
                return true;
            }
        });
    }

    /**
     * 清除全部消息
     */
    private void clearMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("确定清除全部消息吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isSuccess = DBUtils.getInstance(context).deleteAll();
                if(isSuccess){
                    messageList.clear();
                    recycler_view.setVisibility(View.GONE);
                    iv_no_msg.setVisibility(View.VISIBLE);
                    showToastMsgShort("清除成功");
                }else {
                    showToastMsgShort("清除失败，请稍后再试！");
                }
                dialog.dismiss();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppBus.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        onLoadData(null);
    }

    /**
     * 加载数据
     */
    @Subscribe
    public void onLoadData(SystemMsg msg){
        ProgressDialog dialog = null;
        if(isVisible()) {
            dialog = UIUtil.showProgressDialog(getActivity(), "", "加载中", false);
        }
        messageList = DBUtils.getInstance(getActivity()).query();
        Log.d("query msg result list size : " + messageList.size());
        if(messageList.size() == 0){
            recycler_view.setVisibility(View.GONE);
            iv_no_msg.setVisibility(View.VISIBLE);
        }else {
            Collections.reverse(messageList);
            iv_no_msg.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
            msgAdapter = new MsgAdapter(getActivity(), messageList);
            recycler_view.setAdapter(msgAdapter);
        }
        if(isVisible() && dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 清除全部系统消息
     * @param msg
     */
    @Subscribe
    public void clearMsg(ClearMsg msg){
        boolean isSuccess = DBUtils.getInstance(context).deleteAll();
        AppBus.getInstance().post(new ClearMsgResult(isSuccess));
        onLoadData(null);
    }
}
