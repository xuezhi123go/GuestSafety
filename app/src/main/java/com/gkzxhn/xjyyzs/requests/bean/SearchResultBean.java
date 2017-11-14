package com.gkzxhn.xjyyzs.requests.bean;

import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/1.
 * description:搜索结果实体
 * {
 "applies": [
     {
     "name": "黄正能",
     "uuid": "@#*@S%&BB@*@*D#P&S",
     "phone": "18774810958",
     "application": [
                     {
                     "applyDate": "2016-09-27",
                     "_id": "57e8685f2b03640001687585",
                     "feedback": {
                             "meetingTime": "10:00",
                             "sfs": "s0997003",
                             "prison": "第一监狱",
                             "content": "预约会见成功",
                             "isPass": "PASSED",
                             "from": "M"
                             }
                     }
                ]
     },
     {
     "name": "黄正能",
     "uuid": "@#*@S%&BB@*@*D#P&S",
     "phone": "18774810958",
     "application": [
                     {
                     "applyDate": "2016-09-27",
                     "_id": "57e8685f2b03640001687585",
                     "feedback": {
                             "meetingTime": "10:00",
                             "sfs": "s0997003",
                             "prison": "第一监狱",
                             "content": "预约会见成功",
                             "isPass": "PASSED",
                             "from": "M"
                             }
                     }
                ]
     }
 ]
 }


 */

public class SearchResultBean {


    /**
     * application : [{"_id":"57c55689a51fb41400d4e454","applyDate":"2016-09-06","feedback":{"content":"预约会见成功","from":"M","isPass":"PASSED","meetingTime":"10:00","prison":"第三监狱","sfs":"s0997003"}}]
     * name : 黄正能
     * uuid : @#*@S%&BB@*@*D#P&S
     * phone:18774810958
     */

    private List<AppliesBean> applies;

    public List<AppliesBean> getApplies() {
        return applies;
    }

    public void setApplies(List<AppliesBean> applies) {
        this.applies = applies;
    }

    public static class AppliesBean {
        private String name;
        private String applicant;
        /**
         * _id : 57c55689a51fb41400d4e454
         * applyDate : 2016-09-06
         * feedback : {"content":"预约会见成功","from":"M","isPass":"PASSED","meetingTime":"10:00","prison":"第三监狱","sfs":"s0997003"}
         */

        private List<ApplyBean> history;
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<ApplyBean> getApplication() {
            return history;
        }

        public void setApplication(List<ApplyBean> application) {
            this.history = application;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUuid() {
            return applicant;
        }

        public void setUuid(String uuid) {
            this.applicant = uuid;
        }

        public static class ApplyBean {
            private String _id;
            private String fillingDate;
            /**
             * content : 预约会见成功
             * from : M
             * isPass : PASSED
             * meetingTime : 10:00
             * prison : 第三监狱
             * sfs : s0997003
             */

            private FeedbackBean feedback;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getApplyDate() {
                return fillingDate;
            }

            public void setApplyDate(String applyDate) {
                this.fillingDate = applyDate;
            }

            public FeedbackBean getFeedback() {
                return feedback;
            }

            public void setFeedback(FeedbackBean feedback) {
                this.feedback = feedback;
            }

            public static class FeedbackBean {
                private String content;
                private String from;
                private String isPass;
                private String meetingTime;
                private String prison;
                private String sfs;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
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

                public String getMeetingTime() {
                    return meetingTime;
                }

                public void setMeetingTime(String meetingTime) {
                    this.meetingTime = meetingTime;
                }

                public String getPrison() {
                    return prison;
                }

                public void setPrison(String prison) {
                    this.prison = prison;
                }

                public String getSfs() {
                    return sfs;
                }

                public void setSfs(String sfs) {
                    this.sfs = sfs;
                }

                @Override
                public String toString() {
                    return "FeedbackBean{" +
                            "content='" + content + '\'' +
                            ", from='" + from + '\'' +
                            ", isPass='" + isPass + '\'' +
                            ", meetingTime='" + meetingTime + '\'' +
                            ", prison='" + prison + '\'' +
                            ", sfs='" + sfs + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ApplyBean{" +
                        "_id='" + _id + '\'' +
                        ", applyDate='" + fillingDate + '\'' +
                        ", feedback=" + feedback.toString() +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AppliesBean{" +
                    "name='" + name + '\'' +
                    ", uuid='" + applicant + '\'' +
                    ", application.size()=" + history.size() +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchResultBean{" +
                "applies=" + applies.size() +
                '}';
    }
}
