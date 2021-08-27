package com.fr1014.mycoludmusic.ui.home.playlistdialog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fr1014.mycoludmusic.R;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.base.BasePlayActivity;
import com.fr1014.mycoludmusic.databinding.FragmentPagerPlaydialogBinding;
import com.fr1014.mycoludmusic.musicmanager.listener.OnPlayerEventListener;
import com.fr1014.mycoludmusic.musicmanager.player.Music;
import com.fr1014.mycoludmusic.musicmanager.player.MusicKt;
import com.fr1014.mycoludmusic.musicmanager.player.MyAudioPlay;
import com.fr1014.mycoludmusic.musicmanager.player.PlayerEvent;
import com.fr1014.mycoludmusic.utils.ScreenUtils;
import com.fr1014.mycoludmusic.ui.playing.ReBoundActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Create by fanrui on 2020/12/17
 * Describe:
 */
public class PlayDialogPageFragment extends Fragment {
    private static String PAGE_TYPE = "page_type";
    private static final int PAGE_TYPE_HISTORY = 0; //历史播放
    private static final int PAGE_TYPE_CURRENT = 1; //当前播放
    private int pageType;
    private FragmentPagerPlaydialogBinding binding;
    private PlayDialogAdapter playDialogAdapter;
    private int oldPosition = -1;  //当前播放音乐的位置
    private OnDialogListener dialogListener;
    private OnPlayerEventListener onPlayerEventListener;

