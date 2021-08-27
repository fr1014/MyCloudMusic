package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.CustomPlaycontrolbarBinding;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.musicmanager.player.Music;
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay;

/**
 * Create by fanrui on 2020/12/26
 * Describe:
 */
public class PlayControlBarView extends LinearLayout implements View.OnClickListener {
    private CustomPlaycontrolbarBinding mViewBinding;
    private OnPlayControlBarClick playControlBarClick;

    public PlayControlBarView(Context context) {
        super(context);
        initView();
    }

    public PlayControlBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PlayControlBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewBinding = CustomPlaycontrolbarBinding.inflate(LayoutInflater.from(getContext()), this, false);
        addView(mViewBinding.getRoot());

        initListener();
    }

    private void initListener() {
        mViewBinding.ivState.setOnClickListener(this);
        mViewBinding.ivMusicMenu.setOnClickListener(this);
        mViewBinding.ivMode.setOnClickListener(this);
        mViewBinding.ivPre.setOnClickListener(this);
        mViewBinding.ivNext.setOnClickListener(this);
    }

    public void setPlayControlBarClick(OnPlayControlBarClick playControlBarClick) {
        this.playControlBarClick = playControlBarClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mode:
                switchPlayMode();
                break;
            case R.id.iv_pre:
                playControlBarClick.pre(playPreMusic());
                break;
            case R.id.iv_next:
                playControlBarClick.next(playNextMusic());
                break;
            case R.id.iv_state:
                MyAudioPlay.get().playOrPause();
                break;
            case R.id.iv_music_menu:
                playControlBarClick.openMenu();
                break;
        }
    }

    private void switchPlayMode() {
        setImageMode(MyAudioPlay.get().switchPlayMode().value());
    }

    public void initPlayMode() {
        int mode = MyAudioPlay.get().getPlayMode().value();
        setImageMode(mode);
    }

    private void setImageMode(int mode) {
        switch (mode) {
            case 0:
                mViewBinding.ivMode.setImageResource(R.drawable.selector_loop);
                break;
            case 1:
                mViewBinding.ivMode.setImageResource(R.drawable.selector_random);
                break;
            case 2:
                mViewBinding.ivMode.setImageResource(R.drawable.selector_cycle);
                break;
        }
    }

    public Music playPreMusic() {
        return MyAudioPlay.get().playPre();
    }

    public Music playNextMusic() {
        return MyAudioPlay.get().playNext();
    }

    public void setStateImage(@DrawableRes int resId) {
        mViewBinding.ivState.setImageResource(resId);
    }

    public interface OnPlayControlBarClick {
        void pre(Music pre);

        void next(Music next);

        void openMenu();
    }
}
