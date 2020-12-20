package com.fr1014.mycoludmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.musicmanager.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by fanrui on 2020/12/19
 * Describe: 歌曲封面下载并保存到本地
 */
public class CoverLoadUtils {
    private List<LoadResultListener> loadResultListenerList;

    private CoverLoadUtils() {

    }

    public void init() {
        loadResultListenerList = new ArrayList<>();
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
        private static CoverLoadUtils instance = new CoverLoadUtils();
    }

    public void loadRemoteCover(Context context, Music music) {
        Bitmap coverLocal = FileUtils.getCoverLocal(music);
        if (coverLocal != null) {
            for (LoadResultListener loadResultListener : loadResultListenerList) {
                loadResultListener.coverLoadSuccess(coverLocal);
            }
            return;
        }

        Glide.with(context)
                .asBitmap()
                .load(music.getImgUrl())
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .priority(Priority.HIGH)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        FileUtils.saveCoverToLocal(resource, music, loadResultListenerList);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        for (LoadResultListener loadResultListener : loadResultListenerList) {
                            loadResultListener.coverLoadFail();
                        }
                    }
                });
    }
}
