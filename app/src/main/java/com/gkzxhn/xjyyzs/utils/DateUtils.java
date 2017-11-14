package com.gkzxhn.xjyyzs.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/22.
 * function:日期工具类
 */

public class DateUtils {

    /**
     * 返回当前日之后的n天日期(yyyy-MM-dd),过滤掉周末的
     * @param n
     * @return
     */
    public static List<String> afterNDay(int n){
        List<String> list = new ArrayList<>();
        for(int i = 1; i <= n; i++) {
            Calendar c = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            c.setTime(new Date());
            c.add(Calendar.DATE, i);
            Date d2 = c.getTime();
            String s = df.format(d2);
            if(!(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && !(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                list.add(s);
            }
        }
        return list;
    }

    /**
     * 格式化日期
     * @param format  格式化后的格式
     * @param ms
     * @return
     */
    public static String formatDate(String format, long ms){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(ms);
        return simpleDateFormat.format(date);
    }

    /**
     * 反格式化  返回毫秒值
     * @param format 日期格式
     * @param date 日期字符串
     * @return
     */
    public static long reFormatDate(String format, String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        long ms = 0;
        try {
           ms = dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ms;
    }

    /**
     * 日期格式化  从xxxx/x/x 到xxxx-x(0x)-x(0x)
     * @param date
     * @return
     */
    public static String dateFormat(String date){
        String newDate = date.replace("/", "-");
        String[] dates = newDate.split("-");
        if(dates[1].length() == 1){
            dates[1] = "0" + dates[1];
        }
        if(dates[2].length() == 1){
            dates[2] = "0" + dates[2];
        }
        return dates[0] + "-" + dates[1] + "-" + dates[2];
    }

    /**
     * 返回时间字符串 若超过24小时则带日期  不超过24小时只返回hh:mm
     * @param ms
     * @return
     */
    public static String getTimeString(long ms){
        long current = System.currentTimeMillis();
        long day = 1000L * 60L * 60L * 24L;// 一天
        long twoDay = day * 2L;// 两天
        long threeDay = day * 3L;// 三天
        String format = "HH:mm";
        long timeDiff = current - ms;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(ms);
        if(timeDiff >= 0 && timeDiff < day){
            // 一天内显示HH:mm
            return simpleDateFormat.format(date);
        }else if(timeDiff >= day && timeDiff < twoDay){
            // 大于一天小于两天显示 昨天 HH:mm
            return "昨天 " + simpleDateFormat.format(date);
        }else if(timeDiff >= twoDay && timeDiff < threeDay){
            // 大于两天小于三天显示 前天 HH:mm
            return "前天 " + simpleDateFormat.format(date);
        }else {
            // 超过三天显示具体日期
            format = "yyyy-MM-dd HH:mm";
            simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return simpleDateFormat.format(date);
        }
    }
}
