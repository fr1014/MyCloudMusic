package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.databinding.CustomviewPlaystatusbarBinding;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayEventAdapterListener;
import com.fr1014.mycoludmusic.ui.playing.CurrentPlayMusicFragment;
import com.fr1014.mycoludmusic.ui.home.playlistdialog.PlayDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

/**
 * 底部播放状态栏
 * <p>
 * 仅可在继承了BasePlayActivity的Activity中使用
 */
public class PlayStatusBarView extends LinearLayout implements View.OnClickListener, LoadResultListener {
    private CustomviewPlaystatusbarBinding mViewBinding;
    private FragmentManager fragmentManager;
    private PlayDialogFragment listDialogFragment;
    private CurrentPlayMusicFragment musicDialogFragment;
    private Context mContext;
    private OnPlayerEventListener onPlayerEventListener;

    public PlayStatusBarView(Context context, FragmentManager fragmentManager) {
        super(context);
        mContext = context;
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
        initListener();
        listDialogFragment = new PlayDialogFragment();
        musicDialogFragment = new CurrentPlayMusicFragment();
        Music music = AudioPlayer.get().getPlayMusic();
        if (music != null) {
            Bitmap coverLocal = FileUtils.getCoverLocal(music);
            if (coverLocal != null) {
                mViewBinding.ivCoverImg.setImageBitmap(coverLocal);
            } else {
                mViewBinding.ivCoverImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
            }
            setText(music);
        }
        setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
    }

    private void initListener() {
        onPlayerEventListener = new OnPlayEventAdapterListener() {
            @Override
            public void onChange(@NotNull Music music) {
                initViewData(music);
                //mViewBinding.ivCoverImg.setImageBitmap(FileUtils.getCoverLocal(music));
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

    public OnPlayerEventListener getOnPlayEventListener(){
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

    public void initViewData(Music music) {
        setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
        if (music != null) {
            setVisibility(VISIBLE);
            setText(music);
//            CoverLoadUtils.get().loadRemoteCover(mContext, music);
        }
    }

    private void setText(Music music) {
        mViewBinding.tvName.setText(music.getTitle());
        mViewBinding.tvAuthor.setText(music.getArtist());
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
                if (!musicDialogFragment.isAdded()) {
                    //当前播放的音乐的详情页
                    ((BasePlayActivity) mContext).showPlayingFragment();
                }
                break;
        }
    }

    @Override
    public void coverLoading() {
        mViewBinding.ivCoverImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
    }

    @Override
    public void coverLoadSuccess(Bitmap coverLocal) {
        mViewBinding.ivCoverImg.setImageBitmap(coverLocal);
    }

    @Override
    public void coverLoadFail() {
        mViewBinding.ivCoverImg.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.film));
    }
}
