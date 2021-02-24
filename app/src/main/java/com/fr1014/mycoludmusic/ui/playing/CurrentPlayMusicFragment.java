package com.fr1014.mycoludmusic.ui.playing;

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
import com.fr1014.mycoludmusic.ui.home.playlistdialog.PlayDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.musicmanager.lrcview.LrcView;
import com.fr1014.mycoludmusic.ui.playing.dialog.CoverInfoDialog;
import com.fr1014.mycoludmusic.utils.CommonUtils;
import com.fr1014.mycoludmusic.utils.CoverLoadUtils;
import com.fr1014.mycoludmusic.utils.FileUtils;
import com.fr1014.mycoludmusic.utils.MusicUtils;
import com.fr1014.mycoludmusic.utils.ResourceUtils;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mycoludmusic.utils.StatusBarUtils;
import com.fr1014.mymvvm.base.BaseFragment;

import java.io.File;

public class CurrentPlayMusicFragment extends BaseFragment<FragmentCurrentMusicBinding, CurrentPlayMusicViewModel> implements View.OnClickListener, OnPlayerEventListener, LrcView.OnPlayClickListener, LoadResultListener {
    private MediaPlayer player;
    private SeekBar sbProgress;
    private CoverInfoDialog coverInfoDialog;

    @Override
    protected FragmentCurrentMusicBinding getViewBinding(ViewGroup container) {
        return FragmentCurrentMusicBinding.inflate(getLayoutInflater(), container, false);
    }

    @Override
    protected CurrentPlayMusicViewModel initViewModel() {
        return new ViewModelProvider(getActivity(), AppViewModelFactory.getInstance(MyApplication.getInstance())).get(CurrentPlayMusicViewModel.class);
    }

    @Override
    protected void initView() {
        initSystemBar();
        sbProgress = mViewBinding.sbProgress;
        getLifecycle().addObserver(mViewBinding.albumCoverView);
        mViewBinding.userControlBar.initUserControlBar(mViewModel, this);

        initListener();
        initCoverLrc();
        AudioPlayer.get().addOnPlayEventListener(this);
        CoverLoadUtils.get().registerLoadListener(this);
        player = AudioPlayer.get().getMediaPlayer();
        Music music = AudioPlayer.get().getCurrentMusic();
        if (music == null) return;
        onChange(music);
        if (AudioPlayer.get().isPlaying()) {
            mViewBinding.albumCoverView.startAnimator();
            mViewBinding.playControlBar.setStateImage(R.drawable.selector_stop_state);
        } else {
            mViewBinding.playControlBar.setStateImage(R.drawable.selector_play_state);
        }
        mViewBinding.playControlBar.initPlayMode();
    }

