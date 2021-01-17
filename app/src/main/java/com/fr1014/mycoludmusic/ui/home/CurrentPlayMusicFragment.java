package com.fr1014.mycoludmusic.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.AppViewModelFactory;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.customview.PlayControlBarView;
import com.fr1014.mycoludmusic.databinding.FragmentCurrentMusicBinding;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.ui.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.ui.home.toplist.TopListViewModel;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.musicmanager.lrcview.LrcView;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.CoverLoadUtils;
import com.fr1014.mycoludmusic.utils.FileUtils;
import com.fr1014.mycoludmusic.utils.ScreenUtil;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseFragment;

import java.io.File;

public class CurrentPlayMusicFragment extends BaseFragment<FragmentCurrentMusicBinding, TopListViewModel> implements View.OnClickListener, OnPlayerEventListener, LrcView.OnPlayClickListener, LoadResultListener {
    private MediaPlayer player;
    private Music oldMusic;

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
        oldMusic = AudioPlayer.get().getPlayMusic();
        if (oldMusic == null) return;
        setMusicInfo(oldMusic);
        initViewData(oldMusic);

        if (AudioPlayer.get().isPlaying()) {
            mViewBinding.albumCoverView.startAnimator();
            mViewBinding.playControlBar.setStateImage(R.drawable.ic_stop_white);
        } else {
            mViewBinding.playControlBar.setStateImage(R.drawable.ic_play_white);
        }
        mViewBinding.playControlBar.initPlayMode();
    }

    private void initListener() {
        mViewBinding.icBack.setOnClickListener(this);
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

        mViewBinding.playControlBar.setPlayControlBarClick(new PlayControlBarView.OnPlayControlBarClick() {
            @Override
            public void pre(Music pre) {
                changeMusicPlay(pre);
            }

            @Override
            public void next(Music next) {
                changeMusicPlay(next);
            }

            @Override
            public void openMenu() {
                new PlayListDialogFragment().show(getParentFragmentManager(), "playlist_dialog");
            }
        });
    }

    private void changeMusicPlay(Music music){
        AudioPlayer.get().stopPlayer();
        mViewBinding.albumCoverView.endAnimator();
        resetSeekBarData();
        setMusicInfo(music);
        oldMusic = music;
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
                mViewBinding.lrcView.setLabel("该歌曲暂无歌词");
                if (!lrcPath.equals("")) {
                    mViewBinding.lrcView.loadLrc(new File(lrcPath));
                }else {
                    mViewBinding.lrcView.loadLrc("[00:00.000]该歌曲暂无歌词");
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                mViewBinding.albumCoverView.endAnimator();
                getActivity().onBackPressed();
                break;
            case R.id.album_cover_view:
                if (mViewBinding.albumCoverView.getVisibility() == View.VISIBLE) {
                    mViewBinding.albumCoverView.setVisibility(View.GONE);
                    mViewBinding.albumCoverView.pauseAnimator();
                    mViewBinding.llLrc.setVisibility(View.VISIBLE);
                    getSongLrc(AudioPlayer.get().getPlayMusic());
                }
                break;
        }
    }

    private void initViewData(Music music) {
        initSeekBarData(music);
        setMusicImage(music);
    }

    private void setMusicInfo(Music music){
        mViewBinding.tvTitle.setText(music.getTitle());
        mViewBinding.tvArtist.setText(music.getArtist());
    }

    private void setMusicImage(Music music){
        if (TextUtils.isEmpty(music.getImgUrl())) {
            mViewBinding.biBackground.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_play));
            return;
        }
        setBitmap(FileUtils.getCoverLocal(music));
    }

    private void initSeekBarData(Music music) {
        long duration = music.getDuration();
        if (duration == 0) {
            duration = player.getDuration();
        }
        mViewBinding.sbProgress.setMax((int) duration);
        mViewBinding.sbProgress.setSecondaryProgress(0);
        mViewBinding.tvDuration.setText(CommonUtil.formatTime(duration));
    }

    private void resetSeekBarData(){
        mViewBinding.tvNowTime.setText(R.string.start_seekbar);
        mViewBinding.sbProgress.setSecondaryProgress(0);
    }

    //音乐旋转图、歌词
    private void initCoverLrc() {
        mViewBinding.lrcView.setDraggable(true, this);
        mViewBinding.lrcView.setOnTapListener(new LrcView.OnTapListener() {
            @Override
            public void onTap(LrcView view, float x, float y) {
                mViewBinding.albumCoverView.setVisibility(View.VISIBLE);
                mViewBinding.albumCoverView.resumeAnimator();
                mViewBinding.llLrc.setVisibility(View.GONE);
            }
        });
    }

    private void setBitmap(Bitmap resource) {
        if (resource != null) {
            mViewBinding.albumCoverView.songImgSetBitmap(resource);
            mViewBinding.biBackground.setBitmap(resource);
        }
    }

    @Override
    public void onChange(Music music) {
        initViewData(music);
        if (mViewBinding.llLrc.getVisibility() == View.VISIBLE) {
            getSongLrc(music); //切换歌时，请求歌词
        }
        if (oldMusic != music){
            setMusicInfo(music);
            resetSeekBarData();
            oldMusic = music;
        }
    }

    @Override
    public void onPlayerComplete() {
        mViewBinding.albumCoverView.endAnimator();
    }

    @Override
    public void onPlayerStart() {
//        initSeekBarData(AudioPlayer.get().getPlayMusic());
        mViewBinding.playControlBar.setStateImage(R.drawable.ic_stop_white);
        mViewBinding.albumCoverView.resumeOrStartAnimator();
    }

    @Override
    public void onPlayerPause() {
        mViewBinding.albumCoverView.pauseAnimator();
        mViewBinding.playControlBar.setStateImage(R.drawable.ic_play_white);
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
        mViewBinding.sbProgress.setSecondaryProgress(mViewBinding.sbProgress.getMax() * 100 / percent);
    }

    private void getSongLrc(Music music) {
        String mTag = music.getTitle() + music.getArtist();
        Object tag = mViewBinding.lrcView.getTag();
        if (tag == null || !tag.equals(mTag)) {
            mViewBinding.lrcView.setTag(mTag);
            mViewBinding.lrcView.loadLrc(new File(""));
            mViewBinding.lrcView.setLabel("正在搜索歌词");
            mViewModel.getSongLrc(music);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
//            AudioPlayer.get().removeOnPlayEventListener(this);
//            CoverLoadUtils.get().removeLoadListener(this);

            StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), true);
            mViewBinding.albumCoverView.endAnimator();
        } else {
//            AudioPlayer.get().addOnPlayEventListener(this);
//            CoverLoadUtils.get().registerLoadListener(this);

            initViewData(AudioPlayer.get().getPlayMusic());
            if (AudioPlayer.get().isPlaying()) {
                StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), false);
                mViewBinding.albumCoverView.startAnimator();
            }
        }
    }

    //点击歌词
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

    //加载图片
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
        if (mViewBinding != null){
            mViewBinding.albumCoverView.endAnimator();
        }
        CoverLoadUtils.get().removeLoadListener(this);
        super.onDestroy();
    }

}