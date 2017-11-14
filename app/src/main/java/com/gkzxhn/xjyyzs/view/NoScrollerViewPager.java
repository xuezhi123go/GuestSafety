package com.gkzxhn.xjyyzs.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: Huang ZN
 * Date: 2016/10/8
 * Email:943852572@qq.com
 * Description:自定义的viewpager
 */

public class NoScrollerViewPager extends ViewPager {

    private boolean isScroller = true;

    public NoScrollerViewPager(Context context) {
        super(context);
    }

    public NoScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isScroller){
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isScroller){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 获取是否可以滑动切换条目
     * @return
     */
    public boolean isScrollerEnable(){
        return isScroller;
    }

    /**
     * 设置是否可滑动切换条目
     * @param isScroller
     */
    public void setScroller(boolean isScroller){
        this.isScroller = isScroller;
    }
}
