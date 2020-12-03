package com.fr1014.mycoludmusic.data;

import androidx.lifecycle.LiveData;

import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.data.source.http.HttpDataSource;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.CheckEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SearchEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.SongUrlEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.TopListEntity;
import com.fr1014.mycoludmusic.data.source.local.room.LocalDataSource;
import com.fr1014.mymvvm.base.BaseModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 创建时间:2020/9/4
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class DataRepository extends BaseModel implements HttpDataSource, LocalDataSource {

    private volatile static DataRepository instance = null;
    private HttpDataSource httpDataSource;
    private LocalDataSource localDataSource;

    private DataRepository(HttpDataSource httpDataSource, LocalDataSource localDataSource) {
        this.httpDataSource = httpDataSource;
        this.localDataSource = localDataSource;
    }

    public static DataRepository getInstance(HttpDataSource httpDataSource, LocalDataSource localDataSource) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(httpDataSource, localDataSource);
                }
            }
        }
        return instance;
    }

    @Override
    public Observable<TopListEntity> getTopList() {
        return httpDataSource.getTopList();
    }

    @Override
    public Observable<TopListDetailEntity> getTopListDetail() {
        return httpDataSource.getTopListDetail();
    }

    @Override
    public Observable<PlayListDetailEntity> getTopList(long id) {
        return httpDataSource.getTopList(id);
    }

    @Override
    public Observable<PlayListDetailEntity> getPlayListDetail(long id) {
        return httpDataSource.getPlayListDetail(id);
    }

    @Override
    public Observable<SongDetailEntity> getSongDetail(long ids) {
        return httpDataSource.getSongDetail(ids);
    }

    @Override
    public Observable<SongUrlEntity> getSongUrl(long id) {
        return httpDataSource.getSongUrl(id);
    }

    @Override
    public Observable<CheckEntity> checkMusic(long id) {
        return httpDataSource.checkMusic(id);
    }

    @Override
    public Observable<SearchEntity> getSearch(String keywords, int offset) {
        return httpDataSource.getSearch(keywords, offset);
    }

    @Override
    public Observable<com.fr1014.mycoludmusic.data.entity.http.kuwo.SearchEntity> getSearch(String name, int page, int count) {
        return httpDataSource.getSearch(name, page, count);
    }

    @Override
    public Observable<ResponseBody> getSongUrl(String rid) {
        return httpDataSource.getSongUrl(rid);
    }

    @Override
    public LiveData<List<MusicEntity>> getAllLive() {
        return localDataSource.getAllLive();
    }

    @Override
    public LiveData<MusicEntity> getItemLive(String title, String artist) {
        return localDataSource.getItemLive(title, artist);
    }

    @Override
    public List<MusicEntity> getAll() {
        return localDataSource.getAll();
    }

    @Override
    public MusicEntity getItem(String title, String artist) {
        return localDataSource.getItem(title, artist);
    }

    @Override
    public void insertAll(List<MusicEntity> musicEntities) {
        localDataSource.insertAll(musicEntities);
    }

    @Override
    public void insert(MusicEntity musicEntity) {
        localDataSource.insert(musicEntity);
    }

    @Override
    public void delete(MusicEntity musicEntity) {
        localDataSource.delete(musicEntity);
    }
}
