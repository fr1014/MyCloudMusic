package com.fr1014.mycoludmusic.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.databinding.FragmentCurrentMusicBinding;
import com.fr1014.mycoludmusic.home.playlist.PlayListDialogFragment;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.MusicService;
import com.fr1014.mycoludmusic.utils.CommonUtil;

public class CurrentMusicDialogFragment extends DialogFragment implements View.OnClickListener {

    private int FIRST_START_ANIMATION = 0; //旋转的动画是否已经start,0-noStart,1-start
    private FragmentCurrentMusicBinding binding;
    private MusicService.MusicControl musicControl;
    private Music oldMusic;
    ObjectAnimator rotationAnimator;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
            musicControl.registerOnStateChangeListener(onStateChangeListener);
            musicControl.registerOnProgressChangeListener(onProgressChangeListener);

            oldMusic = musicControl.getCurrentMusic();
            Glide.with(CurrentMusicDialogFragment.this)
                    .load(oldMusic.getImgUrl())
                    .placeholder(R.drawable.film)
                    .into(binding.civSongImg);
            binding.tvName.setText(oldMusic.getTitle());
            rotationAnimator = ObjectAnimator.ofFloat(binding.civSongImg, "rotation", 0f, 360f);//旋转的角度可有多个
            rotationAnimator.setDuration(20000);
            rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
            rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);//匀速
            //让旋转动画一直转，不停顿的重点
            rotationAnimator.setInterpolator(new LinearInterpolator());
            if (musicControl.isPlaying()) {
                rotationAnimator.start();//开始（重新开始）
                FIRST_START_ANIMATION = 1;
                binding.ivState.setImageResource(R.drawable.ic_stop_black);
            } else {
                binding.ivState.setImageResource(R.drawable.ic_play_black);
            }

            int mode = musicControl.getPlayMode();
            switch (mode) {
                case MusicService.TYPE_SINGLE:
                    binding.ivMode.setImageResource(R.drawable.ic_cycle);
                    break;
                case MusicService.TYPE_ORDER:
                    binding.ivMode.setImageResource(R.drawable.ic_type_order);
                    break;
                case MusicService.TYPE_RANDOM:
                    binding.ivMode.setImageResource(R.drawable.ic_random);
                    break;
            }

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
                    musicControl.seekTo(seekBar.getProgress());
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicControl.unregisterOnStateChangeListener(onStateChangeListener);
            musicControl.unregisterOnProgressChangeListener(onProgressChangeListener);
        }
    };

    private MusicService.OnStateChangeListener onStateChangeListener = new MusicService.OnStateChangeListener() {
        @Override
        public void onPlay(Music item) {
            binding.ivState.setImageResource(R.drawable.ic_stop_black);
            if (item == oldMusic) { //选择播放的音乐与当前音乐相同
                if (FIRST_START_ANIMATION == 1) { //动画已经start过
                    rotationAnimator.resume();//继续（在暂停的位置继续动画）
                } else {
                    rotationAnimator.start();
                    FIRST_START_ANIMATION = 1;
                }
            } else {
                if (item.getSongUrl() != null) {
                    oldMusic = item;
                    binding.tvName.setText(oldMusic.getTitle());
                    if (getActivity() != null) {
                        Glide.with(CurrentMusicDialogFragment.this)
                                .load(oldMusic.getImgUrl())
                                .placeholder(R.drawable.film)
                                .into(binding.civSongImg);
                    }
                    rotationAnimator.start();
                    FIRST_START_ANIMATION = 1;
                }
            }
        }

        @Override
        public void onPause() {
            rotationAnimator.pause();//暂停
            binding.ivState.setImageResource(R.drawable.ic_play_black);
        }

    };

    private MusicService.MyHandler.OnProgressChangeListener onProgressChangeListener = new MusicService.MyHandler.OnProgressChangeListener() {
        @Override
        public void onPlayProgressChange(long played, long duration) {
            binding.sbProgress.setMax((int) duration);
            binding.tvNowTime.setText(CommonUtil.formatTime(played));
            binding.tvDuration.setText(CommonUtil.formatTime(duration));
            binding.sbProgress.setProgress((int) played);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mode:
                int mode = musicControl.getPlayMode();
                switch (mode) {
                    case MusicService.TYPE_SINGLE:
                        musicControl.setPlayMode(MusicService.TYPE_ORDER);
                        binding.ivMode.setImageResource(R.drawable.ic_type_order);
                        CommonUtil.toastShort("循环播放");
                        break;
                    case MusicService.TYPE_ORDER:
                        musicControl.setPlayMode(MusicService.TYPE_RANDOM);
                        binding.ivMode.setImageResource(R.drawable.ic_random);
                        CommonUtil.toastShort("随机播放");
                        break;
                    case MusicService.TYPE_RANDOM:
                        musicControl.setPlayMode(MusicService.TYPE_SINGLE);
                        binding.ivMode.setImageResource(R.drawable.ic_cycle);
                        CommonUtil.toastShort("单曲循环");
                        break;
                }
                break;
            case R.id.ic_back:
                if (getDialog() != null) {
                    getDialog().dismiss();
                    rotationAnimator.end();//结束（回到原始位置）
                }
                break;
            case R.id.iv_pre:
                musicControl.playPre();
                break;
            case R.id.iv_next:
                musicControl.playNext();
                break;
            case R.id.iv_state:
                musicControl.playOrPause();
                break;
            case R.id.iv_music_menu:
                new PlayListDialogFragment().show(getParentFragmentManager(), "playlist_dialog");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rotationAnimator != null && rotationAnimator.isPaused() && musicControl.isPlaying()) {
            rotationAnimator.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            rotationAnimator.pause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            getActivity().unbindService(serviceConnection);
        }
    }
}