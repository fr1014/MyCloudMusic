package com.fr1014.mycoludmusic.ui.home.homepage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.homeblock.HomeBlock;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.NetizensPlaylist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListResult;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.WYUserPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.dataconvter.CommonPlaylist;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class HomeViewModel extends CommonViewModel {

    private MutableLiveData<List<CommonPlaylist>> recommendListLiveData;
    private MutableLiveData<List<CommonPlaylist>> netizensPlaylistLiveData;
    private MutableLiveData<PlayListDetailEntity> playListDetailLive;
    private MutableLiveData<HomeBlock> homeBlockLiveData;

    public HomeViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public LiveData<HomeBlock> getHomeBlockLiveData() {
        if (homeBlockLiveData == null) {
            homeBlockLiveData = new MutableLiveData<>();
            getWYHomePage(true);
        }
        return homeBlockLiveData;
    }

    public LiveData<PlayListDetailEntity> getPlayListDetail() {
        if (playListDetailLive == null) {
            playListDetailLive = new MutableLiveData<>();
        }
        return playListDetailLive;
    }

    public LiveData<List<CommonPlaylist>> getNetizensPlaylistLiveData() {
        if (netizensPlaylistLiveData == null) {
            netizensPlaylistLiveData = new MutableLiveData<>();
        }
        return netizensPlaylistLiveData;
    }

    public LiveData<List<CommonPlaylist>> getRecommendListLiveData() {
        if (recommendListLiveData == null) {
            recommendListLiveData = new MutableLiveData<>();
        }
        return recommendListLiveData;
    }

    public void getWYHomePage(boolean refresh) {
        addSubscribe(model.getWYHomeBlock(refresh, String.valueOf(System.currentTimeMillis()))
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<HomeBlock>() {
                    @Override
                    public void accept(HomeBlock homeBlock) throws Exception {
                        homeBlockLiveData.postValue(homeBlock);
                    }
                }));
    }

    //获取歌单详情(网易)
    public void getPlayListDetailEntity(final long id) {
        addSubscribe(model.getPlayListDetail(id)
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<PlayListDetailEntity>() {
                    @Override
                    public void accept(PlayListDetailEntity playListDetailEntity) throws Exception {
                        playListDetailLive.postValue(playListDetailEntity);
                    }
                }));
    }

    public void getWYRecommendList(int limit) {
        addSubscribe(
                model.getWYRecommendPlayList(limit)
                        .compose(RxSchedulers.apply())
                        .map(new Function<RecommendPlayList, List<CommonPlaylist>>() {
                            @Override
                            public List<CommonPlaylist> apply(@io.reactivex.annotations.NonNull RecommendPlayList recommendPlayList) throws Exception {
                                List<PlayListResult> results = recommendPlayList.component4();
                                List<CommonPlaylist> commonPlaylists = new ArrayList<>();
                                for (PlayListResult playListResult : results) {
                                    commonPlaylists.add(new CommonPlaylist(playListResult.getId(), playListResult.getName(), playListResult.getPicUrl()));
                                }
                                return commonPlaylists;
                            }
                        })
                        .subscribe(new Consumer<List<CommonPlaylist>>() {
                            @Override
                            public void accept(List<CommonPlaylist> commonPlaylists) throws Exception {
                                recommendListLiveData.postValue(commonPlaylists);
                            }
                        })
        );
    }

    public void getWYNetizensPlayList(String order, String cat, int limit, int offset) {
        addSubscribe(
                model.getWYNetizensPlayList(order, cat, limit, offset)
                        .compose(RxSchedulers.apply())
                        .map(new Function<NetizensPlaylist, List<CommonPlaylist>>() {
                            @Override
                            public List<CommonPlaylist> apply(@io.reactivex.annotations.NonNull NetizensPlaylist netizensPlaylist) throws Exception {
                                List<Playlist> playlists = netizensPlaylist.component4();
                                List<CommonPlaylist> commonPlaylists = new ArrayList<>();
                                for (Playlist playlist : playlists) {
                                    commonPlaylists.add(new CommonPlaylist(playlist.getId(), playlist.getName(), playlist.getCoverImgUrl()));
                                }
                                return commonPlaylists;
                            }
                        })
                        .subscribe(new Consumer<List<CommonPlaylist>>() {
                            @Override
                            public void accept(List<CommonPlaylist> commonPlaylists) throws Exception {
                                netizensPlaylistLiveData.postValue(commonPlaylists);
                            }
                        })
        );
    }

}