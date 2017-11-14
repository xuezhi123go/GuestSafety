package com.gkzxhn.xjyyzs.requests.bean;

import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/11.
 * function:预约申请post所需实体类
 */

public class Apply {


    /**
     * orgCode : 0997123
     * uuid : 650104198111224217
     * applyDate : 2016-08-08
     */

    private ApplyBean application;

    public ApplyBean getApply() {
        return application;
    }

    public void setApply(ApplyBean apply) {
        this.application = apply;
    }

    public class ApplyBean {
        private String orgCode;
        private String uuid;
        private String phone;
        private String name;
        private String fillingDate;
        private List<Family> family;

        public List<Family> getFamily() {
            return family;
        }

        public void setFamily(List<Family> family) {
            this.family = family;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getApplyDate() {
            return fillingDate;
        }

        public void setApplyDate(String applyDate) {
            this.fillingDate = applyDate;
        }

        @Override
        public String toString() {
            return "ApplyBean{" +
                    "orgCode='" + orgCode + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", phone='" + phone + '\'' +
                    ", name='" + name + '\'' +
                    ", fillingDate='" + fillingDate + '\'' +
                    ", family=" + family +
                    '}';
        }

        public class Family {

            private String uuid;
            private String phone;
            private String name;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

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

            @Override
            public String toString() {
                return "Family{" +
                        "uuid='" + uuid + '\'' +
                        ", phone='" + phone + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "Apply{" +
                "apply=" + application.toString() +
                '}';
    }
}
