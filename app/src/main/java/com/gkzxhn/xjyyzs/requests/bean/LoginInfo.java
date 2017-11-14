package com.gkzxhn.xjyyzs.requests.bean;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:登录信息
 */

public class LoginInfo {

    private LoginBean session;

    public LoginBean getSession() {
        return session;
    }

    public void setSession(LoginBean session) {
        this.session = session;
    }

    public class LoginBean {

        private String userid;
        private String password;
        private String deviceId;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "\"userid\":\"" + userid + "\", \"password\":\"" + password + "\", \"deviceId\":\"" + deviceId + "\"";
        }
    }

    @Override
    public String toString() {
        return "{\"session\":{" + session.toString() + "}}";
    }
}
