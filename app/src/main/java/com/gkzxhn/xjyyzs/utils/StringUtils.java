package com.gkzxhn.xjyyzs.utils;


import android.content.Context;
import android.telephony.TelephonyManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/7/22.
 * function:字符串工具类
 */

public class StringUtils {

    /**
     * 判断是否是手机号
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-3,5-9])|(14[5,7])|(17[0,6-8])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 功能：身份证的有效验证
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    public static String IDCardValidate(String IDStr) throws ParseException {
        String lowerCaseIdStr = IDStr.toLowerCase();
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // 号码的长度 15位或18位
        if (lowerCaseIdStr.length() != 15 && lowerCaseIdStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }

        // 数字 除最后一位都为数字
        if (lowerCaseIdStr.length() == 18) {
            Ai = lowerCaseIdStr.substring(0, 17);
        } else if (lowerCaseIdStr.length() == 15) {
            Ai = lowerCaseIdStr.substring(0, 6) + "19" + lowerCaseIdStr.substring(6, 15);
        }
        if (!isNumeric(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }

        // 出生年月是否有效
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (!isDataFormat(strYear + "-" + strMonth + "-" + strDay)) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(
                strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }

        // 地区码是否有效
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }

        // 判断最后一位的值
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (lowerCaseIdStr.length() == 18) {
            if (!Ai.equals(lowerCaseIdStr)) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        return "";
    }

    /**
     * 功能：判断字符串是否为数字
     * @param str
     * @return  true为全数字  false为含其它字符
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 功能：设置地区编码
     * @return Hashtable 对象
     */
    public static Hashtable GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**验证日期字符串是否是YYYY-MM-DD格式
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str){
        boolean flag=false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(" +
                "1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/" +
                "\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9]))))" +
                ")|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(" +
                "1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/" +
                "\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]" +
                "))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1=Pattern.compile(regxStr);
        Matcher isNo=pattern1.matcher(str);
        if(isNo.matches()){
            flag=true;
        }
        return flag;
    }

    /**
     * 把中文状态转换为大写的英文
     * @param status
     * @return
     */
    public static String getUpCaseStatus(String status){
        String upCaseStatus = "";
        if(status.equals("未处理")){
            status = "PENDING";
        }else if(status.equals("已通过")){
            status = "PASSED";
        }else if(status.equals("已拒绝")){
            status = "DENIED";
        }
        return status;
    }

    /**
     * 身份证号解密
     *  0-->*   1-->&  2-->%  3-->#  4-->@ 5-->Q 6-->P 7-->D 8-->S 9-->B
     * @param uuid  服务端返回的已加密身份证号码
     * @return
     */
    public static String decryptUuid(String uuid){
        return uuid.replace("*", "0").replace("&", "1").replace("%", "2").replace("#", "3")
                .replace("@", "4").replace("Q", "5").replace("P", "6").replace("D", "7")
                .replace("S", "8").replace("B", "9");
    }

    /**
     * 身份证号码加密
     * 加密规则:0—>*   1—>&  2—>%  3—>#  4—>@ 5—>Q 6—>P 7—>D 8—>S 9—>B
     *  43061119920102554x
     *  @#*P\u0026\u0026\u0026BB%*\u0026*%QQ@x
     *
     *  430482199404073618
     *  @#*@S%\u0026BB@*@*D#P\u0026S
     * @param uuid
     * @return
     */
    public static String getEncodedUuid(String uuid) {
        return uuid
                .replaceAll("0", "*")
                .replaceAll("1", "&")
                .replaceAll("2", "%")
                .replaceAll("3", "#")
                .replaceAll("4", "@")
                .replaceAll("5", "Q")
                .replaceAll("6", "P")
                .replaceAll("7", "D")
                .replaceAll("8", "S")
                .replaceAll("9", "B");
    }

    /**
     * 获取android设备唯一标示
     *
     * @param context
     * @return
     */
    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
}
