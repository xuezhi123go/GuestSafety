package com.gkzxhn.xjyyzs.fragments;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.gkzxhn.xjyyzs.R;
import com.gkzxhn.xjyyzs.adapter.HomeOptAdapter;
import com.gkzxhn.xjyyzs.base.BaseFragment;
import com.gkzxhn.xjyyzs.view.NoScrollerViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function:home fragment
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.tl_home_opt)
    TabLayout tl_home_opt;
    @BindView(R.id.vp_home_opt)
    NoScrollerViewPager vp_home_opt;

    private SearchFragment searchFragment;
    private BookFragment bookFragment;
    private MsgFragment msgFragment;
    private GuestFragment guestFragment;

    private HomeOptAdapter adapter;

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        initViewPager();
        vp_home_opt.setScroller(false);
        tl_home_opt.setupWithViewPager(vp_home_opt);
        tl_home_opt.setTabMode(TabLayout.MODE_FIXED);
        tl_home_opt.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    searchFragment.getData(0);
                }
                vp_home_opt.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        if (adapter == null) {
            adapter = new HomeOptAdapter(getActivity().getSupportFragmentManager());
            searchFragment = new SearchFragment();
            bookFragment = new BookFragment();
            msgFragment = new MsgFragment();
            guestFragment = new GuestFragment();

            adapter.addFragment(bookFragment, "预约");
            adapter.addFragment(searchFragment, "查询");
            adapter.addFragment(msgFragment, "消息");
            adapter.addFragment(guestFragment, "来访");
            vp_home_opt.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
