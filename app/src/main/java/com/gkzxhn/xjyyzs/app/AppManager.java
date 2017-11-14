package com.gkzxhn.xjyyzs.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by haungzhengneng on 2016/3/21.
 * Activity管理类
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager(){}

    /**
     * 单一实例
     * @return
     */
    public static AppManager getInstance(){
        if (instance == null){
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     * @param activity
     */
    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     * @return
     */
    public Activity getCurrentActivity(){
        if (activityStack == null)
            return null;
        if (activityStack.empty()){
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity(){
        Activity activity = activityStack.lastElement();
        if (activity != null){
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的activity
     * @param cla
     */
    public void finishActivity(Class<?> cla){
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cla)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private void finishAllActivity(){
        for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出App
     * @param context
     */
    public void ExitApp(Context context){
        try {
            finishAllActivity();
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
