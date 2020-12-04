package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.CustomviewPlaystatusbarBinding;
import com.fr1014.mycoludmusic.home.dialogfragment.currentmusic.CurrentMusicDialogFragment;
import com.fr1014.mycoludmusic.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.listener.MusicInfoListener;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.utils.CommonUtil;

/**
 * 底部播放状态栏
 */
public class PlayStatusBarView extends LinearLayout implements View.OnClickListener, OnPlayerEventListener {
    private CustomviewPlaystatusbarBinding mViewBinding;
    private FragmentManager fragmentManager;
    private MusicInfoListener musicInfoListener;
    private PlayListDialogFragment listDialogFragment;
    private CurrentMusicDialogFragment musicDialogFragment;
    private Music oldMusic;

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
        listDialogFragment = new PlayListDialogFragment();
        musicDialogFragment = new CurrentMusicDialogFragment();
        setMusic(AudioPlayer.get().getPlayMusic());
        setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
        initClickListener();
    }

    private void initClickListener() {
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

    public void setMusic(Music music) {
        if (music != oldMusic) {
            setTitle(music.getTitle());
            setImageUrl(music.getImgUrl());
        }
        oldMusic = music;
    }

    private void setTitle(String title) {
        mViewBinding.tvName.setText(title);
    }

    private void setImageUrl(String imgUrl) {
        mViewBinding.ivCoverImg.setImageUrl(imgUrl);
    }

    public void setMusicInfoListener(MusicInfoListener musicInfoListener) {
        this.musicInfoListener = musicInfoListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_state_play:
            case R.id.iv_state_stop:
                AudioPlayer.get().playPause();
                break;
            case R.id.iv_music_menu:
                if (AudioPlayer.get().getPlayMusic() != null) {
                    if (!listDialogFragment.isAdded()) {
                        //弹出当前播放列表
                        listDialogFragment.show(fragmentManager, "playlist_dialog");
                    }
                } else {
                    CommonUtil.toastShort("当前播放列表为空！！！");
                }
                break;
            case R.id.cl_bottom_bar:
                if (AudioPlayer.get().getPlayMusic() != null) {
                    if (!musicDialogFragment.isAdded()) {
                        //当前播放的音乐的详情页
                        musicDialogFragment.show(fragmentManager, "current_music_dialog");
                    }
                } else {
                    CommonUtil.toastShort("当前尚未有歌曲在播放！！！");
                }
                break;
        }
    }

    @Override
    public void onChange(Music music) {
        setMusic(music);
        setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
        if (musicInfoListener != null && TextUtils.isEmpty(music.getSongUrl())) {
            musicInfoListener.songUrlIsEmpty(music);
        }
    }

    @Override
    public void onPlayerStart() {
        setPlayPause(true);
    }

    @Override
    public void onPlayerPause() {
        setPlayPause(false);
    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }
}
