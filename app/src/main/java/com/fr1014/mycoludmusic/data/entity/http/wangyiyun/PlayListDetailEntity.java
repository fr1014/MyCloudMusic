package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import java.util.List;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 * 获取歌单详情
 */
public class PlayListDetailEntity {

    private int code;
    private Object relatedVideos;
    private PlaylistBean playlist;
    private Object urls;
    private List<PrivilegesBean> privileges;

    public int getCode() {
        return code;
    }

    public Object getRelatedVideos() {
        return relatedVideos;
    }

    public PlaylistBean getPlaylist() {
        return playlist;
    }

    public Object getUrls() {
        return urls;
    }

    public List<PrivilegesBean> getPrivileges() {
        return privileges;
    }

    public static class PlaylistBean {
        private boolean subscribed;
        private CreatorBean creator;
        private Object updateFrequency;
        private long backgroundCoverId;
        private Object backgroundCoverUrl;
        private long titleImage;
        private Object titleImageUrl;
        private Object englishTitle;
        private boolean opRecommend;
        private int adType;
        private long trackNumberUpdateTime;
        private long userId;
        private long createTime;
        private boolean highQuality;
        private long updateTime;
        private long coverImgId;
        private boolean newImported;
        private String coverImgUrl;
        private int specialType;
        private int trackCount;
        private String commentThreadId;
        private int privacy;
        private long trackUpdateTime;
        private long playCount;
        private int subscribedCount;
        private int cloudTrackCount;
        private boolean ordered;
        private String description;
        private int status;
        private String name;
        private long id;
        private int shareCount;
        private String coverImgId_str;
        private String ToplistType;
        private int commentCount;
        private List<SubscribersBean> subscribers;
        private List<TracksBean> tracks;
        private List<TrackIdsBean> trackIds;
        private List<?> tags;

