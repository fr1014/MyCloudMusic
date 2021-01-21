package com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.Privileges;

import java.util.List;

/**
 * 创建时间:2020/9/2
 * 作者:fr
 * 邮箱:1546352238@qq.com
 * 获取歌单详情
 */
public class PlayListDetailEntity {

    public int code;
    public Object relatedVideos;
    public Playlist playlist;
    public Object urls;
    public List<Privileges> privileges;
}
