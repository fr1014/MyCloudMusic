package com.fr1014.mycoludmusic.listener;

import android.graphics.Bitmap;

import com.fr1014.mycoludmusic.musicmanager.Music;

/**
 * Create by fanrui on 2020/12/19
 * Describe:
 */
public interface LoadResultListener {

    void coverLoading();

    void coverLoadSuccess(Music music,Bitmap coverLocal);

    void coverLoadFail();
}
