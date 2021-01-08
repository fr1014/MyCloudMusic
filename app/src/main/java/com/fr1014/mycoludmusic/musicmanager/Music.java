package com.fr1014.mycoludmusic.musicmanager;

/**
 * 创建时间:2020/9/7
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class Music {
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

    public Music(long id, String artist, String title, String songUrl, String imgUrl, String MUSICRID) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.songUrl = songUrl;
        this.imgUrl = imgUrl;
        this.MUSICRID = MUSICRID;
    }

    public Music(long id, String artist, String title, String songUrl, String imgUrl, String MUSICRID, long duration) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.songUrl = songUrl;
        this.imgUrl = imgUrl;
        this.MUSICRID = MUSICRID;
        this.duration = duration;
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
}
