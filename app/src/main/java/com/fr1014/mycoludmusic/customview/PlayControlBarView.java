package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.CustomPlaycontrolbarBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.PlayModeEnum;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.utils.CommonUtils;

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
                AudioPlayer.get().playPause();
                break;
            case R.id.iv_music_menu:
                playControlBarClick.openMenu();
                break;
        }
    }

    Toast toast;
    private void switchPlayMode() {
        if (toast != null){
            toast.cancel();
        }
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SINGLE:
                toast = CommonUtils.toastShort("循环播放");
                mode = PlayModeEnum.LOOP;
                break;
            case LOOP:
                toast = CommonUtils.toastShort("随机播放");
                mode = PlayModeEnum.SHUFFLE;
                AudioPlayer.get().shuffle();
                break;
            case SHUFFLE:
                toast = CommonUtils.toastShort("单曲循环");
                mode = PlayModeEnum.SINGLE;
                break;
        }
        Preferences.savePlayMode(mode.value());
        AudioPlayer.get().notifyMusicListChange();
        initPlayMode();
    }

    public void initPlayMode() {
        int mode = Preferences.getPlayMode();
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
        int playPosition = AudioPlayer.get().playPre();
        return AudioPlayer.get().getPagerMusicList().get(playPosition);
    }

    public Music playNextMusic() {
        int playPosition = AudioPlayer.get().playNext();
        return AudioPlayer.get().getPagerMusicList().get(playPosition);
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
