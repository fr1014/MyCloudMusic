package com.fr1014.mycoludmusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.eventbus.CoverSaveEvent;
import com.fr1014.mycoludmusic.musicmanager.Music;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by fanrui on 2020/12/11
 * Describe:
 */
public class FileUtils {
    private static final String LRC = ".lrc";
    private static final String PNG = ".png";

    private static String getAppDir() {
        return MyApplication.getInstance().getFilesDir() + "/MyCloudMusic";
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "/Lyric/";
        return mkdirs(dir);
    }

    public static String getCoverDir(){
        String dir = getAppDir() + "/Cover";
        return mkdirs(dir);
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    public static String getLrcFileName(String artist, String title) {
        return getFileName(artist, title) + LRC;
    }

    public static String getCoverFileName(String artist, String title){
        return getFileName(artist, title) + PNG;
    }

    public static String getFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        return artist + " - " + title;
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static void saveLrcFile(String path, String content) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileEmpty(String path){
        File file = new File(path);
        return !file.exists() || file.length() == 0;
    }

    public static void saveCoverToLocal(Bitmap bitmap, Music music) {
        String path = FileUtils.getCoverDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            EventBus.getDefault().post(new CoverSaveEvent(true));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getCoverLocal(Music music){
        String path = FileUtils.getCoverDir() + FileUtils.getLrcFileName(music.getArtist(), music.getTitle());
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
