package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import java.util.List;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 *
 * 所有榜单
 */
public class TopListEntity {
    private int code;
    private ArtistToplistBean artistToplist;
    private List<ListBean> list;

    public int getCode() {
        return code;
    }

    public ArtistToplistBean getArtistToplist() {
        return artistToplist;
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
    }

    public static class ListBean {
        private Object subscribed;
        private Object creator;
        private Object artists;
        private Object tracks;
        private String updateFrequency;
        private int backgroundCoverId;
        private Object backgroundCoverUrl;
        private int titleImage;
        private Object titleImageUrl;
        private Object englishTitle;
        private boolean opRecommend;
        private Object recommendInfo;
        private long trackNumberUpdateTime;
        private int adType;
        private int subscribedCount;
        private int cloudTrackCount;
        private long userId;
        private long createTime;
        private boolean highQuality;
        private long updateTime;
        private long coverImgId;
        private boolean newImported;
        private boolean anonimous;
        private int specialType;
        private int totalDuration;
        private String coverImgUrl;
        private int trackCount;
        private String commentThreadId;
        private int privacy;
        private long trackUpdateTime;
        private long playCount;
        private boolean ordered;
        private String description;
        private int status;
        private String name;
        private long id;
        private String coverImgId_str;
        private String ToplistType;
        private List<?> subscribers;
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

        public Object getTracks() {
            return tracks;
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

        public long getTrackNumberUpdateTime() {
            return trackNumberUpdateTime;
        }

        public int getAdType() {
            return adType;
        }

        public int getSubscribedCount() {
            return subscribedCount;
        }

        public int getCloudTrackCount() {
            return cloudTrackCount;
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

        public boolean isAnonimous() {
            return anonimous;
        }

        public int getSpecialType() {
            return specialType;
        }

        public int getTotalDuration() {
            return totalDuration;
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

        public int getPrivacy() {
            return privacy;
        }

        public long getTrackUpdateTime() {
            return trackUpdateTime;
        }

        public long getPlayCount() {
            return playCount;
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

        public List<?> getTags() {
            return tags;
        }
    }
}
