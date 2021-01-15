package com.fr1014.mycoludmusic.data.source.http;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.UserEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLevelInfo;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeMusic;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSongLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYUserPlayList;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public interface HttpDataSource {
    /*
      ========================项目===================================
    */
    Observable<ResponseBody> getSongCover(String coverPath);

    /*
     ========================网易云===================================
   */
    Observable<UserEntity> getWYUserProfile(String phone, String password);

    Observable<WYLikeMusic> likeMusicWY(long id,boolean like);

    Observable<WYLikeList> getWYLikeList(long uid);

    Observable<WYUserPlayList> getWYUserPlayList(long uid);

    Observable<WYLevelInfo> getWYLevelInfo();

    Observable<TopListEntity> getTopList();

    Observable<TopListDetailEntity> getTopListDetail();

    Observable<PlayListDetailEntity> getTopList(long id);

    Observable<PlayListDetailEntity> getPlayListDetail(long id);

    Observable<SongDetailEntity> getSongDetail(long ids);

    Observable<CheckEntity> checkMusic(long id);

    Observable<SongUrlEntity> getWYSongUrl(String id);

    Observable<SongUrlEntity> getWYSongUrl(long id);

    Observable<WYSearchDetail> getWYSearch(String keywords, int offset);

    Observable<WYSongLrcEntity> getWYSongLrcEntity(long id);

    Observable<RecommendPlayList> getWYRecommendPlayList(int limit);

    /*
    ===============================酷我========================================
    */
//    Observable<KWSearchEntity> getSearch(String name, int page, int count);
    Observable<ResponseBody> getKWSongUrl(String rid);

    Observable<ResponseBody> getSearchResult(String name, int count);

    Observable<KWNewSearchEntity> getKWSearchResult(String name, int page, int count);

    Observable<KWSongDetailEntity> getKWSongDetail(long mid);

    Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(String mid);
}

