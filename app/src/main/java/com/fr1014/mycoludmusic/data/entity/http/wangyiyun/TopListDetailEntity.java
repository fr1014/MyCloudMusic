package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import java.util.List;

public class TopListDetailEntity{
    private int code;
    private ArtistToplistBean artistToplist;
    private RewardToplistBean rewardToplist;
    private List<ListBean> list;

    public int getCode() {
        return code;
    }

    public ArtistToplistBean getArtistToplist() {
        return artistToplist;
    }

    public RewardToplistBean getRewardToplist() {
        return rewardToplist;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ArtistToplistBean {
        private String coverUrl;
        private String name;
        private String upateFrequency;
        private int position;
        private String updateFrequency;
        private List<ArtistsBean> artists;

        public String getCoverUrl() {
            return coverUrl;
        }

        public String getName() {
            return name;
        }

        public String getUpateFrequency() {
            return upateFrequency;
        }

        public int getPosition() {
            return position;
        }

        public String getUpdateFrequency() {
            return updateFrequency;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public static class ArtistsBean {
            private String first;
            private String second;
            private int third;

            public String getFirst() {
                return first;
            }

            public String getSecond() {
                return second;
            }

            public int getThird() {
                return third;
            }
        }
    }

    public static class RewardToplistBean {
        private String coverUrl;
        private String name;
        private int position;
        private List<SongsBean> songs;

        public String getCoverUrl() {
            return coverUrl;
        }

        public String getName() {
            return name;
        }

        public int getPosition() {
            return position;
        }

        public List<SongsBean> getSongs() {
            return songs;
        }

        public static class SongsBean {
            private String name;
            private long id;
            private int position;
            private int status;
            private int fee;
            private int copyrightId;
            private String disc;
            private int no;
            private AlbumBean album;
            private boolean starred;
            private int popularity;
            private int score;
            private int starredNum;
            private int duration;
            private int playedNum;
            private int dayPlays;
            private int hearTime;
            private String ringtone;
            private Object crbt;
            private Object audition;
            private String copyFrom;
            private String commentThreadId;
            private Object rtUrl;
            private int ftype;
            private int copyright;
            private Object transName;
            private Object sign;
            private int mark;
            private Object noCopyrightRcmd;
            private int mvid;
            private BMusicBean bMusic;
            private Object mp3Url;
            private HMusicBean hMusic;
            private MMusicBean mMusic;
            private LMusicBean lMusic;
            private int rtype;
            private Object rurl;
            private List<?> alias;
            private List<ArtistsBeanXX> artists;
            private List<?> rtUrls;

            public String getName() {
                return name;
            }

            public long getId() {
                return id;
            }

            public int getPosition() {
                return position;
            }

            public int getStatus() {
                return status;
            }

            public int getFee() {
                return fee;
            }

            public int getCopyrightId() {
                return copyrightId;
            }

            public String getDisc() {
                return disc;
            }

            public int getNo() {
                return no;
            }

            public AlbumBean getAlbum() {
                return album;
            }

            public boolean isStarred() {
                return starred;
            }

            public int getPopularity() {
                return popularity;
            }

            public int getScore() {
                return score;
            }

            public int getStarredNum() {
                return starredNum;
            }

            public int getDuration() {
                return duration;
            }

            public int getPlayedNum() {
                return playedNum;
            }

            public int getDayPlays() {
                return dayPlays;
            }

            public int getHearTime() {
                return hearTime;
            }

            public String getRingtone() {
                return ringtone;
            }

            public Object getCrbt() {
                return crbt;
            }

            public Object getAudition() {
                return audition;
            }

            public String getCopyFrom() {
                return copyFrom;
            }

            public String getCommentThreadId() {
                return commentThreadId;
            }

            public Object getRtUrl() {
                return rtUrl;
            }

            public int getFtype() {
                return ftype;
            }

            public int getCopyright() {
                return copyright;
            }

            public Object getTransName() {
                return transName;
            }

            public Object getSign() {
                return sign;
            }

            public int getMark() {
                return mark;
            }

            public Object getNoCopyrightRcmd() {
                return noCopyrightRcmd;
            }

            public int getMvid() {
                return mvid;
            }

            public BMusicBean getBMusic() {
                return bMusic;
            }

            public Object getMp3Url() {
                return mp3Url;
            }

            public HMusicBean getHMusic() {
                return hMusic;
            }

            public MMusicBean getMMusic() {
                return mMusic;
            }

            public LMusicBean getLMusic() {
                return lMusic;
            }

            public int getRtype() {
                return rtype;
            }

            public Object getRurl() {
                return rurl;
            }

            public List<?> getAlias() {
                return alias;
            }

            public List<ArtistsBeanXX> getArtists() {
                return artists;
            }

            public List<?> getRtUrls() {
                return rtUrls;
            }

            public static class AlbumBean {
                private String name;
                private long id;
                private String type;
                private int size;
                private long picId;
                private String blurPicUrl;
                private int companyId;
                private long pic;
                private String picUrl;
                private long publishTime;
                private String description;
                private String tags;
                private Object company;
                private String briefDesc;
                private ArtistBean artist;
                private int status;
                private int copyrightId;
                private String commentThreadId;
                private String subType;
                private Object transName;
                private int mark;
                private String picId_str;
                private List<?> songs;
                private List<?> alias;
                private List<ArtistsBeanX> artists;

                public String getName() {
                    return name;
                }

                public long getId() {
                    return id;
                }

                public String getType() {
                    return type;
                }

                public int getSize() {
                    return size;
                }

                public long getPicId() {
                    return picId;
                }

                public String getBlurPicUrl() {
                    return blurPicUrl;
                }

                public int getCompanyId() {
                    return companyId;
                }

                public long getPic() {
                    return pic;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public long getPublishTime() {
                    return publishTime;
                }

                public String getDescription() {
                    return description;
                }

                public String getTags() {
                    return tags;
                }

                public Object getCompany() {
                    return company;
                }

                public String getBriefDesc() {
                    return briefDesc;
                }

                public ArtistBean getArtist() {
                    return artist;
                }

                public int getStatus() {
                    return status;
                }

                public int getCopyrightId() {
                    return copyrightId;
                }

                public String getCommentThreadId() {
                    return commentThreadId;
                }

                public String getSubType() {
                    return subType;
                }

                public Object getTransName() {
                    return transName;
                }

                public int getMark() {
                    return mark;
                }

                public String getPicId_str() {
                    return picId_str;
                }

                public List<?> getSongs() {
                    return songs;
                }

                public List<?> getAlias() {
                    return alias;
                }

                public List<ArtistsBeanX> getArtists() {
                    return artists;
                }

                public static class ArtistBean {
                    private String name;
                    private long id;
                    private long picId;
                    private long img1v1Id;
                    private String briefDesc;
                    private String picUrl;
                    private String img1v1Url;
                    private int albumSize;
                    private String trans;
                    private int musicSize;
                    private int topicPerson;
                    private List<?> alias;

                    public String getName() {
                        return name;
                    }

                    public long getId() {
                        return id;
                    }

                    public long getPicId() {
                        return picId;
                    }

                    public long getImg1v1Id() {
                        return img1v1Id;
                    }

                    public String getBriefDesc() {
                        return briefDesc;
                    }

                    public String getPicUrl() {
                        return picUrl;
                    }

                    public String getImg1v1Url() {
                        return img1v1Url;
                    }

                    public int getAlbumSize() {
                        return albumSize;
                    }

                    public String getTrans() {
                        return trans;
                    }

                    public int getMusicSize() {
                        return musicSize;
                    }

                    public int getTopicPerson() {
                        return topicPerson;
                    }

                    public List<?> getAlias() {
                        return alias;
                    }
                }

                public static class ArtistsBeanX{
                    private String name;
                    private long id;
                    private long picId;
                    private long img1v1Id;
                    private String briefDesc;
                    private String picUrl;
                    private String img1v1Url;
                    private int albumSize;
                    private String trans;
                    private int musicSize;
                    private int topicPerson;
                    private List<?> alias;

                    public String getName() {
                        return name;
                    }

                    public long getId() {
                        return id;
                    }

                    public long getPicId() {
                        return picId;
                    }

                    public long getImg1v1Id() {
                        return img1v1Id;
                    }

                    public String getBriefDesc() {
                        return briefDesc;
                    }

                    public String getPicUrl() {
                        return picUrl;
                    }

                    public String getImg1v1Url() {
                        return img1v1Url;
                    }

                    public int getAlbumSize() {
                        return albumSize;
                    }

                    public String getTrans() {
                        return trans;
                    }

                    public int getMusicSize() {
                        return musicSize;
                    }

                    public int getTopicPerson() {
                        return topicPerson;
                    }

                    public List<?> getAlias() {
                        return alias;
                    }
                }
            }

            public static class BMusicBean {
                private Object name;
                private long id;
                private int size;
                private String extension;
                private int sr;
                private int dfsId;
                private int bitrate;
                private int playTime;
                private int volumeDelta;

                public Object getName() {
                    return name;
                }

                public long getId() {
                    return id;
                }

                public int getSize() {
                    return size;
                }

                public String getExtension() {
                    return extension;
                }

                public int getSr() {
                    return sr;
                }

                public int getDfsId() {
                    return dfsId;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public int getPlayTime() {
                    return playTime;
                }

                public int getVolumeDelta() {
                    return volumeDelta;
                }
            }

            public static class HMusicBean {
                private Object name;
                private long id;
                private int size;
                private String extension;
                private int sr;
                private int dfsId;
                private int bitrate;
                private int playTime;
                private int volumeDelta;

                public Object getName() {
                    return name;
                }

                public long getId() {
                    return id;
                }

                public int getSize() {
                    return size;
                }

                public String getExtension() {
                    return extension;
                }

                public int getSr() {
                    return sr;
                }

                public int getDfsId() {
                    return dfsId;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public int getPlayTime() {
                    return playTime;
                }

                public int getVolumeDelta() {
                    return volumeDelta;
                }
            }

            public static class MMusicBean {
                private Object name;
                private long id;
                private int size;
                private String extension;
                private int sr;
                private int dfsId;
                private int bitrate;
                private int playTime;
                private int volumeDelta;

                public Object getName() {
                    return name;
                }

                public long getId() {
                    return id;
                }

                public int getSize() {
                    return size;
                }

                public String getExtension() {
                    return extension;
                }

                public int getSr() {
                    return sr;
                }

                public int getDfsId() {
                    return dfsId;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public int getPlayTime() {
                    return playTime;
                }

                public int getVolumeDelta() {
                    return volumeDelta;
                }
            }

            public static class LMusicBean {
                private Object name;
                private long id;
                private int size;
                private String extension;
                private int sr;
                private int dfsId;
                private int bitrate;
                private int playTime;
                private int volumeDelta;

                public Object getName() {
                    return name;
                }

                public long getId() {
                    return id;
                }

                public int getSize() {
                    return size;
                }

                public String getExtension() {
                    return extension;
                }

                public int getSr() {
                    return sr;
                }

                public int getDfsId() {
                    return dfsId;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public int getPlayTime() {
                    return playTime;
                }

                public int getVolumeDelta() {
                    return volumeDelta;
                }
            }

            public static class ArtistsBeanXX {
                private String name;
                private long id;
                private int picId;
                private int img1v1Id;
                private String briefDesc;
                private String picUrl;
                private String img1v1Url;
                private int albumSize;
                private String trans;
                private int musicSize;
                private int topicPerson;
                private List<?> alias;

                public String getName() {
                    return name;
                }

                public long getId() {
                    return id;
                }

                public int getPicId() {
                    return picId;
                }

                public int getImg1v1Id() {
                    return img1v1Id;
                }

                public String getBriefDesc() {
                    return briefDesc;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public String getImg1v1Url() {
                    return img1v1Url;
                }

                public int getAlbumSize() {
                    return albumSize;
                }

                public String getTrans() {
                    return trans;
                }

                public int getMusicSize() {
                    return musicSize;
                }

                public int getTopicPerson() {
                    return topicPerson;
                }

                public List<?> getAlias() {
                    return alias;
                }
            }
        }
    }

    public static class ListBean {
        private Object subscribed;
        private Object creator;
        private Object artists;
        private String updateFrequency;
        private int backgroundCoverId;
        private Object backgroundCoverUrl;
        private int titleImage;
        private Object titleImageUrl;
        private Object englishTitle;
        private boolean opRecommend;
        private Object recommendInfo;
        private int adType;
        private long trackNumberUpdateTime;
        private long userId;
        private long createTime;
        private boolean highQuality;
        private int specialType;
        private boolean newImported;
        private boolean anonimous;
        private long updateTime;
        private long coverImgId;
        private String coverImgUrl;
        private int trackCount;
        private String commentThreadId;
        private int totalDuration;
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
        private String coverImgId_str;
        private String ToplistType;
        private List<?> subscribers;
        private List<TracksBean> tracks;
        private List<?> tags;

        public Object getSubscribed() {
            return subscribed;
        }

        public Object getCreator() {
            return creator;
        }

        public Object getArtists() {
            return artists;
        }

        public String getUpdateFrequency() {
            return updateFrequency;
        }

        public int getBackgroundCoverId() {
            return backgroundCoverId;
        }

        public Object getBackgroundCoverUrl() {
            return backgroundCoverUrl;
        }

        public int getTitleImage() {
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

        public Object getRecommendInfo() {
            return recommendInfo;
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

        public int getSpecialType() {
            return specialType;
        }

        public boolean isNewImported() {
            return newImported;
        }

        public boolean isAnonimous() {
            return anonimous;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public long getCoverImgId() {
            return coverImgId;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public int getTrackCount() {
            return trackCount;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public int getTotalDuration() {
            return totalDuration;
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

        public String getCoverImgId_str() {
            return coverImgId_str;
        }

        public String getToplistType() {
            return ToplistType;
        }

        public List<?> getSubscribers() {
            return subscribers;
        }

        public List<TracksBean> getTracks() {
            return tracks;
        }

        public List<?> getTags() {
            return tags;
        }

        public static class TracksBean {
            private String first;
            private String second;

            public String getFirst() {
                return first;
            }

            public String getSecond() {
                return second;
            }
        }
    }
}