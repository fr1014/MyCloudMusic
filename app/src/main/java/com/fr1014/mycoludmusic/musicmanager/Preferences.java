package com.fr1014.mycoludmusic.musicmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.fr1014.mycoludmusic.data.entity.http.wangyiyun.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 创建时间:2020/9/28
 * 作者:fr
 * 邮箱:1546352238@qq.com
 */
public class Preferences {
    private static final String PLAY_POSITION = "play_position";
    private static final String PLAY_MODE = "play_mode";
    private static final String SPLASH_URL = "splash_url";
    private static final String NIGHT_MODE = "night_mode";
    private static final String MUSIC_SOURCE = "music_source";
    private static final String USER_PROFILE = "profile";

    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Profile getUserProfile(){
        Profile profile = null;
        try {
            Gson gson = new Gson();
            String json = getString(USER_PROFILE,"");
            Type type = new TypeToken<Profile>() {
            }.getType();
            profile = gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return profile;

    }

    public static void saveUserProfile(Profile profile){
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        saveString(USER_PROFILE,json);
    }

    public static int getPlayPosition() {
        return getInt(PLAY_POSITION, 0);
    }

    public static void savePlayPosition(int position) {
        saveInt(PLAY_POSITION, position);
    }

    public static int getPlayMode() {
        return getInt(PLAY_MODE, 0);
    }

    public static void savePlayMode(int mode) {
        saveInt(PLAY_MODE, mode);
    }

    public static String getSplashUrl() {
        return getString(SPLASH_URL, "");
    }

    public static void saveSplashUrl(String url) {
        saveString(SPLASH_URL, url);
    }

    public static void saveMusicSource(String source){
        saveMusicSource(MUSIC_SOURCE,source);
    }

//    public static boolean enableMobileNetworkPlay() {
//        return getBoolean(sContext.getString(R.string.setting_key_mobile_network_play), false);
//    }
//
//    public static void saveMobileNetworkPlay(boolean enable) {
//        saveBoolean(sContext.getString(R.string.setting_key_mobile_network_play), enable);
//    }
//
//    public static boolean enableMobileNetworkDownload() {
//        return getBoolean(sContext.getString(R.string.setting_key_mobile_network_download), false);
//    }

    public static boolean isNightMode() {
        return getBoolean(NIGHT_MODE, false);
    }

    public static void saveNightMode(boolean on) {
        saveBoolean(NIGHT_MODE, on);
    }

    private static void saveMusicSource(String key, String source) {
        getPreferences().edit().putString(key, source).apply();
    }

    public static String getMusicSource() {
        return getString(MUSIC_SOURCE,"酷我");
    }

    //    public static String getFilterSize() {
//        return getString(sContext.getString(R.string.setting_key_filter_size), "0");
//    }
//
//    public static void saveFilterSize(String value) {
//        saveString(sContext.getString(R.string.setting_key_filter_size), value);
//    }
//
//    public static String getFilterTime() {
//        return getString(sContext.getString(R.string.setting_key_filter_time), "0");
//    }
//
//    public static void saveFilterTime(String value) {
//        saveString(sContext.getString(R.string.setting_key_filter_time), value);
//    }

    private static boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    private static void saveBoolean(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    private static int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    private static void saveInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    private static long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    private static void saveLong(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    private static String getString(String key, @Nullable String defValue) {
        return getPreferences().getString(key, defValue);
    }

    private static void saveString(String key, @Nullable String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }
}