    public PlayDialogPageFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        binding = FragmentPagerPlaydialogBinding.inflate(getLayoutInflater());
        initHeader();
        initAdapter();
        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(MyApplication.getInstance()));
        binding.rvPlaylist.setAdapter(playDialogAdapter);

        inPageTypeData();
        return binding.getRoot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(PlayerEvent event) {
        switch (event.getType()) {
            case OnChange:
                if (pageType == PAGE_TYPE_CURRENT) {
                    Music music = event.getMusic();
                    List<Music> musicList = MyAudioPlay.get().getCurrentMusicList();
                    if (music != null) {
                        int position = MusicKt.indexOf(music, musicList);
                        setHeaderCount(musicList.size());
                        playDialogAdapter.setData(musicList);
                        if (oldPosition != position) {
                            playDialogAdapter.setCurrentMusic(music);
                            playDialogAdapter.notifyDataSetChanged();
                            oldPosition = position;
                        }
                    }
                }
                break;
            case OnPlayListChange:
                playDialogAdapter.setData(event.getMusicList());
                break;
        }
    }

    private void inPageTypeData() {
        if (pageType == PAGE_TYPE_HISTORY) {
            binding.header.tvMode.setText(getString(R.string.open_outside));
            binding.header.tvMode.setTextColor(getContext().getResources().getColor(R.color.font_gray));
            binding.header.tvMode.setOnClickListener((View.OnClickListener) v -> dialogListener.dialogDismiss());
            //刷新adapter中的数据
//            DBManager.get().getLocalMusicListLive(true).observe(getViewLifecycleOwner(), new Observer<List<MusicEntity>>() {
//                @Override
//                public void onChanged(List<MusicEntity> musicEntities) {
//                    if (!CollectionUtils.isEmptyList(musicEntities)) {
//                        Observable.just(musicEntities)
//                                .compose(RxSchedulers.applyIO())
//                                .map(new Function<List<MusicEntity>, List<Music>>() {
//                                    @Override
//                                    public List<Music> apply(@io.reactivex.annotations.NonNull List<MusicEntity> musicEntities) throws Exception {
//                                        List<Music> musicList = new ArrayList<>();
//                                        for (MusicEntity musicEntity : musicEntities) {
//                                            musicList.add(new Music(musicEntity.getId(), musicEntity.getArtist(), musicEntity.getTitle(), "", musicEntity.getImgUrl(), musicEntity.getMusicRid(), musicEntity.getDuration()));
//                                        }
//                                        Collections.reverse(musicList);
//                                        return musicList;
//                                    }
//                                })
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new MyDisposableObserver<List<Music>>() {
//                                    @Override
//                                    public void onNext(@io.reactivex.annotations.NonNull List<Music> musicList) {
//                                        setHeaderCount(musicList.size());
//                                        playDialogAdapter.setData(musicList);
//                                        playDialogAdapter.notifyDataSetChanged();
//                                    }
//                                });
//                    }
//                }
//            });
        } else {
            initPlayMode();
            playDialogAdapter.setData(MyAudioPlay.get().getCurrentMusicList());
            scrollToPosition();
            playDialogAdapter.setCurrentMusic(MyAudioPlay.get().getCurrentMusic());
            playDialogAdapter.notifyDataSetChanged();
            binding.header.tvMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAudioPlay.get().switchPlayMode();
                    initPlayMode();
                }
            });
        }
    }

    public void scrollToPosition() {
        Music currentMusic = MyAudioPlay.get().getCurrentMusic();
        if (currentMusic != null) {
            oldPosition = MusicKt.indexOf(currentMusic, MyAudioPlay.get().getCurrentMusicList());
        }
        if (oldPosition < 0) return;
        binding.rvPlaylist.scrollToPosition(oldPosition);
    }

    private void initAdapter() {
        playDialogAdapter = new PlayDialogAdapter();
        playDialogAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.ll_playlist:
                    if (pageType == PAGE_TYPE_CURRENT) {
                        if (oldPosition != position) {
                            Music item = (Music) adapter.getData(position);
                            MyAudioPlay.get().addPlayMusic(item);
                        } else {
                            //点击的为当前播放的歌曲
                            if (getActivity() instanceof BasePlayActivity) {
                                startActivity(new Intent(getContext(), ReBoundActivity.class));
                                dialogListener.dialogDismiss();
                            }
                        }
                    } else {
//                        Music item = (Music) adapter.getData(position);
//                        List<Music> musicList = AudioPlayer.get().getMusicList();
//                        for (Music music : musicList) {
//                            if (TextUtils.equals(music.getTitle(), item.getTitle()) && TextUtils.equals(music.getArtist(), item.getArtist())) {
//                                AudioPlayer.get().addAndPlay(music);
//                                return;
//                            }
//                        }
//                        AudioPlayer.get().addAndPlay(item);
                        dialogListener.dialogDismiss();
                    }
                    break;
                case R.id.iv_del:
//                    Music music = (Music) adapter.getData(position);
//                    if (pageType == PAGE_TYPE_HISTORY) {
//                        DBManager.get().getMusicEntityItem(music).observe(getViewLifecycleOwner(), new Observer<MusicEntity>() {
//                            @Override
//                            public void onChanged(MusicEntity musicEntity) {
//                                if (musicEntity != null) {
//                                    DBManager.get().delete(musicEntity);
//                                }
//                            }
//                        });
//                    } else {
//                        MyAudioPlay.get().delete(music);
//                        DBManager.get().getMusicEntityItem(music).observe(getViewLifecycleOwner(), new Observer<MusicEntity>() {
//                            @Override
//                            public void onChanged(MusicEntity musicEntity) {
//                                if (musicEntity != null) {
//                                    DBManager.get().delete(musicEntity);
//                                }
//                            }
//                        });
//                        initHeader();
//                        playDialogAdapter.setData(AudioPlayer.get().getMusicList());
//                        playDialogAdapter.notifyDataSetChanged();
//                    }
                    break;
            }
        });
    }

    private void initHeader() {
        if (pageType == PAGE_TYPE_HISTORY) {
            binding.header.tvType.setText("历史播放");
        } else {
            binding.header.tvType.setText("当前播放");
            int count = MyAudioPlay.get().getCurrentMusicList().size();
            setHeaderCount(count);
        }
    }

    private void setHeaderCount(int count) {
        binding.header.tvCount.setText(String.format("(%d)", count));
    }

    public void initPlayMode() {
        if (pageType == PAGE_TYPE_CURRENT) {
            int mode = MyAudioPlay.get().getPlayMode().value();
            setImageMode(mode);
        }
    }

    private void setImageMode(int mode) {
        Drawable drawable;
        String tvMode;
        switch (mode) {
            case 0:
                drawable = getContext().getDrawable(R.drawable.selector_loop);
                tvMode = getString(R.string.loop);
                break;
            case 1:
                drawable = getContext().getDrawable(R.drawable.selector_random);
                tvMode = getString(R.string.shuffle);
                break;
            case 2:
                drawable = getContext().getDrawable(R.drawable.selector_cycle);
                tvMode = getString(R.string.cycle);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, ScreenUtils.dp2px(16), ScreenUtils.dp2px(16));
            binding.header.tvMode.setCompoundDrawables(drawable, null, null, null);
            binding.header.tvMode.setText(tvMode);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public interface OnDialogListener {
        void dialogDismiss();
    }
}
