package com.gkzxhn.xjyyzs.app;

import com.squareup.otto.Bus;

/**
 * Author: Huang ZN
 * Date: 2016/10/8
 * Email:943852572@qq.com
 * Description:otto app bus
 */

public class AppBus extends Bus {

    private static AppBus bus;

    /**
     * get instance
     * @return appBus instance
     */
    public static AppBus getInstance(){
        if (bus == null){
            bus = new AppBus();
        }
        return bus;
    }
}
