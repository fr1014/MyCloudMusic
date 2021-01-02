package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import java.util.List;

/**
 * Create by fanrui on 2020/12/29
 * Describe:
 */
public class WYSearchDetail {
    private ResultBean result;
    private int code;

    public ResultBean getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public static class ResultBean {

        private int songCount;
        private List<SongsBean> songs;

        public int getSongCount() {
            return songCount;
        }

        public List<SongsBean> getSongs() {
            return songs;
        }

        public static class SongsBean {

            private String name;
            private int id;
            private int pst;
            private int t;
            private int pop;
            private int st;
            private String rt;
            private int fee;
            private int v;
            private Object crbt;
            private String cf;
            private AlBean al;
            private int dt;
            private HBean h;
            private MBean m;
            private LBean l;
            private Object a;
            private String cd;
            private int no;
            private Object rtUrl;
            private int ftype;
            private int djId;
            private int copyright;
            private int s_id;
            private long mark;
            private int originCoverType;
            private Object originSongSimpleData;
            private int single;
            private Object noCopyrightRcmd;
            private int mst;
            private int rtype;
            private Object rurl;
            private int cp;
            private int mv;
            private long publishTime;
            private PrivilegeBean privilege;
            private List<ArBean> ar;
            private List<?> alia;
            private List<?> rtUrls;

            public String getName() {
                return name;
            }

            public int getId() {
                return id;
            }

            public int getPst() {
                return pst;
            }

            public int getT() {
                return t;
            }

            public int getPop() {
                return pop;
            }

            public int getSt() {
                return st;
            }

            public String getRt() {
                return rt;
            }

            public int getFee() {
                return fee;
            }

            public int getV() {
                return v;
            }

            public Object getCrbt() {
                return crbt;
            }

            public String getCf() {
                return cf;
            }

            public AlBean getAl() {
                return al;
            }

            public int getDt() {
                return dt;
            }

            public HBean getH() {
                return h;
            }

            public MBean getM() {
                return m;
            }

            public LBean getL() {
                return l;
            }

            public Object getA() {
                return a;
            }

            public String getCd() {
                return cd;
            }

            public int getNo() {
                return no;
            }

            public Object getRtUrl() {
                return rtUrl;
            }

            public int getFtype() {
                return ftype;
            }

            public int getDjId() {
                return djId;
            }

            public int getCopyright() {
                return copyright;
            }

            public int getS_id() {
                return s_id;
            }

            public long getMark() {
                return mark;
            }

            public int getOriginCoverType() {
                return originCoverType;
            }

            public Object getOriginSongSimpleData() {
                return originSongSimpleData;
            }

            public int getSingle() {
                return single;
            }

            public Object getNoCopyrightRcmd() {
                return noCopyrightRcmd;
            }

            public int getMst() {
                return mst;
            }

            public int getRtype() {
                return rtype;
            }

            public Object getRurl() {
                return rurl;
            }

            public int getCp() {
                return cp;
            }

            public int getMv() {
                return mv;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public PrivilegeBean getPrivilege() {
                return privilege;
            }

            public List<ArBean> getAr() {
                return ar;
            }

            public List<?> getAlia() {
                return alia;
            }

            public List<?> getRtUrls() {
                return rtUrls;
            }

            public static class AlBean {

                private int id;
                private String name;
                private String picUrl;
                private String pic_str;
                private long pic;
                private List<?> tns;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public String getPic_str() {
                    return pic_str;
                }

                public long getPic() {
                    return pic;
                }

                public List<?> getTns() {
                    return tns;
                }
            }

            public static class HBean {

                private int br;
                private int fid;
                private int size;
                private double vd;

                public int getBr() {
                    return br;
                }

                public int getFid() {
                    return fid;
                }

                public int getSize() {
                    return size;
                }

                public double getVd() {
                    return vd;
                }
            }

            public static class MBean {
                private int br;
                private int fid;
                private int size;
                private double vd;

                public int getBr() {
                    return br;
                }

                public int getFid() {
                    return fid;
                }

                public int getSize() {
                    return size;
                }

                public double getVd() {
                    return vd;
                }
            }

            public static class LBean {
                private int br;
                private int fid;
                private int size;
                private double vd;

                public int getBr() {
                    return br;
                }

                public int getFid() {
                    return fid;
                }

                public int getSize() {
                    return size;
                }

                public double getVd() {
                    return vd;
                }
            }

            public static class PrivilegeBean {
                private int id;
                private int fee;
                private int payed;
                private int st;
                private int pl;
                private int dl;
                private int sp;
                private int cp;
                private int subp;
                private boolean cs;
                private int maxbr;
                private int fl;
                private boolean toast;
                private int flag;
                private boolean preSell;
                private int playMaxbr;
                private int downloadMaxbr;
                private FreeTrialPrivilegeBean freeTrialPrivilege;
                private List<ChargeInfoListBean> chargeInfoList;

                public int getId() {
                    return id;
                }

                public int getFee() {
                    return fee;
                }

                public int getPayed() {
                    return payed;
                }

                public int getSt() {
                    return st;
                }

                public int getPl() {
                    return pl;
                }

                public int getDl() {
                    return dl;
                }

                public int getSp() {
                    return sp;
                }

                public int getCp() {
                    return cp;
                }

                public int getSubp() {
                    return subp;
                }

                public boolean isCs() {
                    return cs;
                }

                public int getMaxbr() {
                    return maxbr;
                }

                public int getFl() {
                    return fl;
                }

                public boolean isToast() {
                    return toast;
                }

                public int getFlag() {
                    return flag;
                }

                public boolean isPreSell() {
                    return preSell;
                }

                public int getPlayMaxbr() {
                    return playMaxbr;
                }

                public int getDownloadMaxbr() {
                    return downloadMaxbr;
                }

                public FreeTrialPrivilegeBean getFreeTrialPrivilege() {
                    return freeTrialPrivilege;
                }

                public List<ChargeInfoListBean> getChargeInfoList() {
                    return chargeInfoList;
                }

                public static class FreeTrialPrivilegeBean {
                    private boolean resConsumable;
                    private boolean userConsumable;

                    public boolean isResConsumable() {
                        return resConsumable;
                    }

                    public boolean isUserConsumable() {
                        return userConsumable;
                    }
                }

                public static class ChargeInfoListBean {

                    private int rate;
                    private Object chargeUrl;
                    private Object chargeMessage;
                    private int chargeType;

                    public int getRate() {
                        return rate;
                    }

                    public Object getChargeUrl() {
                        return chargeUrl;
                    }

                    public Object getChargeMessage() {
                        return chargeMessage;
                    }

                    public int getChargeType() {
                        return chargeType;
                    }
                }
            }

            public static class ArBean {

                private int id;
                private String name;
                private List<?> tns;
                private List<String> alias;
                private List<String> alia;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }

                public List<?> getTns() {
                    return tns;
                }

                public List<String> getAlias() {
                    return alias;
                }

                public List<String> getAlia() {
                    return alia;
                }
            }
        }
    }
}
