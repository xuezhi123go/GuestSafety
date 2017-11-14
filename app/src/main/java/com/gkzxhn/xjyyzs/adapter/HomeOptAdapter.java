package com.gkzxhn.xjyyzs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.gkzxhn.xjyyzs.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/19.
 * function: 主页顶部切换适配器
 */
public class HomeOptAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public HomeOptAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * 添加fragment
     * @param fragment
     * @param title
     */
    public void addFragment(BaseFragment fragment, String title){
        mFragments.add(fragment);
        mTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}

