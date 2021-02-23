package com.fr1014.mycoludmusic.utils;

import com.fr1014.mycoludmusic.musicmanager.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机播放的算法
 */
public class ShuffleUtils {

    public static List<Music> shuffle(List<Music> musicList) {
        if (musicList.size() == 1) return musicList;
        int key;
        Music temp;
        Random rand = new Random();
        List<Music> musics = new ArrayList<>(musicList);
        for (int i = 0; i < musics.size(); i++) {
            key = rand.nextInt(musics.size() - 1);
            temp = musics.get(i);
            musics.set(i,musics.get(key));
            musics.set(key,temp);
        }
        return musics;
    }
}