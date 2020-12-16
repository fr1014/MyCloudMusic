package com.fr1014.mycoludmusic.ui.home.dialogfragment.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity;
import com.fr1014.mycoludmusic.data.source.local.room.DBManager;
import com.fr1014.mycoludmusic.databinding.FragmentPlaydialogpageBinding;
import com.fr1014.mycoludmusic.musicmanager.AudioPlayer;
import com.fr1014.mycoludmusic.musicmanager.Music;
import com.fr1014.mycoludmusic.musicmanager.OnPlayerEventListener;
import com.fr1014.mycoludmusic.rx.MyDisposableObserver;
import com.fr1014.mycoludmusic.rx.RxSchedulers;
import com.fr1014.mycoludmusic.utils.CommonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Create by fanrui on 2020/12/17
 * Describe:
 */
public class PlayDialogPageFragment extends Fragment implements OnPlayerEventListener {
    private static String PAGE_TYPE = "page_type";
    private static final int PAGE_TYPE_HISTORY = 0; //历史播放
    private static final int PAGE_TYPE_CURRENT = 1; //当前播放
    private int pageType;
    private FragmentPlaydialogpageBinding binding;
    private PlayListAdapter playListAdapter;
    private int oldPosition = -1;  //当前播放音乐的位置
    private OnDialogListener dialogListener;

    public PlayDialogPageFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt(PAGE_TYPE);
        }
    }

    public static PlayDialogPageFragment getInstance(int position) {
        PlayDialogPageFragment dialogPageFragment = new PlayDialogPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_TYPE, position);
        dialogPageFragment.setArguments(bundle);
        return dialogPageFragment;
    }

    public void setDialogListener(OnDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaydialogpageBinding.inflate(getLayoutInflater());
        initHeader();
        initAdapter();
        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        binding.rvPlaylist.setAdapter(playListAdapter);
        binding.rvPlaylist.scrollToPosition(oldPosition);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                            dialogListener.dialogDismiss();
                        }
                    }
                    break;
                case R.id.iv_del:
                    Music music = (Music) adapter.getData(position);
                    DBManager.get().getMusicEntityItem(music).observe(getViewLifecycleOwner(), new Observer<MusicEntity>() {
                        @Override
                        public void onChanged(MusicEntity musicEntity) {
                            if (musicEntity != null) {
                                DBManager.get().delete(musicEntity);
                            }
                        }
                    });
                    break;
            }
        });

        //删除之后刷新adapter中的数据
        DBManager.get().getListMusicEntity().observe(getViewLifecycleOwner(), new Observer<List<MusicEntity>>() {
            @Override
            public void onChanged(List<MusicEntity> musicEntities) {
                if (!CommonUtil.isEmptyList(musicEntities)) {
                    Observable.just(musicEntities)
                            .compose(RxSchedulers.applyIO())
                            .map(new Function<List<MusicEntity>, List<Music>>() {
                                @Override
                                public List<Music> apply(@io.reactivex.annotations.NonNull List<MusicEntity> musicEntities) throws Exception {
                                    List<Music> musicList = new ArrayList<>();
                                    for (MusicEntity musicEntity : musicEntities) {
                                        musicList.add(new Music(musicEntity.getId(), musicEntity.getArtist(), musicEntity.getTitle(), "", musicEntity.getImgUrl(), musicEntity.getMusicRid(), musicEntity.getDuration()));
                                    }
                                    Collections.reverse(musicList);
                                    return musicList;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new MyDisposableObserver<List<Music>>() {
                                @Override
                                public void onNext(@io.reactivex.annotations.NonNull List<Music> musicList) {
                                    playListAdapter.setData(musicList);
                                    playListAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });
    }

    private void initHeader() {
        if (pageType == PAGE_TYPE_HISTORY) {
            binding.header.tvType.setText("历史播放");
            int count = AudioPlayer.get().getMusicList().size();
            binding.header.tvCount.setText(String.format("(%d)", count));
        } else {
            binding.header.tvType.setText("当前播放");
            int count = AudioPlayer.get().getMusicList().size();
            binding.header.tvCount.setText(String.format("(%d)", count));
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

    public interface OnDialogListener {
        void dialogDismiss();
    }
}
