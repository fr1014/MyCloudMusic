package com.fr1014.mycoludmusic.data.source;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.data.source.http.KWApiService;
import com.fr1014.mycoludmusic.data.source.http.WYApiService;

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
    public Observable<SongDetailEntity> getSongDetail(long ids) {
        return wyApiService.getSongDetail(ids);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return wyApiService.checkMusic(id);
    }

    @Override
    public Observable<SongUrlEntity> getSongUrl(long id) {
        return wyApiService.getSongUrl(id);
    }

    @Override
    public Observable<SearchEntity> getSearch(String keywords, int offset) {
        return wyApiService.getSearch(keywords, offset);
    }

    @Override
    public Observable<com.fr1014.mycoludmusic.data.entity.http.kuwo.SearchEntity> getSearch(String name, int page, int count) {
        return kwApiService.getSearchEntity(name, page, count);
    }

    @Override
    public Observable<ResponseBody> getSongUrl(String rid) {
        return kwApiService.getSongUrl(rid);
    }
}
