package com.fr1014.mycoludmusic.home.dialogfragment.playlist;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.databinding.FragmentPlayListDialogBinding;
import com.fr1014.mycoludmusic.home.dialogfragment.currentmusic.CurrentPlayMusicFragment;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.utils.ScreenUtil;

import java.util.List;


public class PlayListDialogFragment extends DialogFragment implements OnPlayerEventListener {

    private FragmentPlayListDialogBinding binding;
    private PlayListAdapter playListAdapter;
    private int oldPosition = -1;  //当前播放音乐的位置

    public PlayListDialogFragment() {
        // Required empty public constructor

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.PayListDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置content前设定
        dialog.setCanceledOnTouchOutside(true);  //外部点击取消

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayListDialogBinding.inflate(inflater, container, false);
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
        initHeader();
        initAdapter();
        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        binding.rvPlaylist.setAdapter(playListAdapter);
        binding.rvPlaylist.scrollToPosition(oldPosition);
    }

    /**
     * 修改布局的大小
     */
    @Override
    public void onStart() {
        super.onStart();
        resizeDialogFragment();
    }

    private void resizeDialogFragment() {
        Dialog dialog = getDialog();
        if (null != dialog) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.height = (2 * ScreenUtil.getScreenHeight(MyApplication.getInstance()) / 3);//获取屏幕的宽度，定义自己的宽度
                lp.width = (ScreenUtil.getScreenWidth(MyApplication.getInstance()));
                lp.y = 0;
                lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                window.setLayout(lp.width, lp.height);
            }

        }
    }

    private void initAdapter() {
        playListAdapter = new PlayListAdapter();
        List<Music> playList = AudioPlayer.get().getMusicList();
        if (playList != null) {
            playListAdapter.setData(playList);
            oldPosition = playList.indexOf(AudioPlayer.get().getPlayMusic());
        }
        playListAdapter.setCurrentMusic(AudioPlayer.get().getPlayMusic());

        playListAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_playlist:
                    if (oldPosition != position) {
                        Music item = (Music) adapter.getData(position);
                        AudioPlayer.get().addAndPlay(item);
                    } else {
                        //点击的为当前播放的歌曲
                        if (getActivity() instanceof BasePlayActivity) {
                            ((BasePlayActivity) getActivity()).showPlayingFragment();
                            dismissDialog();
                        }
                    }
                    break;
                case R.id.iv_del:
                    break;
            }
        });
    }

    private void initHeader() {
        int count = AudioPlayer.get().getMusicList().size();
        binding.header.tvCount.setText(String.format("(%d)", count));
    }

    private static final String TAG = "PlayListDialogFragment";

    private void dismissDialog() {
        if (getDialog() != null) {
            getDialog().dismiss();
        }
    }

    @Override
    public void onChange(Music music) {
        int position = AudioPlayer.get().getMusicList().indexOf(music);
        //oldPosition != position && music.getSongUrl() != null
        if (oldPosition != position) {
            playListAdapter.setCurrentMusic(music);
            playListAdapter.notifyDataSetChanged();
            oldPosition = position;
        }
    }

    @Override
    public void onPlayerStart() {

    }

    @Override
    public void onPlayerPause() {

    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioPlayer.get().removeOnPlayEventListener(this);
    }
}