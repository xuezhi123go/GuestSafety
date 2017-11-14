package com.gkzxhn.xjyyzs.utils;

import android.content.Context;

import com.gkzxhn.xjyyzs.entities.Message;
import com.gkzxhn.xjyyzs.greendao.gen.DaoMaster;
import com.gkzxhn.xjyyzs.greendao.gen.DaoSession;
import com.gkzxhn.xjyyzs.greendao.gen.MessageDao;

import java.util.List;

/**
 * Author: Huang ZN
 * Date: 2016/9/30
 * Email:943852572@qq.com
 * Description:
 */

public class DBUtils {

    private static DBUtils instance;
    private static MessageDao msgDao;
    private static Context mContext;

    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static DBUtils getInstance(Context context) {
        mContext = context;
        if (instance == null)
            instance = new DBUtils();
        if (msgDao == null) {
            initDao();
        }
        return instance;
    }

    /**
     * 初始化数据库
     */
    private static void initDao() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(mContext, "yyzs", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        msgDao = daoSession.getMessageDao();
    }

    /**
     * 插入数据
     *
     * @param msg
     * @return success return true
     */
    public boolean insert(Message msg) {
        if (msgDao != null) {
            msgDao.insert(msg);
            return true;
        }
        return false;
    }

    /**
     * 查询
     *
     * @return query result list
     */
    public List<Message> query() {
        return msgDao.queryBuilder()
                .where(MessageDao.Properties.Account.eq(SPUtil.get(mContext, "cloudId", "")))
                .orderAsc(MessageDao.Properties.Time)
                .build()
                .list();
    }

    /**
     * 修改数据
     *
     * @param msg
     * @return success return true
     */
    public boolean update(Message msg) {
        Message message = msgDao.queryBuilder().where(MessageDao
                .Properties.Id.eq(msg.getId())).build().unique();
        if (message != null) {
            msgDao.update(msg);
            return true;
        }
        return false;
    }

    /**
     * 删除一条
     *
     * @param id
     * @return success return true
     */
    public boolean deleteOne(int id) {
        Message msg = msgDao.queryBuilder().where(MessageDao.Properties.Id.eq(id))
                .build().unique();
        if (msg != null) {
            msgDao.delete(msg);
            return true;
        }
        return false;
    }

    /**
     * 删除全部
     *
     * @return
     */
    public boolean deleteAll() {
        if (msgDao != null) {
            msgDao.deleteAll();
            return true;
        }
        return false;
    }
}
