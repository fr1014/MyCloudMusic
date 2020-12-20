package com.fr1014.mycoludmusic.listener;

import android.graphics.Bitmap;

/**
 * Create by fanrui on 2020/12/19
 * Describe:
 */
public interface LoadResultListener {

    void coverLoading();

    void coverLoadSuccess(Bitmap coverLocal);

    void coverLoadFail();
}
