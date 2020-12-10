package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import java.util.List;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class SongUrlEntity {

    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        private int id;
        private String url;
        private int br;
        private int size;
        private String md5;
        private int code;
        private int expi;
        private String type;
        private int gain;
        private int fee;
        private Object uf;
        private int payed;
        private int flag;
        private boolean canExtend;
        private Object freeTrialInfo;
        private Object level;
        private Object encodeType;

        public int getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public int getBr() {
            return br;
        }

        public int getSize() {
            return size;
        }

        public String getMd5() {
            return md5;
        }

        public int getCode() {
            return code;
        }

        public int getExpi() {
            return expi;
        }

        public String getType() {
            return type;
        }

        public int getGain() {
            return gain;
        }

        public int getFee() {
            return fee;
        }

        public Object getUf() {
            return uf;
        }

        public int getPayed() {
            return payed;
        }

        public int getFlag() {
            return flag;
        }

        public boolean isCanExtend() {
            return canExtend;
        }

        public Object getFreeTrialInfo() {
            return freeTrialInfo;
        }

        public Object getLevel() {
            return level;
        }

        public Object getEncodeType() {
            return encodeType;
        }
    }
}
