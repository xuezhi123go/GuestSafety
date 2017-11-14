package com.gkzxhn.xjyyzs.entities.events;

import com.gkzxhn.xjyyzs.requests.bean.FamilyBean;

/**
 * Author: Huang ZN
 * Date: 2016/11/9
 * Email:943852572@qq.com
 * Description:
 */
public class ItemRemovedEvent {

    private int position;
    private FamilyBean bean;

    public ItemRemovedEvent(int position, FamilyBean bean){}

    public FamilyBean getBean() {
        return bean;
    }

    public void setBean(FamilyBean bean) {
        this.bean = bean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
