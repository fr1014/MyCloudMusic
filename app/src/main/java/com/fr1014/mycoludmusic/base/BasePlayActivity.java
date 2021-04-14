package com.fr1014.mycoludmusic.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.fr1014.mycoludmusic.MainActivity;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.SourceHolder;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.ui.SwitchDialogFragment;
import com.fr1014.mycoludmusic.ui.home.comment.CommentFragment;
import com.fr1014.mycoludmusic.ui.home.songsale.detail.AlbumDetailFragment;
import com.fr1014.mycoludmusic.ui.playing.CurrentPlayMusicFragment;
import com.fr1014.mycoludmusic.musicmanager.PlayService;
import com.fr1014.mycoludmusic.utils.CommonUtils;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseActivity;
import com.fr1014.mymvvm.base.BaseViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class BasePlayActivity<VB extends ViewBinding, VM extends BaseViewModel> extends BaseActivity<VB, VM> implements SwitchDialogFragment.MusicSourceCallback {

    protected PlayService playService;
    private ServiceConnection serviceConnection;
    private boolean isPlayFragmentShow;
    private CurrentPlayMusicFragment mPlayFragment;
    private Toast toast;
    protected String source = "";

    @Override
    public void musicSource(int position) {
        if (toast != null) {
            toast.cancel();
        }
        source = SwitchDialogFragment.array[position];
        toast = CommonUtils.toastShort("音乐源已切换为: " + source);
        switch (position) {
            case 0:  //酷我
                SourceHolder.get().setSource("酷我");
                break;
            case 1:  //网易
                SourceHolder.get().setSource("网易");
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置statusBar字体为黑色
        StatusBarUtils.setImmersiveStatusBar(getWindow(), true);
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        serviceConnection = new PlayServiceConnect(this);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
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

//    public void showPlayingFragment() {
//        if (isPlayFragmentShow) {
//            return;
//        }
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
//        if (mPlayFragment == null) {
//            mPlayFragment = new CurrentPlayMusicFragment();
//            ft.replace(android.R.id.content, mPlayFragment);
//        } else {
//            ft.show(mPlayFragment);
//        }
//        ft.commitAllowingStateLoss();
//        isPlayFragmentShow = true;
//    }
//
//    public void hidePlayingFragment() {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
//        ft.hide(mPlayFragment);
//        ft.commitAllowingStateLoss();
//        isPlayFragmentShow = false;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mPlayFragment != null && isPlayFragmentShow) {
//            hidePlayingFragment();
//            return;
//        }
//        super.onBackPressed();
//    }

    public void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new CurrentPlayMusicFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    public void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.remove(mPlayFragment);
        mPlayFragment = null;
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            if (this instanceof MainActivity){
                ((MainActivity)this).back();
            }
            hidePlayingFragment();
            return;
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof AlbumDetailFragment) {
                ((AlbumDetailFragment) fragment).onBackPressed();
                return;
            } else if (fragment instanceof CommentFragment && fragment.isVisible()) {
                ((CommentFragment) fragment).onBackPressed();
                return;
            }
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
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
        super.onDestroy();
        Glide.with(MyApplication.getInstance()).pauseAllRequests();
    }
}
