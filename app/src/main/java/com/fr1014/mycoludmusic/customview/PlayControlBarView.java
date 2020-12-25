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
import com.fr1014.mycoludmusic.utils.CommonUtil;

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
        mViewBinding.llState.setOnClickListener(this);
        mViewBinding.llMenu.setOnClickListener(this);
        mViewBinding.llMode.setOnClickListener(this);
        mViewBinding.llPre.setOnClickListener(this);
        mViewBinding.llNext.setOnClickListener(this);
    }

    public void setPlayControlBarClick(OnPlayControlBarClick playControlBarClick) {
        this.playControlBarClick = playControlBarClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mode:
                switchPlayMode();
                break;
            case R.id.ll_pre:
                playPreMusic();
                playControlBarClick.pre();
                break;
            case R.id.ll_next:
                playNextMusic();
                playControlBarClick.next();
                break;
            case R.id.ll_state:
                AudioPlayer.get().playPause();
                break;
            case R.id.ll_menu:
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
                toast = CommonUtil.toastShort("循环播放");
                mode = PlayModeEnum.LOOP;
                break;
            case LOOP:
                toast = CommonUtil.toastShort("随机播放");
                mode = PlayModeEnum.SHUFFLE;
                break;
            case SHUFFLE:
                toast = CommonUtil.toastShort("单曲循环");
                mode = PlayModeEnum.SINGLE;
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
    }

    public void initPlayMode() {
        int mode = Preferences.getPlayMode();
        setImageMode(mode);
    }

    private void setImageMode(int mode) {
        switch (mode) {
            case 0:
                mViewBinding.ivMode.setImageResource(R.drawable.ic_loop_white);
                break;
            case 1:
                mViewBinding.ivMode.setImageResource(R.drawable.ic_random_white);
                break;
            case 2:
                mViewBinding.ivMode.setImageResource(R.drawable.ic_cycle_white);
                break;
        }
    }

    public void playPreMusic() {
        int playPosition = AudioPlayer.get().playPre();
        Music pre = AudioPlayer.get().getMusicList().get(playPosition);
        playOtherMusic(pre);
    }

    public void playNextMusic() {
        int playPosition = AudioPlayer.get().playNext();
        Music next = AudioPlayer.get().getMusicList().get(playPosition);
        playOtherMusic(next);
    }

    public void playOtherMusic(Music music) {
//        initViewData(music);
    }

    public void setStateImage(@DrawableRes int resId) {
        mViewBinding.ivState.setImageResource(resId);
    }

    public interface OnPlayControlBarClick {
        void pre();

        void next();

        void openMenu();
    }
}
