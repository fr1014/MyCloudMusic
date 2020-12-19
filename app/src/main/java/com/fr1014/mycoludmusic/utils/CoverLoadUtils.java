package com.fr1014.mycoludmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.musicmanager.Music;

/**
 * Create by fanrui on 2020/12/19
 * Describe:
 */
public class CoverLoadUtils {

    public static boolean isCoverLoaded(Music music) {
        return FileUtils.getCoverLocal(music) != null;
    }

    public static void loadRemoteCover(Context context, Music music,final LoadResultListener loadResultListener){
        if (isCoverLoaded(music)){
            loadResultListener.success();
            return;
        }
        Glide.with(context)
                .asBitmap()
                .load(music.getImgUrl())
                .override(300,300)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        FileUtils.saveCoverToLocal(resource,music);
                        loadResultListener.success();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }


}
