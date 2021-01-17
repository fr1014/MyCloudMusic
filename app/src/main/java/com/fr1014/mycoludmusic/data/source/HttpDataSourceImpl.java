package com.fr1014.mycoludmusic.data.source;

import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWNewSearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.kuwo.KWSongInfoAndLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.UserEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLevelInfo;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeMusic;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSearchDetail;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYSongLrcEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYUserPlayList;
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.http.api.KWApiService;
import com.fr1014.mycoludmusic.http.api.WYApiService;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class HttpDataSourceImpl implements HttpDataSource {
    private volatile static HttpDataSourceImpl instance = null;
    private WYApiService wyApiService;
    private KWApiService kwApiService;

    public static HttpDataSourceImpl getInstance(WYApiService wyApiService,KWApiService kwApiService) {
        if (instance == null){
            synchronized (HttpDataSourceImpl.class){
                if (instance == null){
                    instance = new HttpDataSourceImpl(wyApiService,kwApiService);
                }
            }
        }
        return instance;
    }

    private HttpDataSourceImpl(WYApiService wyApiService,KWApiService kwApiService){
        this.wyApiService = wyApiService;
        this.kwApiService = kwApiService;
    }

    @Override
    public Observable<ResponseBody> getSongCover(String coverPath) {
        return kwApiService.getSongCover(coverPath);
    }

    @Override
    public Observable<UserEntity> getWYUserProfile(String phone, String password) {
        return wyApiService.getWYUserProfile(phone, password);
    }

    @Override
    public Observable<WYLikeMusic> likeMusicWY(long id, boolean like) {
        return wyApiService.likeMusicWY(id,like);
    }

    @Override
    public Observable<WYLikeList> getWYLikeList(long uid) {
        return wyApiService.getWYLikeList(uid);
    }

    @Override
    public Observable<WYUserPlayList> getWYUserPlayList(long uid) {
        return wyApiService.getWYUserPlayList(uid);
    }

    @Override
    public Observable<NetizensPlaylist> getWYNetizensPlayList(String order, String cat, int limit, int offset) {
        return wyApiService.getWYNetizensPlaylist(order, cat, limit, offset);
    }

    @Override
    public Observable<WYLevelInfo> getWYLevelInfo() {
        return wyApiService.getWYLevelInfo();
    }

    @Override
    public Observable<TopListEntity> getTopList() {
        return wyApiService.getTopList();
    }

    @Override
    public Observable<TopListDetailEntity> getTopListDetail() {
        return wyApiService.getTopListDetail();
    }

    @Override
    public Observable<PlayListDetailEntity> getTopList(long id) {
        return wyApiService.getTopList(id);
    }

    @Override
    public Observable<PlayListDetailEntity> getPlayListDetail(long id) {
        return wyApiService.getPlayListDetail(id);
    }

    @Override
    public Observable<SongDetailEntity> getWYSongDetail(String ids) {
        return wyApiService.getWYSongDetail(ids);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return wyApiService.checkMusic(id);
    }

    @Override
    public Observable<SongUrlEntity> getWYSongUrl(String ids) {
        return wyApiService.getWYSongUrl(ids);
    }

    @Override
    public Observable<SongUrlEntity> getWYSongUrl(long id) {
        return wyApiService.getWYSongUrl(id);
    }

    @Override
    public Observable<WYSearchDetail> getWYSearch(String keywords, int offset) {
        return wyApiService.getWYSearch(keywords, offset);
    }

    @Override
    public Observable<WYSongLrcEntity> getWYSongLrcEntity(long id) {
        return wyApiService.getWYSongLrcEntity(id);
    }

    @Override
    public Observable<RecommendPlayList> getWYRecommendPlayList(int limit) {
        return wyApiService.getWYRecommendPlayList(limit);
    }

//    @Override
//    public Observable<KWSearchEntity> getSearch(String name, int page, int count) {
//        return wyApiService.getSearchEntity(name, page, count);
//    }

    @Override
    public Observable<ResponseBody> getKWSongUrl(String rid) {
        return kwApiService.getKWSongUrl(rid);
    }

    @Override
    public Observable<ResponseBody> getSearchResult(String name, int count) {
        return kwApiService.getSearchResult(name, count);
    }

    @Override
    public Observable<KWNewSearchEntity> getKWSearchResult(String name, int page, int count) {
        return kwApiService.getKWSearchResult(name, page, count);
    }

    @Override
    public Observable<KWSongDetailEntity> getKWSongDetail(long mid) {
        return kwApiService.getKWSongDetail(mid);
    }

    @Override
    public Observable<KWSongInfoAndLrcEntity> getKWSongInfoAndLrc(String mid) {
        return kwApiService.getKWSongInfoAndLrc(mid);
    }
}
