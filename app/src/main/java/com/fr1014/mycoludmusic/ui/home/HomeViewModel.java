package com.fr1014.mycoludmusic.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.RecommendPlayList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYLikeList;
import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.WYUserPlayList;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.ui.vm.CommonViewModel;
import com.fr1014.mymvvm.base.BaseViewModel;

import io.reactivex.functions.Consumer;

public class HomeViewModel extends CommonViewModel {

    private MutableLiveData<RecommendPlayList> recommendListLiveData;

    public HomeViewModel(@NonNull Application application, DataRepository model) {
        super(application, model);
    }

    public LiveData<RecommendPlayList> getRecommendListLiveData() {
        if (recommendListLiveData == null) {
            recommendListLiveData = new MutableLiveData<>();
        }
        return recommendListLiveData;
    }

    public void getWYRecommendList(int limit) {
        addSubscribe(
                model.getWYRecommendPlayList(limit)
                        .compose(RxSchedulers.apply())
                        .subscribe(new Consumer<RecommendPlayList>() {
                            @Override
                            public void accept(RecommendPlayList recommendPlayList) throws Exception {
                                recommendListLiveData.setValue(recommendPlayList);
                            }
                        })
        );
    }

    public void getWYUserPlayList(){
        addSubscribe(
                model.getWYUserPlayList(Preferences.getUserProfile().getUserId())
                .compose(RxSchedulers.apply())
                .subscribe(new Consumer<WYUserPlayList>() {
                    @Override
                    public void accept(WYUserPlayList wyUserPlayList) throws Exception {

                    }
                })
        );
    }
}