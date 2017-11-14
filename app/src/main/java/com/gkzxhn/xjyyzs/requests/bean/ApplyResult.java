package com.gkzxhn.xjyyzs.requests.bean;

import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:申请结果查询bean
 *
 * {
    "applies": [
                {
                    "name": "肖君",
                    "uuid": "432503199003240835",
                    "phone":"xxxxxxx"
                    "application": {
                            "applyDate": "2016-09-06",
                            "_id": "57c55689a51fb41400d4e454",
                            "feedback": {
                                    "meetingTime": "10:00",
                                    "sfs": "s0997003",
                                    "prison": "第三监狱",
                                    "content": "预约会见成功",
                                    "isPass": "PASSED",
                                    "from": "M"
                            }
                     }
                }
                ]
    }

 */

public class ApplyResult {


    /**
     * name : 肖君
     * uuid : 432503199003240835
     * phone: xxxxxxx
     * application : {"applyDate":"2016-09-06","_id":"57c55689a51fb41400d4e454","feedback":{"meetingTime":"10:00","sfs":"s0997003","prison":"第三监狱","content":"预约会见成功","isPass":"PASSED","from":"M"}}
     */

    private List<AppliesBean> applies;

    public List<AppliesBean> getApplies() {
        return applies;
    }

    public void setApplies(List<AppliesBean> applies) {
        this.applies = applies;
    }

    public static class AppliesBean {

        public AppliesBean(String name, String uuid, String phone, ApplyBean application) {
            this.name = name;
            this.uuid = uuid;
            this.application = application;
            this.phone = phone;
        }

        private String name;
        private String uuid;
        private String phone;
        /**
         * applyDate : 2016-09-06
         * _id : 57c55689a51fb41400d4e454
         * feedback : {"meetingTime":"10:00","sfs":"s0997003","prison":"第三监狱","content":"预约会见成功","isPass":"PASSED","from":"M"}
         */

        private ApplyBean application;

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

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public ApplyBean getApplication() {
            return application;
        }

        public void setApplication(ApplyBean application) {
            this.application = application;
        }

        public static class ApplyBean {

            public ApplyBean(String applyDate, String _id, FeedbackBean feedback) {
                this.applyDate = applyDate;
                this._id = _id;
                this.feedback = feedback;
            }

            private String applyDate;
            private String _id;
            /**
             * meetingTime : 10:00
             * sfs : s0997003
             * prison : 第三监狱
             * content : 预约会见成功
             * isPass : PASSED
             * from : M
             */

            private FeedbackBean feedback;

            public String getApplyDate() {
                return applyDate;
            }

            public void setApplyDate(String applyDate) {
                this.applyDate = applyDate;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public FeedbackBean getFeedback() {
                return feedback;
            }

            public void setFeedback(FeedbackBean feedback) {
                this.feedback = feedback;
            }

            public static class FeedbackBean {

                public FeedbackBean(String meetingTime, String sfs, String content, String prison, String isPass, String from) {
                    this.meetingTime = meetingTime;
                    this.sfs = sfs;
                    this.content = content;
                    this.prison = prison;
                    this.isPass = isPass;
                    this.from = from;
                }

                public FeedbackBean() {
                }

                private String meetingTime;
                private String sfs;
                private String prison;
                private String content;
                private String isPass;
                private String from;

                public String getMeetingTime() {
                    return meetingTime;
                }

                public void setMeetingTime(String meetingTime) {
                    this.meetingTime = meetingTime;
                }

                public String getSfs() {
                    return sfs;
                }

                public void setSfs(String sfs) {
                    this.sfs = sfs;
                }

                public String getPrison() {
                    return prison;
                }

                public void setPrison(String prison) {
                    this.prison = prison;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getIsPass() {
                    String status = "";
                    if(isPass.equals("PENDING")){
                        status = "未处理";
                    }else if(isPass.equals("PASSED")){
                        status = "已通过";
                    }else if(isPass.equals("DENIED")){
                        status = "已拒绝";
                    }
                    return status;
                }

                public void setIsPass(String isPass) {
                    this.isPass = isPass;
                }

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                @Override
                public String toString() {
                    return "FeedbackBean{" +
                            "meetingTime='" + meetingTime + '\'' +
                            ", sfs='" + sfs + '\'' +
                            ", prison='" + prison + '\'' +
                            ", content='" + content + '\'' +
                            ", isPass='" + isPass + '\'' +
                            ", from='" + from + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ApplyBean{" +
                        "applyDate='" + applyDate + '\'' +
                        ", _id='" + _id + '\'' +
                        ", feedback=" + feedback.toString() +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AppliesBean{" +
                    "name='" + name + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", phone='" + phone + '\'' +
                    ", application=" + application +
                    '}';
        }
    }
}
