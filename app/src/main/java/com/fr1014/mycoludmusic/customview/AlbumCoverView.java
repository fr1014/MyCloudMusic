package com.fr1014.mycoludmusic.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.fr1014.mycoludmusic.databinding.AlubmCoverviewBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;

/**
 * Create by fanrui on 2020/12/11
 * Describe:
 */
public class AlbumCoverView extends RelativeLayout implements LifecycleObserver {
    private ObjectAnimator rotationAnimator;

    private AlubmCoverviewBinding mViewBinding;

    public AlbumCoverView(Context context) {
        super(context);
        initView();
    }


    public AlbumCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AlbumCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mViewBinding = AlubmCoverviewBinding.inflate(LayoutInflater.from(getContext()), this, false);
        addView(mViewBinding.getRoot());
        initAnimator();
    }

    public void initAnimator() {
        rotationAnimator = ObjectAnimator.ofFloat(mViewBinding.civSongImg, "rotation", 0f, 360f);//旋转的角度可有多个
        rotationAnimator.setDuration(25000);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);//匀速
        //让旋转动画一直转，不停顿的重点
        rotationAnimator.setInterpolator(new LinearInterpolator());
    }

    public void startAnimator() {
        rotationAnimator.start();
    }

    public void pauseAnimator() {
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            rotationAnimator.pause();
        }
    }

    public void resumeAnimator() {
        if (rotationAnimator != null && rotationAnimator.isPaused()) {
            rotationAnimator.resume();
        }
    }

    public void resumeOrStartAnimator() {
        if (rotationAnimator.isPaused()) {
            rotationAnimator.resume();//继续（在暂停的位置继续动画）
        } else {
            startAnimator();
        }
    }

    public void endAnimator() {
        if (rotationAnimator.isRunning() || rotationAnimator.isPaused()) {
            rotationAnimator.end();//结束（回到原始位置）
        }
    }

    public void songImgSetBitmap(Bitmap resource) {
        mViewBinding.civSongImg.setImageBitmap(resource);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume(){
        if (AudioPlayer.get().isPlaying()) {
            resumeAnimator();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(){
        pauseAnimator();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(){
        if (mViewBinding != null){
            endAnimator();
        }
    }
}
