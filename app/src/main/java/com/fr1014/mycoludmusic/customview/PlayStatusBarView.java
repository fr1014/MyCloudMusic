package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.CustomviewPlaystatusbarBinding;
import com.fr1014.mycoludmusic.home.dialogfragment.currentmusic.CurrentMusicDialogFragment;
import com.fr1014.mycoludmusic.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.MusicService;

/**
 * 底部播放状态栏
 */
public class PlayStatusBarView extends LinearLayout implements View.OnClickListener {
    private CustomviewPlaystatusbarBinding mViewBinding;
    private MusicService.MusicControl musicControl;
    private FragmentManager fragmentManager;

    public PlayStatusBarView(Context context,FragmentManager fragmentManager) {
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
        initClickListener();
    }

    private void initClickListener() {
        mViewBinding.clBottomBar.setOnClickListener(this);
        mViewBinding.ivStateStop.setOnClickListener(this);
        mViewBinding.ivStatePlay.setOnClickListener(this);
        mViewBinding.ivMusicMenu.setOnClickListener(this);
    }

    public void setMusicControl(MusicService.MusicControl musicControl){
        this.musicControl = musicControl;
    }

    public void setPlayStatus(int visibility) {
        mViewBinding.ivStatePlay.setVisibility(visibility);
    }

    public void setStopStatus(int visibility) {
        mViewBinding.ivStateStop.setVisibility(visibility);
    }

    public void setMusic(Music music){
        setTitle(music.getTitle());
        setImageUrl(music.getImgUrl());
    }

    public void setTitle(String title) {
        mViewBinding.tvName.setText(title);
    }

    public void setImageUrl(String imgUrl) {
        Glide.with(MyApplication.getInstance())
                .load(imgUrl)
                .placeholder(R.drawable.film)
                .into(mViewBinding.ivCoverImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_state_play:
            case R.id.iv_state_stop:
                musicControl.playOrPause();
                break;
            case R.id.iv_music_menu:
                //弹出当前播放列表
                new PlayListDialogFragment().show(fragmentManager, "playlist_dialog");
                break;
            case R.id.cl_bottom_bar:
                //当前播放的音乐的详情页
                new CurrentMusicDialogFragment().show(fragmentManager, "current_music_dialog");
                break;
        }
    }
}
