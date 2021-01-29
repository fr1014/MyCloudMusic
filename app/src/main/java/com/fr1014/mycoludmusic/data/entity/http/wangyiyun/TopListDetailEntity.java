package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongsBean;

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