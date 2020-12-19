package com.fr1014.mycoludmusic.data.entity.http.kuwo;

import java.util.List;

/**
 * Create by fanrui on 2020/12/11
 * Describe:
 */
public class KWNewSearchEntity {
    private String ARTISTPIC;
    private String HIT;
    private String HITMODE;
    private String HIT_BUT_OFFLINE;
    private String MSHOW;
    private String NEW;
    private String PN;
    private String RN;
    private String SHOW;
    private String TOTAL;
    private String searchgroup;
    private List<AbslistBean> abslist;

    public String getARTISTPIC() {
        return ARTISTPIC;
    }

    public String getHIT() {
        return HIT;
    }

    public String getHITMODE() {
        return HITMODE;
    }

    public String getHIT_BUT_OFFLINE() {
        return HIT_BUT_OFFLINE;
    }

    public String getMSHOW() {
        return MSHOW;
    }

    public String getNEW() {
        return NEW;
    }

    public String getPN() {
        return PN;
    }

    public String getRN() {
        return RN;
    }

    public String getSHOW() {
        return SHOW;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public String getSearchgroup() {
        return searchgroup;
    }

    public List<AbslistBean> getAbslist() {
        return abslist;
    }

    public static class AbslistBean {
        /**
         * AARTIST : 泰勒·斯威夫特
         * ALBUM : evermore
         * ALBUMID : 16965636
         * ALIAS :
         * ARTIST : Taylor Swift
         * ARTISTID : 4968
         * CanSetRing : 1
         * CanSetRingback : 1
         * DURATION : 275
         * FARTIST : Taylor Swift
         * FORMAT : wma
         * FSONGNAME :
         * KMARK : 0
         * MINFO : level:ff,bitrate:2000,format:flac,size:28.11Mb;level:pp,bitrate:1000,format:ape,size:27.33Mb;level:p,bitrate:320,format:mp3,size:10.49Mb;level:h,bitrate:192,format:mp3,size:6.29Mb;level:s,bitrate:48,format:aac,size:807.10Kb
         * MUSICRID : MUSIC_158790286
         * MVFLAG : 0
         * MVPIC :
         * MVQUALITY :
         * NAME : cowboy like me
         * NEW : 1
         * ONLINE : 1
         * PAY : 255
         * PROVIDER :
         * SONGNAME : cowboy like me(Explicit)
         * SUBLIST : []
         * SUBTITLE :
         * TAG : http://218.30.20.121:5699/qunx/qingshenyuan2/qsyC18.wma
         * audiobookpayinfo : {"download":"0","play":"0"}
         * barrage : 0
         * cache_status : 1
         * content_type : 0
         * fpay : 0
         * iot_info :
         * isdownload : 0
         * isshowtype : 0
         * isstar : 0
         * mvpayinfo : {"download":"0","play":"0","vid":"0"}
         * nationid : 0
         * opay : 0
         * originalsongtype : 1
         * overseas_copyright : 1
         * overseas_pay : 0
         * payInfo : {"cannotDownload":"0","cannotOnlinePlay":"0","download":"1111","feeType":{"album":"1","bookvip":"0","song":"0","vip":"0"},"listen_fragment":"0","local_encrypt":"0","play":"1111","tips_intercept":"0"}
         * react_type :
         * spPrivilege : 0
         * subsStrategy : 0
         * subsText :
         * terminal :
         * tpay : 0
         * hts_MVPIC : https://img4.kuwo.cn/wmvpic/324/94/84/3620662350.jpg
         */

        private String AARTIST;
        private String ALBUM;
        private String ALBUMID;
        private String ALIAS;
        private String ARTIST;
        private String ARTISTID;
        private String CanSetRing;
        private String CanSetRingback;
        private String DURATION;
        private String FARTIST;
        private String FORMAT;
        private String FSONGNAME;
        private String KMARK;
        private String MINFO;
        private String MUSICRID;
        private String MVFLAG;
        private String MVPIC;
        private String MVQUALITY;
        private String NAME;
        private String NEW;
        private String ONLINE;
        private String PAY;
        private String PROVIDER;
        private String SONGNAME;
        private String SUBTITLE;
        private String TAG;
        private AudiobookpayinfoBean audiobookpayinfo;
        private String barrage;
        private String cache_status;
        private String content_type;
        private String fpay;
        private String iot_info;
        private String isdownload;
        private String isshowtype;
        private String isstar;
        private MvpayinfoBean mvpayinfo;
        private String nationid;
        private String opay;
        private String originalsongtype;
        private String overseas_copyright;
        private String overseas_pay;
        private PayInfoBean payInfo;
        private String react_type;
        private String spPrivilege;
        private String subsStrategy;
        private String subsText;
        private String terminal;
        private String tpay;
        private String hts_MVPIC;
        private List<?> SUBLIST;

        public String getAARTIST() {
            return AARTIST;
        }

        public String getALBUM() {
            return ALBUM;
        }

        public String getALBUMID() {
            return ALBUMID;
        }

        public String getALIAS() {
            return ALIAS;
        }

        public String getARTIST() {
            return ARTIST;
        }

        public String getARTISTID() {
            return ARTISTID;
        }

        public String getCanSetRing() {
            return CanSetRing;
        }

        public String getCanSetRingback() {
            return CanSetRingback;
        }

        public String getDURATION() {
            return DURATION;
        }

        public String getFARTIST() {
            return FARTIST;
        }

        public String getFORMAT() {
            return FORMAT;
        }

        public String getFSONGNAME() {
            return FSONGNAME;
        }

        public String getKMARK() {
            return KMARK;
        }

        public String getMINFO() {
            return MINFO;
        }

        public String getMUSICRID() {
            return MUSICRID;
        }

        public String getMVFLAG() {
            return MVFLAG;
        }

        public String getMVPIC() {
            return MVPIC;
        }

        public String getMVQUALITY() {
            return MVQUALITY;
        }

        public String getNAME() {
            return NAME;
        }

        public String getNEW() {
            return NEW;
        }

        public String getONLINE() {
            return ONLINE;
        }

        public String getPAY() {
            return PAY;
        }

        public String getPROVIDER() {
            return PROVIDER;
        }

        public String getSONGNAME() {
            return SONGNAME;
        }

        public String getSUBTITLE() {
            return SUBTITLE;
        }

        public String getTAG() {
            return TAG;
        }

        public AudiobookpayinfoBean getAudiobookpayinfo() {
            return audiobookpayinfo;
        }

        public String getBarrage() {
            return barrage;
        }

        public String getCache_status() {
            return cache_status;
        }

        public String getContent_type() {
            return content_type;
        }

        public String getFpay() {
            return fpay;
        }

        public String getIot_info() {
            return iot_info;
        }

        public String getIsdownload() {
            return isdownload;
        }

        public String getIsshowtype() {
            return isshowtype;
        }

        public String getIsstar() {
            return isstar;
        }

        public MvpayinfoBean getMvpayinfo() {
            return mvpayinfo;
        }

        public String getNationid() {
            return nationid;
        }

        public String getOpay() {
            return opay;
        }

        public String getOriginalsongtype() {
            return originalsongtype;
        }

        public String getOverseas_copyright() {
            return overseas_copyright;
        }

        public String getOverseas_pay() {
            return overseas_pay;
        }

        public PayInfoBean getPayInfo() {
            return payInfo;
        }

        public String getReact_type() {
            return react_type;
        }

        public String getSpPrivilege() {
            return spPrivilege;
        }

        public String getSubsStrategy() {
            return subsStrategy;
        }

        public String getSubsText() {
            return subsText;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getTpay() {
            return tpay;
        }

        public String getHts_MVPIC() {
            return hts_MVPIC;
        }

        public List<?> getSUBLIST() {
            return SUBLIST;
        }

        public static class AudiobookpayinfoBean {
            /**
             * download : 0
             * play : 0
             */

            private String download;
            private String play;

            public String getDownload() {
                return download;
            }

            public String getPlay() {
                return play;
            }

        }

        public static class MvpayinfoBean {
            /**
             * download : 0
             * play : 0
             * vid : 0
             */

            private String download;
            private String play;
            private String vid;

            public String getDownload() {
                return download;
            }

            public String getPlay() {
                return play;
            }


            public String getVid() {
                return vid;
            }
        }

        public static class PayInfoBean {
            /**
             * cannotDownload : 0
             * cannotOnlinePlay : 0
             * download : 1111
             * feeType : {"album":"1","bookvip":"0","song":"0","vip":"0"}
             * listen_fragment : 0
             * local_encrypt : 0
             * play : 1111
             * tips_intercept : 0
             */

            private String cannotDownload;
            private String cannotOnlinePlay;
            private String download;
            private FeeTypeBean feeType;
            private String listen_fragment;
            private String local_encrypt;
            private String play;
            private String tips_intercept;

            public String getCannotDownload() {
                return cannotDownload;
            }

            public String getCannotOnlinePlay() {
                return cannotOnlinePlay;
            }

            public String getDownload() {
                return download;
            }

            public FeeTypeBean getFeeType() {
                return feeType;
            }

            public String getListen_fragment() {
                return listen_fragment;
            }

            public String getLocal_encrypt() {
                return local_encrypt;
            }

            public String getPlay() {
                return play;
            }

            public String getTips_intercept() {
                return tips_intercept;
            }

            public static class FeeTypeBean {
                /**
                 * album : 1
                 * bookvip : 0
                 * song : 0
                 * vip : 0
                 */

                private String album;
                private String bookvip;
                private String song;
                private String vip;

                public String getAlbum() {
                    return album;
                }

                public String getBookvip() {
                    return bookvip;
                }

                public String getSong() {
                    return song;
                }

                public String getVip() {
                    return vip;
                }
            }
        }
    }
}
