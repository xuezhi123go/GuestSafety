package com.gkzxhn.xjyyzs.requests.bean;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:登录结果
 *
 *
 * {
 *      "user":
 *          {
 *              "userid":"aks001",
 *              "token":"57bfb9c5982852140025fdab",
 *              "name":"郭刚",
 *              "phone":"",
 *              "cloudMsg":
 *                      {
 *                          "token":"123456",
 *                          "cloudID":"xjtest1"
 *                      },
 *              "orgnization":
 *                  {
 *                      "title":"英巴格社区居委会",
 *                      "code":"0997001"
 *                   }
 *          }
 * }
 */

public class LoginResult {

    /**
     * userid : ryman1981
     * token : 57a7e596623f181400d94f40
     * name : David Liu
     * cloudMsg : {"token":"123456","cloudID":"xjtest1"}
     * orgnization : {"title":"阿克苏依干其乡司法所","code":"0997123"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "user=" + user.toString() +
                '}';
    }

    public static class UserBean {
        private String userid;
        private String token;
        private String name;
        /**
         * token : 123456
         * cloudID : xjtest1
         */

        private CloudMsgBean cloudMsg;
        /**
         * title : 阿克苏依干其乡司法所
         * code : 0997123
         */

        private OrgnizationBean orgnization;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CloudMsgBean getCloudMsg() {
            return cloudMsg;
        }

        public void setCloudMsg(CloudMsgBean cloudMsg) {
            this.cloudMsg = cloudMsg;
        }

        public OrgnizationBean getOrgnization() {
            return orgnization;
        }

        public void setOrgnization(OrgnizationBean orgnization) {
            this.orgnization = orgnization;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "userid='" + userid + '\'' +
                    ", token='" + token + '\'' +
                    ", name='" + name + '\'' +
                    ", cloudMsg=" + cloudMsg.toString() +
                    ", orgnization=" + orgnization.toString() +
                    '}';
        }

        public static class CloudMsgBean {
            private String token;
            private String cloudID;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getCloudID() {
                return cloudID;
            }

            public void setCloudID(String cloudID) {
                this.cloudID = cloudID;
            }

            @Override
            public String toString() {
                return "CloudMsgBean{" +
                        "token='" + token + '\'' +
                        ", cloudID='" + cloudID + '\'' +
                        '}';
            }
        }

        public static class OrgnizationBean {
            private String title;
            private String code;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            @Override
            public String toString() {
                return "OrgnizationBean{" +
                        "title='" + title + '\'' +
                        ", code='" + code + '\'' +
                        '}';
            }
        }
    }
}
