package com.fr1014.mycoludmusic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.provider.MediaStore;
import com.fr1014.mycoludmusic.app.MyApplication;
import com.fr1014.mycoludmusic.musicmanager.player.CoverStatusType;
import com.fr1014.mycoludmusic.musicmanager.player.Music;
import com.fr1014.mycoludmusic.musicmanager.player.MusicCoverEvent;
import com.fr1014.mycoludmusic.musicmanager.player.Notifier;

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

    public static String getPicDir() {
        String dir = getAppDir() + "/picture/";
        return mkdirs(dir);
    }

    public static String getTlyLrcDir() {
        String dir = getAppDir() + "/Tlyric/";
        return mkdirs(dir);
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "/Lyric/";
        return mkdirs(dir);
    }

    public static String getCoverDir() {
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

    public static String getCoverFileName(String artist, String title) {
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

    public static boolean isFileEmpty(String path) {
        File file = new File(path);
        return !file.exists() || file.length() == 0;
    }

    public static void saveCoverToLocal(Bitmap bitmap, Music music) {
        String path = FileUtils.getCoverDir() + FileUtils.getCoverFileName(music.getArtist(), music.getTitle());
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
//            Bitmap result = null;
//            int height = bitmap.getHeight() / 1000;
//            int inSampleSize = 2;
//            switch (height) {
//                case 6:
//                case 5:
//                    inSampleSize = 26;
//                    break;
//                case 4:
//                case 3:
//                    inSampleSize = 6;
//                    break;
//                case 2:
//                    inSampleSize = 4;
//                    break;
//                case 1:
//                case 0:
//                    inSampleSize = 2;
//                    break;
//            }
//            result = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / inSampleSize, bitmap.getHeight() / inSampleSize, true);
//            result.compress(Bitmap.CompressFormat.PNG, 90, out);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
//            for (LoadResultListener loadResultListener : loadResultListeners) {
//                loadResultListener.coverLoadSuccess(music,bitmap);
//            }
            EventBus.getDefault().post(new MusicCoverEvent(CoverStatusType.Success, music, bitmap));
            Notifier.get().showPlay(music);
//            EventBus.getDefault().post(new CoverSaveEvent(true));
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new MusicCoverEvent(CoverStatusType.Fail));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getCoverLocal(Music music) {
        if (music == null) return null;
        String path = FileUtils.getCoverDir() + FileUtils.getCoverFileName(music.getArtist(), music.getTitle());
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (IOException ignored) {
            EventBus.getDefault().post(new MusicCoverEvent(CoverStatusType.Fail));
        }
        return bitmap;
    }

    public static boolean saveBitmap(Context context, Bitmap bmp,String path) {
        // 首先保存图片
        String fileName = path + ".jpg";
        File file = new File(getPicDir(), fileName);
        try (FileOutputStream fos = new FileOutputStream(file)){
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 通知图库更新
        String[] paths = new String[]{file.getAbsolutePath()};
        MediaScannerConnection.scanFile(context, paths, null, null);
        return true;
    }
}
