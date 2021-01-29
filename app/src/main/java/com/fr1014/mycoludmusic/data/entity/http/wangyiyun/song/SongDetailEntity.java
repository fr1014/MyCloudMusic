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
}

