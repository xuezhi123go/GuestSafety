package com.gkzxhn.xjyyzs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.requests.bean.ApplyResult;
import com.gkzxhn.xjyyzs.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:搜索结果列表适配器
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder> {

    private final String TAG = "SearchResultAdapter";
    private Context context;
    private List<ApplyResult.AppliesBean> list;

    private String content = "";

    public static final int DELAY = 100;
    private int mLastPosition = -1;

    public SearchResultAdapter(Context context, List<ApplyResult.AppliesBean> beanList){
        this.context = context;
        list = beanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.search_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_id_card_number.setText(StringUtils.decryptUuid(list.get(position).getUuid()));
        holder.tv_apply_date.setText(list.get(position).getApplication().getApplyDate());
        holder.tv_apply_status.setText(list.get(position).getApplication().getFeedback().getIsPass()+" 原因："+list.get(position).getApplication().getFeedback().getContent());
        if(list.get(position).getApplication().getFeedback().getIsPass().equals("已拒绝")){
            holder.ll_meeting_date.setVisibility(View.GONE);
        }else {
            holder.tv_meeting_date.setText(list.get(position).getApplication().getFeedback().getMeetingTime());
        }
        holder.tv_apply_name.setText(list.get(position).getName());
        holder.tv_phone.setText(list.get(position).getPhone());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showShortToast(context, "别点啦...");
            }
        });
//        setItemAnim(holder.card_view, position);
    }

    /**
     * item加载动画
     * @param view
     * @param position
     */
    public void setItemAnim(final View view, int position){
        final Context context = view.getContext();
        if (position > mLastPosition){
            view.setAlpha(0);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {
                            view.setAlpha(1);
                        }

                        @Override public void onAnimationEnd(Animation animation) {}

                        @Override public void onAnimationRepeat(Animation animation) {}
                    });
                    view.startAnimation(animation);
                }
            }, DELAY * position);
            mLastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.card_view) CardView card_view;
        @BindView(R.id.tv_apply_name) TextView tv_apply_name;
        @BindView(R.id.tv_id_card_number) TextView tv_id_card_number;
        @BindView(R.id.tv_apply_date) TextView tv_apply_date;
        @BindView(R.id.tv_apply_status) TextView tv_apply_status;
        @BindView(R.id.tv_meeting_date) TextView tv_meeting_date;
        @BindView(R.id.tv_phone) TextView tv_phone;
        @BindView(R.id.ll_meeting_date) LinearLayout ll_meeting_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
