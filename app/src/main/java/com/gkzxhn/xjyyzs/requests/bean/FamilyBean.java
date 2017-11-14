package com.gkzxhn.xjyyzs.requests.bean;

/**
 * Author: Huang ZN
 * Date: 2016/11/9
 * Email:943852572@qq.com
 * Description:
 */

public class FamilyBean {
    private String uuid;
    private String phone;
    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Family{" +
                "uuid='" + uuid + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
