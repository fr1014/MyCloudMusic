package com.fr1014.mycoludmusic.ui.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.PlayListDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.song.SongDetailEntity;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.playlist.TrackIds;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mymvvm.base.BaseViewModel;
import com.fr1014.mymvvm.base.BusLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CommonViewModel extends BaseViewModel<DataRepository> {

    protected BusLiveData<Long[]> getPlayListDetail;

    public CommonViewModel(@NonNull Application application) {
        super(application);
    }

    public CommonViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public LiveData<Long[]> getPlayListDetail(long id) {
        if (getPlayListDetail == null) {
            getPlayListDetail = new BusLiveData<>();
        }
        getPlayListDetailEntity(id);
        return getPlayListDetail;
    }

    private void getPlayListDetailEntity(final long id) {
        addSubscribe(model.getPlayListDetail(id)
                .map(new Function<PlayListDetailEntity, Long[]>() {
                    @Override
                    public Long[] apply(@io.reactivex.annotations.NonNull PlayListDetailEntity playListDetailEntity) throws Exception {
                        List<TrackIds> tracks = playListDetailEntity.playlist.getTrackIds();
                        Long[] ids = new Long[tracks.size()];
                        for (int index = 0; index < tracks.size(); index++) {
                            ids[index] = tracks.get(index).getId();
                        }
                        return ids;
                    }
                })
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<Long[]>() {
                    @Override
                    public void accept(Long[] longs) throws Exception {
                        getPlayListDetail.postValue(longs);
                    }
                }));
    }
}
