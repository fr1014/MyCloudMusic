package com.fr1014.mycoludmusic.ui.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.Playlist;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.WYUserPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.TrackIds;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.Profile;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.user.WYLikeIdList;
import com.fr1014.mycoludmusic.data.source.local.room.MusicLike;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mymvvm.base.BaseViewModel;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class CommonViewModel extends BaseViewModel<DataRepository> {

    protected BusLiveData<Long[]> playListDetailIds;
    protected MutableLiveData<List<Playlist>> playlistWYLive;
    private MutableLiveData<PlayListDetailEntity> playListDetailLive;

    public CommonViewModel(@NonNull Application application) {
        super(application);
    }

    public CommonViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public LiveData<PlayListDetailEntity> getPlayListDetailInfo() {
        if (playListDetailLive == null) {
            playListDetailLive = new MutableLiveData<>();
        }
        return playListDetailLive;
    }

    public LiveData<List<Playlist>> getPlaylistWYLive() {
        if (playlistWYLive == null) {
            playlistWYLive = new MutableLiveData<>();
        }
        return playlistWYLive;
    }

    public LiveData<Long[]> getPlayListDetail(long id) {
        if (playListDetailIds == null) {
            playListDetailIds = new BusLiveData<>();
        }
        getPlayListDetailEntity(id);
        return playListDetailIds;
    }

    private void getPlayListDetailEntity(final long id) {
        addSubscribe(model.getPlayListDetail(id)
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<PlayListDetailEntity>() {
                    @Override
                    public void accept(PlayListDetailEntity playListDetailEntity) throws Exception {
                        List<TrackIds> tracks = playListDetailEntity.playlist.getTrackIds();
                        Long[] ids = new Long[tracks.size()];
                        for (int index = 0; index < tracks.size(); index++) {
                            ids[index] = tracks.get(index).getId();
                        }
                        playListDetailLive.postValue(playListDetailEntity);
                        playListDetailIds.postValue(ids);
                    }
                }));
    }

    public void getWYUserPlayList() {
        Profile profile = Preferences.getUserProfile();
        if (profile == null) return;
        final long userId = profile.getUserId();
        if (userId != 0L)
            addSubscribe(
                    model.getWYUserPlayList(userId, String.valueOf(System.currentTimeMillis()))
                            .compose(RxSchedulers.apply())
                            .subscribe(new Consumer<WYUserPlayList>() {
                                @Override
                                public void accept(WYUserPlayList wyUserPlayList) throws Exception {
                                    playlistWYLive.postValue(wyUserPlayList.getPlaylist());
                                }
                            })
            );
    }

    public void getLikeIdList(Long uid) {
        addSubscribe(model.getWYLikeIdList(uid, String.valueOf(System.currentTimeMillis()))
                .compose(RxSchedulers.applyIO())
                .subscribe(new Consumer<WYLikeIdList>() {
                    @Override
                    public void accept(WYLikeIdList wyLikeIdList) throws Exception {
                        model.deleteAllLikeIds();
                        List<Long> ids = wyLikeIdList.getIds();
                        List<MusicLike> musicLikes = new ArrayList<>();
                        for (Long id : ids) {
                            musicLikes.add(new MusicLike(id));
                        }
                        model.insertAllLikeIds(musicLikes);
                    }
                })
        );
    }
}
