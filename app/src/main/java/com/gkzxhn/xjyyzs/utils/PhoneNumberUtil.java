package com.gkzxhn.xjyyzs.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/12.
 * description:电话号码的工具类  判断手机 固话
 */

public class PhoneNumberUtil {

    // 手机号码
    private static final String REGEX_MOBILEPHONE = "^0?1[34578]\\d{9}$";
    // 固话
    private static final String REGEX_FIXEDPHONE = "^(010|02\\d||0[3-9]\\d{2})?\\d{6,8}$";
    // 必须有区号的固话
    private static final String REGEX_ZIPCODE = "^(010|02\\d||0[3-9]\\d{2})\\d{6,8}$";

    private static Pattern PATTERN_MOBILEPHONE;
    private static Pattern PATTERN_FIXEDPHONE;
    private static Pattern PATTERN_ZIPCODE;

    static {
        PATTERN_MOBILEPHONE = Pattern.compile(REGEX_MOBILEPHONE);
        PATTERN_FIXEDPHONE = Pattern.compile(REGEX_FIXEDPHONE);
        PATTERN_ZIPCODE = Pattern.compile(REGEX_ZIPCODE);
    }

    public enum PhoneType{
        /**
         * 手机号
         */
        CELLPHONE,
        /**
         * 固话
         */
        FIXEDPHONE,
        /**
         * 未识别的号码类型
         */
        INVALIDPHONE
    }

    public static class Number{

        private PhoneType type;
        /**
         * 如果是手机号  该字段存的是手机号前七位  如果是固话 则是区号
         */
        private String code;
        private String number;

        public Number(PhoneType type, String code, String number) {
            this.type = type;
            this.code = code;
            this.number = number;
        }

        public PhoneType getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        public String getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return String.format("[number:%s, type:%s, code:%s]", number, type.name(), code);
        }
    }

    /**
     * 判断是否为手机号
     * @param number
     * @return
     */
    public static boolean isCellPhone(String number){
        Matcher match = PATTERN_MOBILEPHONE.matcher(number);
        return match.matches();
    }

    /**
     * 判断是否为固话
     * @param number
     * @return
     */
    public static boolean isFixedPhone(String number){
        Matcher match = PATTERN_FIXEDPHONE.matcher(number);
        return match.matches();
    }

    /**
     * 获取固定号码中的区号
     * @param strNumber
     * @return
     */
    public static String getZipFromPhone(String strNumber){
        Matcher match = PATTERN_ZIPCODE.matcher(strNumber);
        if (match.find()){
            return match.group(1);
        }
        return null;
    }

    /**
     * 检查号码类型并获取号码前缀 手机号获取前7位  固话获取区号
     * @param _number
     * @return
     */
    public static Number checkNumber(String _number){
        String number = _number;
        Number rtNum = null;
        if (number != null && number.length() > 0){
            if (isCellPhone(number)){
                if (number.charAt(0) == '0'){
                    // 以0开头的去掉0
                    number = number.substring(1);
                }
                rtNum = new Number(PhoneType.CELLPHONE, number.substring(0, 7), _number);
            }else if(isFixedPhone(number)){
                String zipCode = getZipFromPhone(number);
                rtNum = new Number(PhoneType.FIXEDPHONE, zipCode, _number);
            }else {
                rtNum = new Number(PhoneType.INVALIDPHONE, null, _number);
            }
        }
        return rtNum;
    }
}
