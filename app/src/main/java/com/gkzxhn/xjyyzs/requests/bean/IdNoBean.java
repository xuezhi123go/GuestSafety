package com.gkzxhn.xjyyzs.requests.bean;

/**
 * Created by Xuezhi
 * on 2017/5/25.
 */

public class IdNoBean {
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "IdNoBean{" +
                "uuid='" + uuid + '\'' +
                '}';
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
