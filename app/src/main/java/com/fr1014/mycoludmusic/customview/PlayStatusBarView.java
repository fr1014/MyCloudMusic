package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.viewpager2.widget.ViewPager2;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.CustomviewPlaystatusbarBinding;
import com.fr1014.mycoludmusic.musicmanager.listener.MusicListChangeListener;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayEventAdapterListener;
import com.fr1014.mycoludmusic.ui.home.playlistdialog.PlayDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.ui.playstatusbar.PlayStatusBarPagerAdapter;
import com.fr1014.mycoludmusic.utils.CollectionUtils;
import com.fr1014.mycoludmusic.utils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 底部播放状态栏
 * <p>
 * 仅可在继承了BasePlayActivity的Activity中使用
 */
public class PlayStatusBarView extends LinearLayout implements View.OnClickListener, MusicListChangeListener, LifecycleObserver {
    private CustomviewPlaystatusbarBinding mViewBinding;
    private FragmentManager fragmentManager;
    private PlayDialogFragment listDialogFragment;
    private OnPlayerEventListener onPlayerEventListener;
    private PlayStatusBarPagerAdapter pagerAdapter;
    private boolean isFirstInitPager = true;
    private boolean isPagerSlideSelected = false;

    public PlayStatusBarView(Context context, FragmentManager fragmentManager) {
        super(context);
        this.fragmentManager = fragmentManager;
        initView();
    }

    public PlayStatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PlayStatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewBinding = CustomviewPlaystatusbarBinding.inflate(LayoutInflater.from(getContext()), this, false);
        addView(mViewBinding.getRoot());
        setVisibility(INVISIBLE);
        initPlayListViewPager();
        initListener();
        listDialogFragment = new PlayDialogFragment();
        setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
    }

    private void initPlayListViewPager() {
        pagerAdapter = new PlayStatusBarPagerAdapter((FragmentActivity) getContext());
        //设置滚动方向
        mViewBinding.pagerList.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.pagerList.setAdapter(pagerAdapter);
        mViewBinding.pagerList.setOffscreenPageLimit(1);
        mViewBinding.pagerList.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                 if (!isFirstInitPager && isPagerSlideSelected) {
                    AudioPlayer.get().pausePlayer();
                    List<Music> pagerMusicList = AudioPlayer.get().getPagerMusicList();
                    AudioPlayer.get().addAndPlay(pagerMusicList.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                /*
                    0：什么都没做
                    1：开始滑动
                    2：滑动结束
                 */
                if (state == 0) {
                    isPagerSlideSelected = false;
                }else {
                    isFirstInitPager = false;
                    isPagerSlideSelected = true;
                }
            }
        });
    }

    private void switchPagerFragment() {
        isPagerSlideSelected = false;
        Music currentMusic = AudioPlayer.get().getCurrentMusic();
        if (currentMusic != null){
            int position = AudioPlayer.get().indexOf(currentMusic);
            mViewBinding.pagerList.setCurrentItem(position, false);
        }
    }

    private void initListener() {
        onPlayerEventListener = new OnPlayEventAdapterListener() {
            @Override
            public void onChange(@NotNull Music music) {
                setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
                switchPagerFragment();
            }

            @Override
            public void onPlayerStart() {
                setPlayPause(true);
            }

            @Override
            public void onPlayerPause() {
                setPlayPause(false);
            }
        };
        mViewBinding.clBottomBar.setOnClickListener(this);
        mViewBinding.ivStateStop.setOnClickListener(this);
        mViewBinding.ivStatePlay.setOnClickListener(this);
        mViewBinding.ivMusicMenu.setOnClickListener(this);
    }

    public OnPlayerEventListener getOnPlayEventListener() {
        return onPlayerEventListener;
    }

    private void setPlayStatus(int visibility) {
        mViewBinding.ivStatePlay.setVisibility(visibility);
    }

    private void setStopStatus(int visibility) {
        mViewBinding.ivStateStop.setVisibility(visibility);
    }

    private void setPlayPause(boolean isPlaying) {
        if (isPlaying) {
            setPlayStatus(View.GONE);
            setStopStatus(View.VISIBLE);
        } else {
            setPlayStatus(View.VISIBLE);
            setStopStatus(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_state_play:
            case R.id.iv_state_stop:
                AudioPlayer.get().playPause();
                break;
            case R.id.iv_music_menu:
                if (AudioPlayer.get().getCurrentMusic() != null) {
                    if (!listDialogFragment.isAdded()) {
                        //弹出当前播放列表
                        listDialogFragment.show(fragmentManager, "playlist_dialog");
                    }
                } else {
                    CommonUtils.toastShort("当前播放列表为空！！！");
                }
                break;
        }
    }

    @Override
    public void isChanged(List<Music> musicList) {
        if (getVisibility() != View.VISIBLE && !CollectionUtils.isEmptyList(musicList)){
            setVisibility(VISIBLE);
        }
        if (pagerAdapter != null) {
            pagerAdapter.setMusicList(musicList);
            Music currentMusic = AudioPlayer.get().getCurrentMusic();
            if (currentMusic != null) {
                switchPagerFragment();
            }
        }
    }

    public void addMusicListChangeListener() {
        AudioPlayer.get().addMusicListChangeListener(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        AudioPlayer.get().removeMusicListChangeListener(this);
    }
}
