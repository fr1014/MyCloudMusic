package com.fr1014.mycoludmusic.home.dialogfragment.currentmusic;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.FragmentCurrentMusicBinding;
import com.fr1014.mycoludmusic.home.dialogfragment.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.musicmanager.PlayModeEnum;
import com.fr1014.mycoludmusic.musicmanager.Preferences;
import com.fr1014.mycoludmusic.utils.CommonUtil;
import com.fr1014.mycoludmusic.utils.glide.DataCacheKey;

public class CurrentMusicDialogFragment extends DialogFragment implements View.OnClickListener, OnPlayerEventListener {
    private FragmentCurrentMusicBinding binding;
    private Music oldMusic;
    private Bitmap oldResource = null;
    private  MediaPlayer player;

    public CurrentMusicDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.CurrentMusicDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置content前设定
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentMusicBinding.inflate(inflater, container, false);

        binding.icBack.setOnClickListener(this);
        binding.ivState.setOnClickListener(this);
        binding.ivMusicMenu.setOnClickListener(this);
        binding.ivMode.setOnClickListener(this);
        binding.ivPre.setOnClickListener(this);
        binding.ivNext.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AudioPlayer.get().addOnPlayEventListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        player = AudioPlayer.get().getMediaPlayer();
        oldMusic = AudioPlayer.get().getPlayMusic();
        if (oldMusic == null) return;

        initView(oldMusic);

        if (AudioPlayer.get().isPlaying()) {
            binding.albumCoverView.startAnimator();
            binding.ivState.setImageResource(R.drawable.ic_stop_white);
        } else {
            binding.ivState.setImageResource(R.drawable.ic_play_white);
        }

        initPlayMode();

        binding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //拖动进度条
                binding.tvNowTime.setText(CommonUtil.formatTime(progress));
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
                binding.ivMode.setImageResource(R.drawable.ic_loop_white);
                break;
            case 1:
                binding.ivMode.setImageResource(R.drawable.ic_random_white);
                break;
            case 2:
                binding.ivMode.setImageResource(R.drawable.ic_cycle_white);
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
                if (getDialog() != null) {
                    binding.albumCoverView.endAnimator();
                    getDialog().dismiss();
                }
                break;
            case R.id.iv_pre:
                AudioPlayer.get().playPre();
                break;
            case R.id.iv_next:
                AudioPlayer.get().playNext();
                break;
            case R.id.iv_state:
                AudioPlayer.get().playPause();
                break;
            case R.id.iv_music_menu:
                new PlayListDialogFragment().show(getParentFragmentManager(), "playlist_dialog");
                break;
        }
    }

    private void initView(Music music) {
        if (oldResource == null || TextUtils.isEmpty(music.getImgUrl())) {
            binding.biBackground.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_play));
        }
        binding.albumCoverView.songImgSetBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bg_play));
        binding.tvTitle.setText(music.getTitle());
        binding.tvArtist.setText(music.getArtist());
        long duration = music.getDuration();
        if (duration == 0){
            duration = player.getDuration();
        }
        binding.sbProgress.setMax((int) duration);
        binding.tvDuration.setText(CommonUtil.formatTime(duration));
        Bitmap cacheBitmap = DataCacheKey.getCacheBitmap(music.getImgUrl());
        if (cacheBitmap == null){
            Glide.with(CurrentMusicDialogFragment.this)
                    .asBitmap()
                    .load(music.getImgUrl())
                    .placeholder(R.drawable.bg_play)
                    .error(R.drawable.film)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            setBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }else {
            setBitmap(cacheBitmap);
        }
    }

    private void setBitmap(Bitmap resource){
        binding.albumCoverView.songImgSetBitmap(resource);
        binding.biBackground.setBitmap(resource);
        oldResource = resource;
    }

    @Override
    public void onChange(Music music) {
        if (music != oldMusic) {
            initView(music);
            oldMusic = music;
        }
        binding.albumCoverView.endAnimator();
    }

    @Override
    public void onPlayerStart() {
        binding.ivState.setImageResource(R.drawable.ic_stop_white);
        Music music = AudioPlayer.get().getPlayMusic();
        if (music == oldMusic) { //选择播放的音乐与当前音乐相同
            binding.albumCoverView.resumeOrStartAnimator();
        }
    }

    @Override
    public void onPlayerPause() {
        binding.albumCoverView.pauseAnimator();
        binding.ivState.setImageResource(R.drawable.ic_play_white);
    }

    @Override
    public void onPublish(int progress) {
        binding.tvNowTime.setText(CommonUtil.formatTime(player.getCurrentPosition()));
        binding.sbProgress.setProgress((int) progress);
    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AudioPlayer.get().isPlaying()) {
            binding.albumCoverView.resumeAnimator();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.albumCoverView.pauseAnimator();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioPlayer.get().removeOnPlayEventListener(this);
    }
}