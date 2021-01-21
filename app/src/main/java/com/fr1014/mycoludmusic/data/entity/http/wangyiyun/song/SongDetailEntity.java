package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.Privileges;

import java.util.List;

/**
 * 创建时间:2020/9/11
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class SongDetailEntity {
    private int code;
    private List<SongsBean> songs;
    public List<Privileges> privileges;

    public int getCode() {
        return code;
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
        private int single;
        private Object noCopyrightRcmd;
        private int rtype;
        private Object rurl;
        private int mst;
        private int cp;
        private int mv;
        private long publishTime;
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

        public int getSingle() {
            return single;
        }

        public Object getNoCopyrightRcmd() {
            return noCopyrightRcmd;
        }

        public int getRtype() {
            return rtype;
        }

        public Object getRurl() {
            return rurl;
        }

        public int getMst() {
            return mst;
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

            public int br;
            public int fid;
            public int size;
            public double vd;
        }

        public static class MBean {
            public int br;
            public int fid;
            public int size;
            public double vd;
        }

        public static class LBean {
            public int br;
            public int fid;
            public int size;
            public double vd;
        }

        public static class ArBean {
            private int id;
            private String name;
            private List<?> tns;
            private List<?> alias;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public List<?> getTns() {
                return tns;
            }

            public List<?> getAlias() {
                return alias;
            }
        }
    }
}