    private void initListener() {
        mViewBinding.icBack.setOnClickListener(this);
        mViewBinding.albumCoverView.setOnClickListener(this);

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //拖动进度条
                mViewBinding.tvNowTime.setText(CommonUtils.formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()) {
                    int progress = seekBar.getProgress();
                    AudioPlayer.get().seekTo(progress);
                } else {
                    seekBar.setProgress(0);
                }
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
                new PlayDialogFragment().show(getParentFragmentManager(), "playlist_dialog");
            }
        });

        mViewBinding.albumCoverView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Music music = getCurrentPlayMusic();
                if (getContext() != null && music != null && !TextUtils.isEmpty(music.getImgUrl())){
                    if (coverInfoDialog == null){
                        coverInfoDialog = new CoverInfoDialog(getContext());
                    }
                    coverInfoDialog.setData(music);
                    coverInfoDialog.show();
                }
                return true;
            }
        });
    }

    /**
     * 修改状态栏字颜色为白色
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), false);
        int top = ScreenUtils.getStatusBarHeight();
        mViewBinding.llContent.setPadding(0, top, 0, 0);
    }

    @Override
    public void initViewObservable() {
        mViewModel.getSongLrcPath().observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] lrcPath) {
                mViewBinding.lrcView.setLabel("该歌曲暂无歌词");
                if (!lrcPath[0].equals("")) {
                    if (!lrcPath[1].equals("")) {
                        mViewBinding.lrcView.loadLrc(new File(lrcPath[0]),new File(lrcPath[1]));
                    } else {
                        mViewBinding.lrcView.loadLrc(new File(lrcPath[0]));
                    }
                } else {
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
                    getSongLrc(getCurrentPlayMusic());
                }
                break;
        }
    }

    private void initViewData(Music music) {
        initSeekBarData(music);
        setBitmap(FileUtils.getCoverLocal(music));
    }

    private void setMusicInfo(Music music) {
        mViewBinding.tvTitle.setText(music.getTitle());
        mViewBinding.tvArtist.setText(music.getArtist());
    }

    private void initSeekBarData(Music music) {
        long duration = music.getDuration();
        if (duration == 0) {
            duration = player.getDuration();
        }
        sbProgress.setMax((int) Math.abs(duration));
//        sbProgress.setSecondaryProgress(0);
        mViewBinding.tvDuration.setText(CommonUtils.formatTime(duration));
    }

    private void resetSeekBarData() {
        mViewBinding.tvNowTime.setText(R.string.start_seekbar);
        sbProgress.setSecondaryProgress(0);
    }

    //音乐旋转图、歌词
    private void initCoverLrc() {
        mViewBinding.lrcView.setDraggable(true, this);
        mViewBinding.lrcView.setOnTapListener(new LrcView.OnTapListener() {
            @Override
            public void onTap(LrcView view, float x, float y) {
                mViewBinding.albumCoverView.setVisibility(View.VISIBLE);
                if (AudioPlayer.get().isPlaying()) {
                    mViewBinding.albumCoverView.resumeAnimator();
                }
                mViewBinding.llLrc.setVisibility(View.GONE);
            }
        });
    }

    private void setBitmap(Bitmap resource) {
        if (resource != null) {
            mViewBinding.albumCoverView.songImgSetBitmap(resource);
            mViewBinding.biBackground.setBitmap(resource);
        }else {
            mViewBinding.biBackground.setBackgroundDrawable(ResourceUtils.getGrayDrawable(getContext(),R.drawable.palying_default_bg));
        }
    }

    @Override
    public void onChange(Music music) {
        initViewData(music);
        changeMusicPlay(music);
    }

    private void changeMusicPlay(Music music) {
        Music currentPlayMusic = getCurrentPlayMusic();
        if (currentPlayMusic != null && !MusicUtils.INSTANCE.isSameMusic(currentPlayMusic,music)) {
            AudioPlayer.get().stopPlayer();
        }
        if (mViewBinding.llLrc.getVisibility() == View.VISIBLE) {
            getSongLrc(music); //切换歌时，请求歌词
        }
        mViewBinding.albumCoverView.endAnimator();
        resetSeekBarData();
        setMusicInfo(music);
        mViewBinding.userControlBar.initLikeIcon(music);
    }

    @Override
    public void onPlayerComplete() {
        mViewBinding.albumCoverView.endAnimator();
    }

    @Override
    public void onPlayerStart() {
//        initSeekBarData(AudioPlayer.get().getPlayMusic());
        mViewBinding.playControlBar.setStateImage(R.drawable.selector_stop_state);
        mViewBinding.albumCoverView.resumeOrStartAnimator();
    }

    @Override
    public void onPlayerPause() {
        mViewBinding.albumCoverView.pauseAnimator();
        mViewBinding.playControlBar.setStateImage(R.drawable.selector_play_state);
    }

    @Override
    public void onPublish(int progress) {
        mViewBinding.tvNowTime.setText(CommonUtils.formatTime(player.getCurrentPosition()));
        sbProgress.setProgress(progress);

        if (isVisible() && (mViewBinding.llLrc.getVisibility() == View.VISIBLE) && mViewBinding.lrcView.hasLrc()) {
            mViewBinding.lrcView.updateTime(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (percent > 0){
            sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
        }
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

    private Music getCurrentPlayMusic(){
        return AudioPlayer.get().getCurrentMusic();
    }

    //加载图片
    @Override
    public void coverLoading() {
        mViewBinding.albumCoverView.songImgSetBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
    }

    @Override
    public void coverLoadSuccess(Music music, Bitmap coverLocal) {
        if (AudioPlayer.get().getCurrentMusic() == music){
            setBitmap(coverLocal);
        }
    }

    @Override
    public void coverLoadFail() {
        mViewBinding.albumCoverView.songImgSetBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null) {
            StatusBarUtils.setImmersiveStatusBar(getActivity().getWindow(), true);
        }
        AudioPlayer.get().removeOnPlayEventListener(this);
        CoverLoadUtils.get().removeLoadListener(this);
        super.onDestroy();
    }

}