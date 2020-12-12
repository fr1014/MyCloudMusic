package com.fr1014.mycoludmusic.data.source.http;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public interface HttpDataSource {
 /*
  ========================网易云===================================
*/
    Observable<TopListEntity> getTopList();

    Observable<TopListDetailEntity> getTopListDetail();

    Observable<PlayListDetailEntity> getTopList(long id);

    Observable<PlayListDetailEntity> getPlayListDetail(long id);

    Observable<SongDetailEntity> getSongDetail(long ids);

    Observable<CheckEntity> checkMusic(long id);

    Observable<SongUrlEntity> getWYSongUrl(String id);
    Observable<SongUrlEntity> getWYSongUrl(long id);

    Observable<WYSearchEntity> getSearch(String keywords, int offset);

/*
===============================酷我========================================
*/
    Observable<KWSearchEntity> getSearch(String name, int page, int count);
    Observable<ResponseBody> getKWSongUrl(String rid);
    Observable<ResponseBody> getSearchResult(String name,int count);
    Observable<KWNewSearchEntity> getKWSearchResult(String name, int page, int count);
    Observable<KWSongDetailEntity> getKWSongDetail(long mid);
    Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(String mid);
}
