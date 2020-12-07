package com.fr1014.mycoludmusic.data.entity.http.kuwo;

public class KWSongDetailEntity {

    /**
     * code : 200
     * curTime : 1607344086149
     * data : {"musicrid":"MUSIC_140897945","barrage":"0","artist":"张韶涵","mvpayinfo":{"play":0,"vid":0,"down":0},"pic":"https://img3.kuwo.cn/star/albumcover/500/79/41/2174085203.jpg","isstar":0,"rid":140897945,"upPcStr":"kuwo://play/?play=MQ==&amp;num=MQ==&amp;musicrid0=TVVTSUNfMTQwODk3OTQ1&amp;name0=xsa86w==&amp;artist0=1cXJ2Lqt&amp;album0=xsa86w==&amp;artistid0=NDky&amp;albumid0=MTQxNDYzMjI=&amp;playsource=d2ViwK3G8L/Nu6e2yy0+MjAxNrDmtaXH+tKz","duration":211,"score100":"101","content_type":"0","mvPlayCnt":776337,"track":1,"hasLossless":true,"hasmv":1,"releaseDate":"2020-05-23","album":"破茧","albumid":14146322,"pay":"16515324","artistid":492,"albumpic":"https://img3.kuwo.cn/star/albumcover/500/79/41/2174085203.jpg","originalsongtype":1,"songTimeMinutes":"03:31","isListenFee":false,"mvUpPcStr":"kuwo://play/?play=MQ==&amp;num=MQ==&amp;musicrid0=TVVTSUNfMTQwODk3OTQ1&amp;name0=xsa86w==&amp;artist0=1cXJ2Lqt&amp;album0=xsa86w==&amp;artistid0=NDky&amp;albumid0=MTQxNDYzMjI=&amp;playsource=d2ViwK3G8L/Nu6e2yy0+MjAxNrDmtaXH+tKz&amp;media=bXY=&amp;mvid0=MA==&amp;mvinfo_play0=MA==&amp;mvinfo_download0=MA==","pic120":"https://img3.kuwo.cn/star/albumcover/120/79/41/2174085203.jpg","albuminfo":"\u201c风拨动了谁的心弦，留恋却来不及告别\u201d，人气动画《斗罗大陆》的主角唐三、小舞经历爱情生死考验；实力唱将张韶涵倾情献唱动画《斗罗大陆》主题曲《破茧》，以宽广的音域，富有穿透力的声音和极具爆发的声线传达了不一样斗罗力量。","name":"破茧","online":1,"payInfo":{"play":"1100","download":"1111","local_encrypt":"1","cannotDownload":0,"cannotOnlinePlay":0,"feeType":{"song":"1","vip":"1"},"down":"1111"}}
     * msg : success
     * profileId : site
     * reqId : 83e94a8bdb9a3e4accf389759fd61504
     */

