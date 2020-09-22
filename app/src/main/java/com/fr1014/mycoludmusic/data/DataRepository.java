package com.fr1014.mycoludmusic.data;

import com.fr1014.mycoludmusic.data.source.http.ApiService;
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.entity.CheckEntity;
import com.fr1014.mycoludmusic.entity.PlayListDetailEntity;
import com.fr1014.mycoludmusic.entity.SearchEntity;
import com.fr1014.mycoludmusic.entity.SongDetailEntity;
import com.fr1014.mycoludmusic.entity.SongUrlEntity;
import com.fr1014.mycoludmusic.entity.TopListDetailEntity;
import com.fr1014.mycoludmusic.entity.TopListEntity;
import com.fr1014.mymvvm.base.BaseModel;

import io.reactivex.Observable;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class DataRepository extends BaseModel implements HttpDataSource {

    private volatile static DataRepository instance = null;
    private ApiService apiService;

    private DataRepository(ApiService apiService){
        this.apiService = apiService;
    }

    public static DataRepository getInstance(ApiService apiService){
        if (instance == null){
            synchronized (DataRepository.class){
                if (instance == null){
                    instance = new DataRepository(apiService);
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<TopListEntity> getTopList() {
        return apiService.getTopList();
    }

    @Override
    public Observable<TopListDetailEntity> getTopListDetail() {
        return apiService.getTopListDetail();
    }

    @Override
    public Observable<PlayListDetailEntity> getPlayListDetail(long id) {
        return apiService.getPlayListDetail(id);
    }

    @Override
    public Observable<SongDetailEntity> getSongDetail(long ids) {
        return apiService.getSongDetail(ids);
    }

    @Override
    public Observable<SongUrlEntity> getSongUrl(long id) {
        return apiService.getSongUrl(id);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return apiService.checkMusic(id);
    }

    @Override
    public Observable<SearchEntity> getSearch(String keywords,int offset) {
        return apiService.getSearch(keywords,offset);
    }
}
