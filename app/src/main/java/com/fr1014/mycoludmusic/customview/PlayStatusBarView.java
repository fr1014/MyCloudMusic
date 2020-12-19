package com.fr1014.mycoludmusic.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.databinding.CustomviewPlaystatusbarBinding;
import com.fr1014.mycoludmusic.listener.LoadResultListener;
import com.fr1014.mycoludmusic.ui.home.dialogfragment.currentmusic.CurrentPlayMusicFragment;
import com.fr1014.mycoludmusic.ui.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.CoverLoadUtils;
import com.fr1014.mycoludmusic.utils.FileUtils;

/**
 * 底部播放状态栏
 * <p>
 * 仅可在继承了BasePlayActivity的Activity中使用
 */
public class PlayStatusBarView extends LinearLayout implements View.OnClickListener, OnPlayerEventListener, LoadResultListener {
    private CustomviewPlaystatusbarBinding mViewBinding;
    private FragmentManager fragmentManager;
    private PlayListDialogFragment listDialogFragment;
    private CurrentPlayMusicFragment musicDialogFragment;
    private Music oldMusic;
    private Context mContext;

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
        listDialogFragment = new PlayListDialogFragment();
        musicDialogFragment = new CurrentPlayMusicFragment();
        onChange(AudioPlayer.get().getPlayMusic());
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
        if (music != null) {
            if (music != oldMusic) {
                setVisibility(VISIBLE);
                setText(music);
                setImageUrl(music);
            }
        } else {
            setVisibility(GONE);
        }
        oldMusic = music;
    }

    private void setText(Music music) {
        mViewBinding.tvName.setText(music.getTitle());
        mViewBinding.tvAuthor.setText(music.getArtist());
    }

    private void setImageUrl(Music music) {

        mViewBinding.ivCoverImg.setImageDrawable(mContext.getDrawable(R.drawable.film));
        CoverLoadUtils.loadRemoteCover(mContext,music,this);
//        Bitmap coverLocal = FileUtils.getCoverLocal(music);
//        if (coverLocal != null){
//            mViewBinding.ivCoverImg.setImageBitmap(coverLocal);
//        }else {
//            RequestOptions options = new RequestOptions()
//                    .centerCrop()
//                    .placeholder(R.drawable.film)
//                    .error(R.drawable.bg_play)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE);
//
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(music.getImgUrl())
//                    .apply(options)
//                    .into(mViewBinding.ivCoverImg);
//        }
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
    public void onChange(Music music) {
        setMusic(music);
        setPlayPause(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
//        if (musicInfoListener != null && TextUtils.isEmpty(music.getSongUrl())) {
//            musicInfoListener.songUrlIsEmpty(music);
//        }
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

    @Override
    public void success() {
        Bitmap coverLocal = FileUtils.getCoverLocal(AudioPlayer.get().getPlayMusic());
        if (coverLocal != null){
            mViewBinding.ivCoverImg.setImageBitmap(coverLocal);
        }
    }
}
