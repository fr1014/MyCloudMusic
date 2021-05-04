package com.fr1014.mycoludmusic.musicmanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建时间:2020/9/7
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class Music implements Parcelable {
    private long id;                //网易歌曲id
    private String artist;          //歌手
    private String title;           //歌曲名
    private String subTitle;        //
    private String songUrl;         //歌曲地址
    private String imgUrl;          //歌曲图片地址
    private String MUSICRID;        //酷我musicID
    private long duration;          // 持续时间
    private String original;        //是否为原唱 1为原唱，0为非原唱
    private String album;           //歌曲专辑
    private long mvId;              //MV_Id
    private String mvUrl;           //Mv地址
    private boolean isOnlineMusic = true;  //是否为在线歌曲

    public Music() {
    }

    public Music(String artist, String title, String songUrl, String imgUrl, boolean isOnlineMusic) {
        this.artist = artist;
        this.title = title;
        this.songUrl = songUrl;
        this.imgUrl = imgUrl;
        this.isOnlineMusic = isOnlineMusic;
    }

    public Music(long id, String artist, String title, String songUrl, String imgUrl, String MUSICRID, long mvId) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.songUrl = songUrl;
        this.imgUrl = imgUrl;
        this.MUSICRID = MUSICRID;
        this.mvId = mvId;
    }

    public Music(long id, String artist, String title, String songUrl, String imgUrl, String MUSICRID, long duration, long mvId) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.songUrl = songUrl;
        this.imgUrl = imgUrl;
        this.MUSICRID = MUSICRID;
        this.duration = duration;
        this.mvId = mvId;
    }

    public String getMUSICRID() {
        return MUSICRID;
    }

    public void setMUSICRID(String MUSICRID) {
        this.MUSICRID = MUSICRID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isOnlineMusic() {
        return isOnlineMusic;
    }

    public void setOnlineMusic(boolean onlineMusic) {
        isOnlineMusic = onlineMusic;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public long getMvId() {
        return mvId;
    }

    public void setMvId(long mvId) {
        this.mvId = mvId;
    }

    public String getMvUrl() {
        return mvUrl;
    }

    public void setMvUrl(String mvUrl) {
        this.mvUrl = mvUrl;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", songUrl='" + songUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", MUSICRID='" + MUSICRID + '\'' +
                ", duration=" + duration +
                ", original='" + original + '\'' +
                ", album='" + album + '\'' +
                ", isOnlineMusic=" + isOnlineMusic +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.artist);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeString(this.songUrl);
        dest.writeString(this.imgUrl);
        dest.writeString(this.MUSICRID);
        dest.writeLong(this.duration);
        dest.writeString(this.original);
        dest.writeString(this.album);
        dest.writeByte(this.isOnlineMusic ? (byte) 1 : (byte) 0);
    }

    protected Music(Parcel in) {
        this.id = in.readLong();
        this.artist = in.readString();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.songUrl = in.readString();
        this.imgUrl = in.readString();
        this.MUSICRID = in.readString();
        this.duration = in.readLong();
        this.original = in.readString();
        this.album = in.readString();
        this.isOnlineMusic = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}
