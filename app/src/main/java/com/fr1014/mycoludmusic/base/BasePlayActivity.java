package com.fr1014.mycoludmusic.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.ui.home.CurrentPlayMusicFragment;
import com.fr1014.mycoludmusic.musicmanager.PlayService;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseActivity;
import com.fr1014.mymvvm.base.BaseViewModel;

import java.lang.ref.WeakReference;

public abstract class BasePlayActivity<VB extends ViewBinding,VM extends BaseViewModel> extends BaseActivity<VB,VM> {

    protected PlayService playService;
    private ServiceConnection serviceConnection;
    private boolean isPlayFragmentShow;
    private CurrentPlayMusicFragment mPlayFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置statusBar字体为黑色
        StatusBarUtils.setImmersiveStatusBar(getWindow(),true);
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        serviceConnection = new PlayServiceConnect(this);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    private static class PlayServiceConnect implements ServiceConnection {
        private final WeakReference<Context> reference;

        public PlayServiceConnect(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BasePlayActivity activity = (BasePlayActivity) reference.get();
            activity.playService = ((PlayService.PlayBinder) service).getService();
            activity.onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    protected void onServiceBound() {
    }

    public void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new CurrentPlayMusicFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    public void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        super.onBackPressed();
    }

    //设置系统状态栏为透明
    private void setSystemBarTransparent() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        if (serviceConnection != null){
            unbindService(serviceConnection);
        }
        super.onDestroy();
        Glide.with(MyApplication.getInstance()).pauseAllRequests();
    }
}