    private int code;
    private long curTime;
    private DataBean data;
    private String msg;
    private String profileId;
    private String reqId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getCurTime() {
        return curTime;
    }

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public static class DataBean {
        /**
         * musicrid : MUSIC_140897945
         * barrage : 0
         * artist : 张韶涵
         * mvpayinfo : {"play":0,"vid":0,"down":0}
         * pic : https://img3.kuwo.cn/star/albumcover/500/79/41/2174085203.jpg
         * isstar : 0
         * rid : 140897945
         * upPcStr : kuwo://play/?play=MQ==&amp;num=MQ==&amp;musicrid0=TVVTSUNfMTQwODk3OTQ1&amp;name0=xsa86w==&amp;artist0=1cXJ2Lqt&amp;album0=xsa86w==&amp;artistid0=NDky&amp;albumid0=MTQxNDYzMjI=&amp;playsource=d2ViwK3G8L/Nu6e2yy0+MjAxNrDmtaXH+tKz
         * duration : 211
         * score100 : 101
         * content_type : 0
         * mvPlayCnt : 776337
         * track : 1
         * hasLossless : true
         * hasmv : 1
         * releaseDate : 2020-05-23
         * album : 破茧
         * albumid : 14146322
         * pay : 16515324
         * artistid : 492
         * albumpic : https://img3.kuwo.cn/star/albumcover/500/79/41/2174085203.jpg
         * originalsongtype : 1
         * songTimeMinutes : 03:31
         * isListenFee : false
         * mvUpPcStr : kuwo://play/?play=MQ==&amp;num=MQ==&amp;musicrid0=TVVTSUNfMTQwODk3OTQ1&amp;name0=xsa86w==&amp;artist0=1cXJ2Lqt&amp;album0=xsa86w==&amp;artistid0=NDky&amp;albumid0=MTQxNDYzMjI=&amp;playsource=d2ViwK3G8L/Nu6e2yy0+MjAxNrDmtaXH+tKz&amp;media=bXY=&amp;mvid0=MA==&amp;mvinfo_play0=MA==&amp;mvinfo_download0=MA==
         * pic120 : https://img3.kuwo.cn/star/albumcover/120/79/41/2174085203.jpg
         * albuminfo : “风拨动了谁的心弦，留恋却来不及告别”，人气动画《斗罗大陆》的主角唐三、小舞经历爱情生死考验；实力唱将张韶涵倾情献唱动画《斗罗大陆》主题曲《破茧》，以宽广的音域，富有穿透力的声音和极具爆发的声线传达了不一样斗罗力量。
         * name : 破茧
         * online : 1
         * payInfo : {"play":"1100","download":"1111","local_encrypt":"1","cannotDownload":0,"cannotOnlinePlay":0,"feeType":{"song":"1","vip":"1"},"down":"1111"}
         */

        private String musicrid;
        private String barrage;
        private String artist;
        private MvpayinfoBean mvpayinfo;
        private String pic;
        private int isstar;
        private int rid;
        private String upPcStr;
        private int duration;
        private String score100;
        private String content_type;
        private int mvPlayCnt;
        private int track;
        private boolean hasLossless;
        private int hasmv;
        private String releaseDate;
        private String album;
        private int albumid;
        private String pay;
        private int artistid;
        private String albumpic;
        private int originalsongtype;
        private String songTimeMinutes;
        private boolean isListenFee;
        private String mvUpPcStr;
        private String pic120;
        private String albuminfo;
        private String name;
        private int online;
        private PayInfoBean payInfo;

        public String getMusicrid() {
            return musicrid;
        }

        public void setMusicrid(String musicrid) {
            this.musicrid = musicrid;
        }

        public String getBarrage() {
            return barrage;
        }

        public void setBarrage(String barrage) {
            this.barrage = barrage;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public MvpayinfoBean getMvpayinfo() {
            return mvpayinfo;
        }

        public void setMvpayinfo(MvpayinfoBean mvpayinfo) {
            this.mvpayinfo = mvpayinfo;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getIsstar() {
            return isstar;
        }

        public void setIsstar(int isstar) {
            this.isstar = isstar;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getUpPcStr() {
            return upPcStr;
        }

        public void setUpPcStr(String upPcStr) {
            this.upPcStr = upPcStr;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getScore100() {
            return score100;
        }

        public void setScore100(String score100) {
            this.score100 = score100;
        }

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }

        public int getMvPlayCnt() {
            return mvPlayCnt;
        }

        public void setMvPlayCnt(int mvPlayCnt) {
            this.mvPlayCnt = mvPlayCnt;
        }

        public int getTrack() {
            return track;
        }

        public void setTrack(int track) {
            this.track = track;
        }

        public boolean isHasLossless() {
            return hasLossless;
        }

        public void setHasLossless(boolean hasLossless) {
            this.hasLossless = hasLossless;
        }

        public int getHasmv() {
            return hasmv;
        }

        public void setHasmv(int hasmv) {
            this.hasmv = hasmv;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public int getAlbumid() {
            return albumid;
        }

        public void setAlbumid(int albumid) {
            this.albumid = albumid;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }

        public int getArtistid() {
            return artistid;
        }

        public void setArtistid(int artistid) {
            this.artistid = artistid;
        }

        public String getAlbumpic() {
            return albumpic;
        }

        public void setAlbumpic(String albumpic) {
            this.albumpic = albumpic;
        }

        public int getOriginalsongtype() {
            return originalsongtype;
        }

        public void setOriginalsongtype(int originalsongtype) {
            this.originalsongtype = originalsongtype;
        }

        public String getSongTimeMinutes() {
            return songTimeMinutes;
        }

        public void setSongTimeMinutes(String songTimeMinutes) {
            this.songTimeMinutes = songTimeMinutes;
        }

        public boolean isIsListenFee() {
            return isListenFee;
        }

        public void setIsListenFee(boolean isListenFee) {
            this.isListenFee = isListenFee;
        }

        public String getMvUpPcStr() {
            return mvUpPcStr;
        }

        public void setMvUpPcStr(String mvUpPcStr) {
            this.mvUpPcStr = mvUpPcStr;
        }

        public String getPic120() {
            return pic120;
        }

        public void setPic120(String pic120) {
            this.pic120 = pic120;
        }

        public String getAlbuminfo() {
            return albuminfo;
        }

        public void setAlbuminfo(String albuminfo) {
            this.albuminfo = albuminfo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public PayInfoBean getPayInfo() {
            return payInfo;
        }

        public void setPayInfo(PayInfoBean payInfo) {
            this.payInfo = payInfo;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "musicrid='" + musicrid + '\'' +
                    ", barrage='" + barrage + '\'' +
                    ", artist='" + artist + '\'' +
                    ", mvpayinfo=" + mvpayinfo +
                    ", pic='" + pic + '\'' +
                    ", isstar=" + isstar +
                    ", rid=" + rid +
                    ", upPcStr='" + upPcStr + '\'' +
                    ", duration=" + duration +
                    ", score100='" + score100 + '\'' +
                    ", content_type='" + content_type + '\'' +
                    ", mvPlayCnt=" + mvPlayCnt +
                    ", track=" + track +
                    ", hasLossless=" + hasLossless +
                    ", hasmv=" + hasmv +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", album='" + album + '\'' +
                    ", albumid=" + albumid +
                    ", pay='" + pay + '\'' +
                    ", artistid=" + artistid +
                    ", albumpic='" + albumpic + '\'' +
                    ", originalsongtype=" + originalsongtype +
                    ", songTimeMinutes='" + songTimeMinutes + '\'' +
                    ", isListenFee=" + isListenFee +
                    ", mvUpPcStr='" + mvUpPcStr + '\'' +
                    ", pic120='" + pic120 + '\'' +
                    ", albuminfo='" + albuminfo + '\'' +
                    ", name='" + name + '\'' +
                    ", online=" + online +
                    ", payInfo=" + payInfo +
                    '}';
        }

        public static class MvpayinfoBean {
            /**
             * play : 0
             * vid : 0
             * down : 0
             */

            private int play;
            private int vid;
            private int down;

            public int getPlay() {
                return play;
            }

            public void setPlay(int play) {
                this.play = play;
            }

            public int getVid() {
                return vid;
            }

            public void setVid(int vid) {
                this.vid = vid;
            }

            public int getDown() {
                return down;
            }

            public void setDown(int down) {
                this.down = down;
            }
        }

        public static class PayInfoBean {
            /**
             * play : 1100
             * download : 1111
             * local_encrypt : 1
             * cannotDownload : 0
             * cannotOnlinePlay : 0
             * feeType : {"song":"1","vip":"1"}
             * down : 1111
             */

            private String play;
            private String download;
            private String local_encrypt;
            private int cannotDownload;
            private int cannotOnlinePlay;
            private FeeTypeBean feeType;
            private String down;

            public String getPlay() {
                return play;
            }

            public void setPlay(String play) {
                this.play = play;
            }

            public String getDownload() {
                return download;
            }

            public void setDownload(String download) {
                this.download = download;
            }

            public String getLocal_encrypt() {
                return local_encrypt;
            }

            public void setLocal_encrypt(String local_encrypt) {
                this.local_encrypt = local_encrypt;
            }

            public int getCannotDownload() {
                return cannotDownload;
            }

            public void setCannotDownload(int cannotDownload) {
                this.cannotDownload = cannotDownload;
            }

            public int getCannotOnlinePlay() {
                return cannotOnlinePlay;
            }

            public void setCannotOnlinePlay(int cannotOnlinePlay) {
                this.cannotOnlinePlay = cannotOnlinePlay;
            }

            public FeeTypeBean getFeeType() {
                return feeType;
            }

            public void setFeeType(FeeTypeBean feeType) {
                this.feeType = feeType;
            }

            public String getDown() {
                return down;
            }

            public void setDown(String down) {
                this.down = down;
            }

            public static class FeeTypeBean {
                /**
                 * song : 1
                 * vip : 1
                 */

                private String song;
                private String vip;

                public String getSong() {
                    return song;
                }

                public void setSong(String song) {
                    this.song = song;
                }

                public String getVip() {
                    return vip;
                }

                public void setVip(String vip) {
                    this.vip = vip;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "KWSongDetailEntity{" +
                "code=" + code +
                ", curTime=" + curTime +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                ", profileId='" + profileId + '\'' +
                ", reqId='" + reqId + '\'' +
                '}';
    }
}
