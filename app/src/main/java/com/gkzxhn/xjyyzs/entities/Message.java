package com.gkzxhn.xjyyzs.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author: Huang ZN
 * Date: 2016/9/30
 * Email:943852572@qq.com
 * Description:sqlite entity
 */
@Entity
public class Message {

    @Id(autoincrement = true)
    private long id;
    private String account;
    private String fromaccount;
    private String content;
    private long time;
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getFromaccount() {
        return this.fromaccount;
    }
    public void setFromaccount(String fromaccount) {
        this.fromaccount = fromaccount;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 794508677)
    public Message(long id, String account, String fromaccount, String content,
            long time) {
        this.id = id;
        this.account = account;
        this.fromaccount = fromaccount;
        this.content = content;
        this.time = time;
    }
    @Generated(hash = 637306882)
    public Message() {
    }

}
