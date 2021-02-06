package com.fr1014.mycoludmusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.data.DataRepository;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Create by fanrui on 2020/12/19
 * Describe: 歌曲封面下载并保存到本地
 */
public class CoverLoadUtils {
    private final List<LoadResultListener> loadResultListenerList = new ArrayList<>();
    DataRepository dataRepository = MyApplication.provideRepository();

    private CoverLoadUtils() {

    }

    public void registerLoadListener(LoadResultListener loadResultListener) {
        loadResultListenerList.add(loadResultListener);
    }

    public void removeLoadListener(LoadResultListener loadResultListener) {
        loadResultListenerList.remove(loadResultListener);
    }

    public static CoverLoadUtils get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final CoverLoadUtils instance = new CoverLoadUtils();
    }

    public void loadRemoteCover(Music music) {
        Bitmap coverLocal = FileUtils.getCoverLocal(music);
        if (coverLocal != null) {
            for (LoadResultListener loadResultListener : loadResultListenerList) {
                loadResultListener.coverLoadSuccess(coverLocal);
            }
            return;
        }

//        Glide.with(context)
//                .asBitmap()
//                .load(music.getImgUrl())
//                .override(300, 300)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .priority(Priority.HIGH)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        FileUtils.saveCoverToLocal(resource, music, loadResultListenerList);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
////                        for (LoadResultListener loadResultListener : loadResultListenerList) {
////                            loadResultListener.coverLoadFail();
////                        }
//                    }
//                });
        AudioPlayer.get().addDisposable(dataRepository.getSongCover(music.getImgUrl() + "?param=700y700")
                .compose(RxSchedulers.apply())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        for (LoadResultListener loadResultListener : loadResultListenerList) {
                            loadResultListener.coverLoading();
                        }
                    }
                })
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        return bitmap;
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        FileUtils.saveCoverToLocal(bitmap, music, loadResultListenerList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        for (LoadResultListener loadResultListener : loadResultListenerList) {
                            loadResultListener.coverLoadFail();
                        }
                    }
                }));
    }
}
