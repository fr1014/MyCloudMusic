package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.util.AttributeSet;
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
import com.fr1014.mycoludmusic.musicmanager.player.Music;
import com.fr1014.mycoludmusic.musicmanager.player.MusicKt;
import com.fr1014.mycoludmusic.musicmanager.player.MusicListManageUtils;
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay;
import com.fr1014.mycoludmusic.musicmanager.player.PlayerEvent;
import com.fr1014.mycoludmusic.ui.home.playlistdialog.PlayDialogFragment;
import com.fr1014.mycoludmusic.ui.playstatusbar.PlayStatusBarPagerAdapter;
import com.fr1014.mycoludmusic.utils.CollectionUtils;
import com.fr1014.mycoludmusic.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 底部播放状态栏
 * <p>
 * 仅可在继承了BasePlayActivity的Activity中使用
 */
public class PlayStatusBarView extends LinearLayout implements View.OnClickListener, LifecycleObserver {
    private CustomviewPlaystatusbarBinding mViewBinding;
    private FragmentManager fragmentManager;
    private PlayDialogFragment listDialogFragment;
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
        setVisibility(CollectionUtils.isEmptyList(getCurrentMusicList()) ? GONE : VISIBLE);
        initPlayListViewPager();
        initListener();
        listDialogFragment = new PlayDialogFragment();
        setPlayPause(MyAudioPlay.get().isPlaying() || MyAudioPlay.get().isPreparing());
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
                    MyAudioPlay.get().pausePlayer();
                    List<Music> pagerMusicList = getCurrentMusicList();
                    MyAudioPlay.get().addPlayMusic(pagerMusicList.get(position));
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
                } else {
                    isFirstInitPager = false;
                    isPagerSlideSelected = true;
                }
            }
        });
    }

    private List<Music> getCurrentMusicList() {
        return MyAudioPlay.get().getCurrentMusicList();
    }

    private void switchPagerFragment(Music music) {
        isPagerSlideSelected = false;
        if (music != null) {
            int position = MusicKt.indexOf(music, getCurrentMusicList());
            mViewBinding.pagerList.setCurrentItem(position, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(PlayerEvent event) {
        switch (event.getType()) {
            case OnPlayListChange:
                List<Music> musicList = event.getMusicList();
                if (pagerAdapter != null && !CollectionUtils.isEmptyList(musicList)) {
                    setVisibility(VISIBLE);
                    pagerAdapter.setMusicList(musicList);
                    switchPagerFragment(MyAudioPlay.get().getCurrentMusic());
                } else {
                    setVisibility(GONE);
                }
                break;
            case OnChange:
                //setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
                switchPagerFragment(event.getMusic());
                break;
            case OnPlayerStart:
                setPlayPause(true);
                break;
            case OnPlayerPause:
                setPlayPause(false);
                break;
        }
    }

    private void initListener() {
        mViewBinding.clBottomBar.setOnClickListener(this);
        mViewBinding.ivStateStop.setOnClickListener(this);
        mViewBinding.ivStatePlay.setOnClickListener(this);
        mViewBinding.ivMusicMenu.setOnClickListener(this);
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
                MyAudioPlay.get().playOrPause();
                break;
            case R.id.iv_music_menu:
                if (MyAudioPlay.get().getCurrentMusic() != null) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        EventBus.getDefault().register(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        EventBus.getDefault().unregister(this);
    }
}
