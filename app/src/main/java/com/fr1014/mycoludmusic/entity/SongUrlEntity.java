package com.fr1014.mycoludmusic.entity;

import java.util.List;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class SongUrlEntity {

    /**
     * data : [{"id":1471813188,"url":"http://m8.music.126.net/20200902023228/e92cf6f078862ebea4e4831c598110f1/ymusic/obj/w5zDlMODwrDDiGjCn8Ky/3663199223/73a3/41be/747d/ef8dc25fa6555ab7543ff44f478a4edf.mp3","br":128001,"size":4177197,"md5":"ef8dc25fa6555ab7543ff44f478a4edf","code":200,"expi":1200,"type":"mp3","gain":0,"fee":8,"uf":null,"payed":0,"flag":64,"canExtend":false,"freeTrialInfo":null,"level":null,"encodeType":null}]
     * code : 200
     */
    private int code;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1471813188
         * url : http://m8.music.126.net/20200902023228/e92cf6f078862ebea4e4831c598110f1/ymusic/obj/w5zDlMODwrDDiGjCn8Ky/3663199223/73a3/41be/747d/ef8dc25fa6555ab7543ff44f478a4edf.mp3
         * br : 128001
         * size : 4177197
         * md5 : ef8dc25fa6555ab7543ff44f478a4edf
         * code : 200
         * expi : 1200
         * type : mp3
         * gain : 0
         * fee : 8
         * uf : null
         * payed : 0
         * flag : 64
         * canExtend : false
         * freeTrialInfo : null
         * level : null
         * encodeType : null
         */

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

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getBr() {
            return br;
        }

        public void setBr(int br) {
            this.br = br;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getExpi() {
            return expi;
        }

        public void setExpi(int expi) {
            this.expi = expi;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getGain() {
            return gain;
        }

        public void setGain(int gain) {
            this.gain = gain;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public Object getUf() {
            return uf;
        }

        public void setUf(Object uf) {
            this.uf = uf;
        }

        public int getPayed() {
            return payed;
        }

        public void setPayed(int payed) {
            this.payed = payed;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public boolean isCanExtend() {
            return canExtend;
        }

        public void setCanExtend(boolean canExtend) {
            this.canExtend = canExtend;
        }

        public Object getFreeTrialInfo() {
            return freeTrialInfo;
        }

        public void setFreeTrialInfo(Object freeTrialInfo) {
            this.freeTrialInfo = freeTrialInfo;
        }

        public Object getLevel() {
            return level;
        }

        public void setLevel(Object level) {
            this.level = level;
        }

        public Object getEncodeType() {
            return encodeType;
        }

        public void setEncodeType(Object encodeType) {
            this.encodeType = encodeType;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", url='" + url + '\'' +
                    ", br=" + br +
                    ", size=" + size +
                    ", md5='" + md5 + '\'' +
                    ", code=" + code +
                    ", expi=" + expi +
                    ", type='" + type + '\'' +
                    ", gain=" + gain +
                    ", fee=" + fee +
                    ", uf=" + uf +
                    ", payed=" + payed +
                    ", flag=" + flag +
                    ", canExtend=" + canExtend +
                    ", freeTrialInfo=" + freeTrialInfo +
                    ", level=" + level +
                    ", encodeType=" + encodeType +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SongUrlEntity{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
