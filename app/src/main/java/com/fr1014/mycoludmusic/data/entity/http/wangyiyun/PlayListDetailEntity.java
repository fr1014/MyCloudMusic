package com.fr1014.mycoludmusic.data.entity.http.wangyiyun;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Creator;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Subscribers;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Track;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.TrackIds;

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
        public Creator creator;
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
        public List<Subscribers> subscribers;
        public List<Track> tracks;
        public List<TrackIds> trackIds;
        private List<?> tags;

        public boolean isSubscribed() {
            return subscribed;
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

        public List<?> getTags() {
            return tags;
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
