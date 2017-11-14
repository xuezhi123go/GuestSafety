package com.gkzxhn.xjyyzs.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.adapter.SearchResultAdapter;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.inters.OnSearchResultCallBack;
import com.gkzxhn.xjyyzs.requests.bean.ApplyResult;
import com.gkzxhn.xjyyzs.requests.bean.SearchResultBean;
import com.gkzxhn.xjyyzs.requests.methods.RequestMethods;
import com.gkzxhn.xjyyzs.utils.DateUtils;
import com.gkzxhn.xjyyzs.utils.Log;
import com.gkzxhn.xjyyzs.utils.StringUtils;
import com.gkzxhn.xjyyzs.view.decoration.DividerItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:历史查询
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener, OnSearchResultCallBack {

    private static final String TAG = "SearchFragment";
    private static final String[] STATUS = {"已通过", "未通过"};
    @BindView(R.id.sp_status)
    Spinner sp_status;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.bt_search)
    Button bt_search;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.tv_no_result)
    TextView tv_no_result;// 没有结果

    private ProgressDialog current_dialog;// 获取当天数据对话框
    private List<ApplyResult.AppliesBean> data;
    private List<ApplyResult.AppliesBean> searchData;
    private SearchResultAdapter adapter;// 结果列表适配器

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_search, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        sp_status.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_item, STATUS));
        setDateText();// 设置两个日期文本
        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler_view.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 设置时间段查询的两个日期文本
     */
    private void setDateText() {
        tv_start_date.setText(DateUtils.formatDate("yyyy-MM-dd", System.currentTimeMillis()));
        tv_end_date.setText(DateUtils.formatDate("yyyy-MM-dd", System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 7));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                showDatePicker(tv_start_date);
                break;
            case R.id.tv_end_date:
                showDatePicker(tv_end_date);
                break;
            case R.id.bt_search:
                search();// 查询
                break;
        }
    }

    /**
     * 查询
     */
    private void search() {
        String start_time1 = DateUtils.dateFormat(tv_start_date.getText().toString());
        String end_time1 = DateUtils.dateFormat(tv_end_date.getText().toString());
        long thirtyDays1 = 1000L * 60L * 60L * 24L * 30L;
        long endMs1 = DateUtils.reFormatDate("yyyy-MM-dd", end_time1);
        long startMs1 = DateUtils.reFormatDate("yyyy-MM-dd", start_time1);
        if (endMs1 - startMs1 > thirtyDays1) {
            showToastMsgLong("日期区间不能超过30天");
            return;
        }
        if (endMs1 < startMs1) {
            showToastMsgLong("开始日期不能在结束日期之后");
            return;
        }
        getSearchResult(0, start_time1, end_time1);
    }

    /**
     * 时间段查询
     *
     * @param start_time
     * @param end_time
     */
    private void getSearchResult(int type, String start_time, String end_time) {
        initShowProgressDialog();
        RequestMethods.getSearchResult(getActivity(), getRequestParameters(type, start_time, end_time),
                new Subscriber<SearchResultBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getSearchResult --> onError : " + e.getMessage());
                        showGetFailed("查询失败！");
                    }

                    @Override
                    public void onNext(SearchResultBean result) {
                        if (result.getApplies().size() > 0) {
                            processDate(result);// 矫正数据
                            String ss = new Gson().toJson(result);
                            Log.i(ss);
                        } else {
                            showGetFailed("没有数据");
                            Log.i(result.toString());
                            tv_no_result.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * 获取请求参数map
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private Map<String, String> getRequestParameters(int type,
                                                     String startTime, String endTime) {
        Map<String, String> map = new HashMap<>();
        map.put("start", startTime);
        map.put("end", endTime);
        if (type == 0) {
            if (sp_status.getSelectedItemPosition() == 1) {
                map.put("isPass", "DENIED");
            } else if (sp_status.getSelectedItemPosition() == 0) {
                map.put("isPass", "PASSED");
            } else {
                map.put("isPass", "PASSED");
            }
        } else {
            map.put("isPass", "PASSED");
        }
        return map;
    }

    /**
     * 矫正数据  把SearchResultBean转换成卡片item需要的ApplyResult里的AppliesBean形式
     *
     * @param result
     */
    private void processDate(SearchResultBean result) {
        searchData = new ArrayList<>();
        List<SearchResultBean.AppliesBean> beanList = result.getApplies();
        Observable.from(beanList).lift(new Observable.Operator<ApplyResult.AppliesBean, SearchResultBean.AppliesBean>() {
            @Override
            public Subscriber<? super SearchResultBean.AppliesBean> call(final Subscriber<? super ApplyResult.AppliesBean> subscriber) {
                return getAppliesBeanSubscriber(subscriber);
            }
        }).subscribe(new Subscriber<ApplyResult.AppliesBean>() {
            @Override
            public void onCompleted() {
                if (searchData != null)
                    setDataList(searchData); // 设置列表数据
                showGetSuccess(); // 获取成功
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "processDate --> onError : " + e.getMessage());
                showGetFailed("查询失败，请稍后再试");
            }

            @Override
            public void onNext(ApplyResult.AppliesBean appliesBean) {
                if (appliesBean != null) {
                    searchData.add(appliesBean);
                } else {
                    Log.e(TAG, "processDate --> onNext : appliesBean is Null");
                }
            }
        });
    }

    /**
     * 获取转换后的数据
     * 将原来的ApplyResult.AppliesBean转换为SearchResultBean.AppliesBean形式
     *
     * @param subscriber
     * @return
     */
    @NonNull
    private Subscriber<SearchResultBean.AppliesBean> getAppliesBeanSubscriber(
            final Subscriber<? super ApplyResult.AppliesBean> subscriber) {
        return new Subscriber<SearchResultBean.AppliesBean>() {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "getAppliesBeanSubscriber --> onError : " + e.getMessage());
                subscriber.onError(e);
            }

            @Override
            public void onNext(SearchResultBean.AppliesBean appliesBean) {
                if (appliesBean.getApplication() != null && appliesBean.getApplication().size() > 0) {
                    for (SearchResultBean.AppliesBean.ApplyBean bean : appliesBean.getApplication()) {
                        ApplyResult.AppliesBean.ApplyBean applyBean = getApplyBean(bean);
                        subscriber.onNext(new ApplyResult.AppliesBean(appliesBean.getName(),
                                appliesBean.getUuid(), appliesBean.getPhone(), applyBean));
                    }
                } else {
                    subscriber.onNext(null);
                    Log.e(TAG, "getAppliesBeanSubscriber --> onNext : appliesBean is Null");
                }
            }
        };
    }

    /**
     * 第二步转换数据格式
     * 将SearchResultBean.AppliesBean.ApplyBean转换为ApplyResult.AppliesBean.ApplyBean
     * 传到最终subscribe的onNext中消费
     *
     * @param bean
     * @return
     */
    @NonNull
    private ApplyResult.AppliesBean.ApplyBean getApplyBean(
            SearchResultBean.AppliesBean.ApplyBean bean) {
        ApplyResult.AppliesBean.ApplyBean.FeedbackBean feedbackBean =
                new ApplyResult.AppliesBean.ApplyBean.FeedbackBean();
        feedbackBean.setContent(bean.getFeedback().getContent());
        feedbackBean.setFrom(bean.getFeedback().getFrom());
        feedbackBean.setIsPass(StringUtils.getUpCaseStatus(bean.getFeedback().getIsPass()));
        feedbackBean.setMeetingTime(bean.getFeedback().getMeetingTime());
        feedbackBean.setPrison(bean.getFeedback().getPrison());
        feedbackBean.setSfs(bean.getFeedback().getSfs());
        return new ApplyResult.AppliesBean.ApplyBean(
                bean.getApplyDate(), bean.get_id(), feedbackBean);
    }

    /**
     * 初始化显示进度条对话框
     */
    private void initShowProgressDialog() {
        if (current_dialog == null) {
            current_dialog = new ProgressDialog(getActivity());
            current_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            current_dialog.setCancelable(false);
            current_dialog.setCanceledOnTouchOutside(false);
        }
        current_dialog.show();
    }

    /**
     * 显示datePicker
     *
     * @param tv
     */
    private void showDatePicker(final TextView tv) {
        DatePickerDialog start_picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        start_picker.show();
    }

    @Override
    public void getData(int type) {// type 0 是tabLayout选中当前页 1 是下拉刷新
        if ((recycler_view != null && recycler_view.getAdapter() == null) || type == 1) {
            Log.i(TAG, "go to get data");
            initShowProgressDialog();
            String date = DateUtils.formatDate("yyyy-MM-dd", System.currentTimeMillis());
            getSearchResult(1, date, date);
        }
    }

    /**
     * 设置列表数据
     */
    private void setDataList(List<ApplyResult.AppliesBean> list) {
        Log.i("setDataList:==" + list.size());
        if (list.size() > 0) {
            tv_no_result.setVisibility(View.GONE);
            adapter = new SearchResultAdapter(getActivity(), list);
            recycler_view.setAdapter(adapter);
            recycler_view.setVisibility(View.VISIBLE);
        } else {
            if (recycler_view.getAdapter() != null && recycler_view.getChildCount() > 0) {
                recycler_view.setVisibility(View.GONE);
            }
            tv_no_result.setVisibility(View.VISIBLE);
            showToastMsgShort("没有数据");
        }

//        if(srl_refresh.isRefreshing()){
//            srl_refresh.setRefreshing(false);
//            showToastMsgShort("刷新成功");
//
//        }
    }

    /**
     * 获取成功
     */
    private void showGetSuccess() {
        if (current_dialog.isShowing())
            current_dialog.dismiss();
    }

    /**
     * 获取失败
     */
    private void showGetFailed(String titleText) {
        if (current_dialog.isShowing())
            current_dialog.dismiss();
//        if(srl_refresh.isRefreshing())
//            srl_refresh.setRefreshing(false);
        showToastMsgShort(titleText);
    }
}
