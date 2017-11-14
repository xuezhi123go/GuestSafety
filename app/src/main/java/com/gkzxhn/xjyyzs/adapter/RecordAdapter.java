package com.gkzxhn.xjyyzs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.entities.RecordEntity;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by Raleigh.Luo on 17/6/8.
 * <p>
 * 访客列表adapter
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHoler> {

    private Context context;

    private List<RecordEntity> mDatas = new ArrayList<>();

    private Map<String, String> mCardDate = new HashMap<>();//cardnumb,date 2017-06-05

    public RecordAdapter(Context context) {
        this.context = context;
    }

    public void loadItem(String name, String cardnumber, String relate, String prisionId) {
        //当天只记录一次
        if (!(mCardDate.containsKey(cardnumber) && mCardDate.containsValue(getDateFromTimeInMillis(System.currentTimeMillis(),
                null)))) {//已经来访了，就不存了，一天只存第一次的

            mDatas.add(0, new RecordEntity(name, cardnumber, relate, prisionId));

            mCardDate.put(cardnumber, getDateFromTimeInMillis(mDatas.get(0).getTime(), null));
            notifyDataSetChanged();//动态更新listview
        }

    }

    public void clear() {
        mCardDate.clear();
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHoler onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_item_layout, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(final ViewHoler viewHoler, final int i) {
        final RecordEntity entity = mDatas.get(i);
        viewHoler.tvName.setText(entity.getName());
        viewHoler.tvTime.setText(getDateFromTimeInMillis(entity.getTime(), new SimpleDateFormat("MM-dd HH:mm")));
        //身份证省略中间
        viewHoler.tvCardNumber.setText(entity.getCardNumber().replaceAll("(\\d{4})\\d{10}(\\d{3}[0-9a-zA-Z])", "$1****$2"));
        viewHoler.tvRelate.setText(entity.getRelate());
        viewHoler.cbChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                viewHoler.cbChoice.setChecked(entity.isClick());
            }
        });
        viewHoler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!entity.isClick()) {
                    entity.setClick(true);
                    viewHoler.cbChoice.setChecked(true);
                }
//                if(Constant.IS_REMOTE_INTERVIEW)remoteInterview(entity.getPrisionId());
            }
        });
        viewHoler.cbChoice.setChecked(entity.isClick());
    }

    private String getDateFromTimeInMillis(long timeInMillis, SimpleDateFormat df) {
        String result = "";
        if (timeInMillis > 0) {
            try {
                Date date = new Date(timeInMillis);
                //英文格式时间格式化
                if (df == null) df = new SimpleDateFormat("yyyy-MM-dd");
                result = df.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        private TextView tvName, tvTime, tvCardNumber, tvRelate;
        private CheckBox cbChoice;

        public ViewHoler(View itemView) {
            super(itemView);
            cbChoice = (CheckBox) itemView.findViewById(R.id.record_item_layout_checkbox);
            tvCardNumber = (TextView) itemView.findViewById(R.id.record_item_layout_tv_cardnumber);
            tvRelate = (TextView) itemView.findViewById(R.id.record_item_layout_tv_relate);
            tvName = (TextView) itemView.findViewById(R.id.record_item_layout_tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.record_item_layout_tv_time);
        }
    }

    /**
     * 启动远程会见
     */
    private void remoteInterview(String prisionId) {
        if (prisionId != null && prisionId.length() > 0) {
            ToastUtil.showLongToast(context, "正在启动远程会见");
            JSONObject params = new JSONObject();
            try {
                params.put("prisonerId", prisionId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), params.toString());
            RequestMethods.remoteInterview(body, new Subscriber<Object>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.showLongToast(context, "已成功启动远程会见");
                }

                @Override
                public void onNext(Object bookResult) {
                    ToastUtil.showLongToast(context, "已成功启动远程会见");
                }
            });
        }

    }
}
