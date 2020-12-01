package com.fr1014.mycoludmusic.musicmanager;

/**
 * 创建时间:2020/9/7
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class Music {
    private long id;            //网易歌曲id
    private String artist;     //歌手
    private String title;      //歌曲名
    private String songUrl;    //歌曲地址
    private String imgUrl;     //专辑图片地址
    private String MUSICRID;   //酷我musicID
    private boolean isOnlineMusic;

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

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", songUrl='" + songUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", isOnlineMusic=" + isOnlineMusic +
                '}';
    }
}
