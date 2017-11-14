package com.gkzxhn.xjyyzs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.entities.Message;
import com.gkzxhn.xjyyzs.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: Huang ZN
 * Date: 2016/10/8
 * Email:943852572@qq.com
 * Description:
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder> {

    private static final String TAG = "MsgAdapter";
    private Context mContext;
    private List<Message> list;

    public MsgAdapter(Context context, List<Message> messageList) {
        this.mContext = context;
        this.list = messageList;
    }

    @Override
    public MsgAdapter.MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgViewHolder(LayoutInflater.from(mContext).inflate(R.layout.msg_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MsgAdapter.MsgViewHolder holder, int position) {
//        holder.tv_msg_title.setText();
        holder.tv_msg_receive_time.setText(DateUtils.getTimeString(list.get(position).getTime()));
        holder.tv_msg_content.setText(TextUtils.isEmpty(list.get(position).getContent()) ? "无消息内容" : list.get(position).getContent());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToDo 弹出对话框显示消息详细
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView card_view;
        @BindView(R.id.tv_msg_title)
        TextView tv_msg_title;
        @BindView(R.id.tv_msg_receive_time)
        TextView tv_msg_receive_time;
        @BindView(R.id.tv_msg_content)
        TextView tv_msg_content;

        public MsgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