        public boolean isSubscribed() {
            return subscribed;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public Object getUpdateFrequency() {
            return updateFrequency;
        }

        public long getBackgroundCoverId() {
            return backgroundCoverId;
        }

        public Object getBackgroundCoverUrl() {
            return backgroundCoverUrl;
        }

        public long getTitleImage() {
            return titleImage;
        }

        public Object getTitleImageUrl() {
            return titleImageUrl;
        }

        public Object getEnglishTitle() {
            return englishTitle;
        }

        public boolean isOpRecommend() {
            return opRecommend;
        }

        public int getAdType() {
            return adType;
        }

        public long getTrackNumberUpdateTime() {
            return trackNumberUpdateTime;
        }

        public long getUserId() {
            return userId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public boolean isHighQuality() {
            return highQuality;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public long getCoverImgId() {
            return coverImgId;
        }

        public boolean isNewImported() {
            return newImported;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public int getSpecialType() {
            return specialType;
        }

        public int getTrackCount() {
            return trackCount;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public int getPrivacy() {
            return privacy;
        }

        public long getTrackUpdateTime() {
            return trackUpdateTime;
        }

        public long getPlayCount() {
            return playCount;
        }

        public int getSubscribedCount() {
            return subscribedCount;
        }

        public int getCloudTrackCount() {
            return cloudTrackCount;
        }

        public boolean isOrdered() {
            return ordered;
        }

        public String getDescription() {
            return description;
        }

        public int getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public long getId() {
            return id;
        }

        public int getShareCount() {
            return shareCount;
        }

        public String getCoverImgId_str() {
            return coverImgId_str;
        }

        public String getToplistType() {
            return ToplistType;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public List<SubscribersBean> getSubscribers() {
            return subscribers;
        }

        public List<TracksBean> getTracks() {
            return tracks;
        }

        public List<TrackIdsBean> getTrackIds() {
            return trackIds;
        }

        public List<?> getTags() {
            return tags;
        }

        public static class CreatorBean {
            private boolean defaultAvatar;
            private int province;
            private int authStatus;
            private boolean followed;
            private String avatarUrl;
            private int accountStatus;
            private int gender;
            private int city;
            private long birthday;
            private long userId;
            private int userType;
            private String nickname;
            private String signature;
            private String description;
            private String detailDescription;
            private long avatarImgId;
            private long backgroundImgId;
            private String backgroundUrl;
            private int authority;
            private boolean mutual;
            private Object expertTags;
            private Object experts;
            private int djStatus;
            private int vipType;
            private Object remarkName;
            private String avatarImgIdStr;
            private String backgroundImgIdStr;
            private String avatarImgId_str;

            public boolean isDefaultAvatar() {
                return defaultAvatar;
            }

            public void setDefaultAvatar(boolean defaultAvatar) {
                this.defaultAvatar = defaultAvatar;
            }

            public int getProvince() {
                return province;
            }

            public int getAuthStatus() {
                return authStatus;
            }

            public boolean isFollowed() {
                return followed;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public int getAccountStatus() {
                return accountStatus;
            }

            public int getGender() {
                return gender;
            }

            public int getCity() {
                return city;
            }

            public long getBirthday() {
                return birthday;
            }

            public long getUserId() {
                return userId;
            }

            public int getUserType() {
                return userType;
            }

            public String getNickname() {
                return nickname;
            }

            public String getSignature() {
                return signature;
            }

            public String getDescription() {
                return description;
            }

            public String getDetailDescription() {
                return detailDescription;
            }

            public long getAvatarImgId() {
                return avatarImgId;
            }

            public long getBackgroundImgId() {
                return backgroundImgId;
            }

            public String getBackgroundUrl() {
                return backgroundUrl;
            }

            public int getAuthority() {
                return authority;
            }

            public boolean isMutual() {
                return mutual;
            }

            public Object getExpertTags() {
                return expertTags;
            }

            public Object getExperts() {
                return experts;
            }

            public int getDjStatus() {
                return djStatus;
            }

            public int getVipType() {
                return vipType;
            }

            public Object getRemarkName() {
                return remarkName;
            }

            public String getAvatarImgIdStr() {
                return avatarImgIdStr;
            }

            public String getBackgroundImgIdStr() {
                return backgroundImgIdStr;
            }

            public String getAvatarImgId_str() {
                return avatarImgId_str;
            }
        }

        public static class SubscribersBean {
            private boolean defaultAvatar;
            private int province;
            private int authStatus;
            private boolean followed;
            private String avatarUrl;
            private int accountStatus;
            private int gender;
            private int city;
            private long birthday;
            private long userId;
            private int userType;
            private String nickname;
            private String signature;
            private String description;
            private String detailDescription;
            private long avatarImgId;
            private long backgroundImgId;
            private String backgroundUrl;
            private int authority;
            private boolean mutual;
            private Object expertTags;
            private Object experts;
            private int djStatus;
            private int vipType;
            private Object remarkName;
            private String avatarImgIdStr;
            private String backgroundImgIdStr;
            private String avatarImgId_str;

            public boolean isDefaultAvatar() {
                return defaultAvatar;
            }

            public int getProvince() {
                return province;
            }

            public int getAuthStatus() {
                return authStatus;
            }

            public boolean isFollowed() {
                return followed;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public int getAccountStatus() {
                return accountStatus;
            }

            public int getGender() {
                return gender;
            }

            public int getCity() {
                return city;
            }

            public long getBirthday() {
                return birthday;
            }

            public long getUserId() {
                return userId;
            }

            public int getUserType() {
                return userType;
            }

            public String getNickname() {
                return nickname;
            }

            public String getSignature() {
                return signature;
            }

            public String getDescription() {
                return description;
            }

            public String getDetailDescription() {
                return detailDescription;
            }

            public long getAvatarImgId() {
                return avatarImgId;
            }

            public long getBackgroundImgId() {
                return backgroundImgId;
            }

            public String getBackgroundUrl() {
                return backgroundUrl;
            }

            public int getAuthority() {
                return authority;
            }

            public boolean isMutual() {
                return mutual;
            }

            public Object getExpertTags() {
                return expertTags;
            }

            public Object getExperts() {
                return experts;
            }

            public int getDjStatus() {
                return djStatus;
            }

            public int getVipType() {
                return vipType;
            }

            public Object getRemarkName() {
                return remarkName;
            }

            public String getAvatarImgIdStr() {
                return avatarImgIdStr;
            }

            public String getBackgroundImgIdStr() {
                return backgroundImgIdStr;
            }

            public String getAvatarImgId_str() {
                return avatarImgId_str;
            }

        }

        public static class TracksBean {
            private String name;
            private long id;
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
            private Object noCopyrightRcmd;
            private int rtype;
            private Object rurl;
            private int mst;
            private int cp;
            private int mv;
            private long publishTime;
            private List<ArBean> ar;
            private List<String> alia;
            private List<?> rtUrls;
            private List<String> tns;

            public String getName() {
                return name;
            }

            public long getId() {
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

            public List<String> getAlia() {
                return alia;
            }

            public List<?> getRtUrls() {
                return rtUrls;
            }

            public List<String> getTns() {
                return tns;
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
                private float vd;

                public int getBr() {
                    return br;
                }

                public int getFid() {
                    return fid;
                }

                public int getSize() {
                    return size;
                }

                public float getVd() {
                    return vd;
                }

            }

            public static class MBean {
                private int br;
                private int fid;
                private int size;
                private float vd;

                public int getBr() {
                    return br;
                }

                public int getFid() {
                    return fid;
                }

                public int getSize() {
                    return size;
                }

                public float getVd() {
                    return vd;
                }
            }

            public static class LBean {
                private int br;
                private int fid;
                private int size;
                private float vd;

                public int getBr() {
                    return br;
                }

                public int getFid() {
                    return fid;
                }

                public int getSize() {
                    return size;
                }

                public float getVd() {
                    return vd;
                }
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

        public static class TrackIdsBean {
            private long id;
            private int v;
            private long at;
            private Object alg;
            private int ratio;
            private int lr;

            public long getId() {
                return id;
            }

            public int getV() {
                return v;
            }

            public long getAt() {
                return at;
            }

            public Object getAlg() {
                return alg;
            }

            public int getRatio() {
                return ratio;
            }

            public int getLr() {
                return lr;
            }
        }

    }

    public static class PrivilegesBean {
        private long id;
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

        public long getId() {
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
    }
}
