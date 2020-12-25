package com.fr1014.mycoludmusic.ui.home.dialogfragment.currentmusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.databinding.FragmentCurrentMusicBinding;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.ui.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.ui.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.musicmanager.PlayModeEnum;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.musicmanager.lrcview.LrcView;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.CoverLoadUtils;
import com.fr1014.mycoludmusic.utils.FileUtils;
import com.fr1014.mycoludmusic.utils.ScreenUtil;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseFragment;

import java.io.File;

public class CurrentPlayMusicFragment extends BaseFragment<FragmentCurrentMusicBinding, TopListViewModel> implements View.OnClickListener, OnPlayerEventListener, LrcView.OnPlayClickListener, LoadResultListener {
    private Bitmap oldResource = null;
    private MediaPlayer player;

    public CurrentPlayMusicFragment() {
        // Required empty public constructor
    }

    @Override
    protected FragmentCurrentMusicBinding getViewBinding(ViewGroup container) {
        return FragmentCurrentMusicBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected TopListViewModel initViewModel() {
        return new ViewModelProvider(getActivity(), AppViewModelFactory.getInstance(MyApplication.getInstance())).get(TopListViewModel.class);
    }

    @Override
    protected void initView() {
        StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), false);
        initSystemBar();
        initListener();
        initCoverLrc();
        AudioPlayer.get().addOnPlayEventListener(this);
        CoverLoadUtils.get().registerLoadListener(this);
        player = AudioPlayer.get().getMediaPlayer();
        Music playMusic = AudioPlayer.get().getPlayMusic();
        if (playMusic == null) return;
        initViewData(playMusic);
        if (AudioPlayer.get().isPlaying()) {
            mViewBinding.albumCoverView.startAnimator();
            mViewBinding.ivState.setImageResource(R.drawable.ic_stop_white);
        } else {
            mViewBinding.ivState.setImageResource(R.drawable.ic_play_white);
        }
        initPlayMode();
    }

    private void initListener() {
        mViewBinding.icBack.setOnClickListener(this);
        mViewBinding.ivState.setOnClickListener(this);
        mViewBinding.ivMusicMenu.setOnClickListener(this);
        mViewBinding.ivMode.setOnClickListener(this);
        mViewBinding.ivPre.setOnClickListener(this);
        mViewBinding.ivNext.setOnClickListener(this);
        mViewBinding.albumCoverView.setOnClickListener(this);
        mViewBinding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //拖动进度条
                mViewBinding.tvNowTime.setText(CommonUtil.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AudioPlayer.get().seekTo(seekBar.getProgress());
            }
        });
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        int top = ScreenUtil.getStatusHeight(MyApplication.getInstance());
        mViewBinding.llContent.setPadding(0, top, 0, 0);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getSongLrcPath().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String lrcPath) {
                mViewBinding.lrcView.setLabel("暂无歌词");
                if (!lrcPath.equals("")) {
                    mViewBinding.lrcView.loadLrc(new File(lrcPath));
                }else {
                    mViewBinding.lrcView.loadLrc("暂无歌词");
                }
            }
        });
    }

    private void initPlayMode() {
        int mode = Preferences.getPlayMode();
        setImageMode(mode);
    }

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SINGLE:
                CommonUtil.toastShort("循环播放");
                mode = PlayModeEnum.LOOP;
                break;
            case LOOP:
                CommonUtil.toastShort("随机播放");
                mode = PlayModeEnum.SHUFFLE;
                break;
            case SHUFFLE:
                CommonUtil.toastShort("单曲循环");
                mode = PlayModeEnum.SINGLE;
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mode:
                switchPlayMode();
                break;
            case R.id.ic_back:
                mViewBinding.albumCoverView.endAnimator();
                getActivity().onBackPressed();
                break;
            case R.id.iv_pre:
                playPreMusic();
                mViewBinding.albumCoverView.endAnimator();
                break;
            case R.id.iv_next:
                playNextMusic();
                mViewBinding.albumCoverView.endAnimator();
                break;
            case R.id.iv_state:
                AudioPlayer.get().playPause();
                break;
            case R.id.iv_music_menu:
                new PlayListDialogFragment().show(getParentFragmentManager(), "playlist_dialog");
                break;
            case R.id.album_cover_view:
                if (mViewBinding.albumCoverView.getVisibility() == View.VISIBLE) {
                    mViewBinding.albumCoverView.setVisibility(View.GONE);
                    mViewBinding.llLrc.setVisibility(View.VISIBLE);
                    getSongLrc(AudioPlayer.get().getPlayMusic());
                }
                break;
        }
    }

    private void initViewData(Music music) {
        if (TextUtils.isEmpty(music.getImgUrl())) {
            mViewBinding.biBackground.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_play));
            return;
        }

        setBitmap(FileUtils.getCoverLocal(music));

        mViewBinding.tvTitle.setText(music.getTitle());
        mViewBinding.tvArtist.setText(music.getArtist());
        initSeekBarData(music);
    }

    private void initSeekBarData(Music music) {
        long duration = music.getDuration();
        if (duration == 0) {
            duration = player.getDuration();
        }
        mViewBinding.sbProgress.setMax((int) duration);
        mViewBinding.tvDuration.setText(CommonUtil.formatTime(duration));
    }

    //音乐旋转图、歌词
    private void initCoverLrc() {
        mViewBinding.lrcView.setDraggable(true, this);
        mViewBinding.lrcView.setOnTapListener(new LrcView.OnTapListener() {
            @Override
            public void onTap(LrcView view, float x, float y) {
                mViewBinding.albumCoverView.setVisibility(View.VISIBLE);
                mViewBinding.llLrc.setVisibility(View.GONE);
            }
        });
    }

    private void setBitmap(Bitmap resource) {
        if (resource != null) {
            mViewBinding.albumCoverView.songImgSetBitmap(resource);
            mViewBinding.biBackground.setBitmap(resource);
            oldResource = resource;
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
        initViewData(music);
    }

    @Override
    public void onChange(Music music) {
        initViewData(music);
        if (mViewBinding.llLrc.getVisibility() == View.VISIBLE){
            getSongLrc(music); //切换歌时，请求歌词
        }
    }

    @Override
    public void onPlayerStart() {
        mViewBinding.ivState.setImageResource(R.drawable.ic_stop_white);
        Music music = AudioPlayer.get().getPlayMusic();
        mViewBinding.albumCoverView.resumeOrStartAnimator();
        initSeekBarData(music);
    }

    @Override
    public void onPlayerPause() {
        mViewBinding.albumCoverView.pauseAnimator();
        mViewBinding.ivState.setImageResource(R.drawable.ic_play_white);
    }

    @Override
    public void onPublish(int progress) {
        mViewBinding.tvNowTime.setText(CommonUtil.formatTime(player.getCurrentPosition()));
        mViewBinding.sbProgress.setProgress((int) progress);

        if (isVisible() && (mViewBinding.llLrc.getVisibility() == View.VISIBLE) && mViewBinding.lrcView.hasLrc()) {
            mViewBinding.lrcView.updateTime(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    private void getSongLrc(Music music) {
        mViewBinding.lrcView.setLabel("正在搜索歌词");
        mViewModel.getSongLrc(music);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), true);
            mViewBinding.albumCoverView.endAnimator();
        } else {
            initViewData(AudioPlayer.get().getPlayMusic());
            if (AudioPlayer.get().isPlaying()) {
                StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), false);
                mViewBinding.albumCoverView.startAnimator();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AudioPlayer.get().isPlaying()) {
            mViewBinding.albumCoverView.resumeAnimator();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewBinding.albumCoverView.pauseAnimator();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewBinding.albumCoverView.endAnimator();
        AudioPlayer.get().removeOnPlayEventListener(this);
        CoverLoadUtils.get().removeLoadListener(this);
    }

    @Override
    public boolean onPlayClick(LrcView view, long time) {
        if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()) {
            AudioPlayer.get().seekTo((int) time);
            if (AudioPlayer.get().isPausing()) {
                AudioPlayer.get().playPause();
            }
            return true;
        }
        return false;
    }

    @Override
    public void coverLoading() {
        mViewBinding.albumCoverView.songImgSetBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
    }

    @Override
    public void coverLoadSuccess(Bitmap coverLocal) {
        setBitmap(coverLocal);
    }

    @Override
    public void coverLoadFail() {
        mViewBinding.albumCoverView.songImgSetBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
    }
}