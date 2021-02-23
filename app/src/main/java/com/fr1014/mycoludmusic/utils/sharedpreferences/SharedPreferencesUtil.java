package com.fr1014.mycoludmusic.utils.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.fr1014.mycoludmusic.musicmanager.Music;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesUtil {

    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static SharedPreferences getSharedPreferences(String name) {
        if (mContext == null) {
            throw new RuntimeException("You must call SharedPreferencesUtil.init(context) when your Application be created!");
        }
        return mContext.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(String name) {
        return getSharedPreferences(name).edit();
    }

    public static boolean getBoolean(String name, String key, boolean defValue) {
        return getSharedPreferences(name).getBoolean(key, defValue);
    }

    public static void putBoolean(String name, String key, boolean value) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static int getInt(String name, String key, int defValue) {
        return getSharedPreferences(name).getInt(key, defValue);
    }

    public static void putInt(String name, String key, int value) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.putInt(key, value);
        editor.apply();
    }

    public static float getFloat(String name, String key, float defValue) {
        return getSharedPreferences(name).getFloat(key, defValue);
    }

    public static void putFloat(String name, String key, float value) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.putFloat(key, value);
        editor.apply();
    }

    public static long getLong(String name, String key, long defValue) {
        return getSharedPreferences(name).getLong(key, defValue);
    }

    public static void putLong(String name, String key, long value) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.putLong(key, value);
        editor.apply();
    }

    public static String getString(String name, String key, String defValue) {
        return getSharedPreferences(name).getString(key, defValue);
    }

    public static void putString(String name, String key, String value) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.putString(key, value);
        editor.apply();
    }

    public static void putMusic(String name,String key,Music music){
        if (music == null) return;
        SharedPreferences.Editor editor = getEditor(name);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(music);
        editor.clear();
        editor.putString(key, strJson);
        editor.apply();
    }

    public static Music getMusic(String name, String key) {
        String strJson = getSharedPreferences(name).getString(key, null);
        if (null == strJson) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(strJson, new TypeToken<Music>() {
        }.getType());
    }

    /**
     * 保存List
     */
    public static <T> void setDataList(String name,String key, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0)
            return;

        SharedPreferences.Editor editor = getEditor(name);
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        editor.clear();
        editor.putString(key, strJson);
        editor.apply();
    }

    /**
     * 获取List
     * @return
     */
    public static List<Music> getDataList(String name, String key) {
        List<Music> datalist = new ArrayList<Music>();
        String strJson = getSharedPreferences(name).getString(key, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<Music>>() {
        }.getType());
        return datalist;

    }

    public static void remove(String name, String key) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.remove(key);
        editor.apply();
    }

    public static void clear(String name) {
        SharedPreferences.Editor editor = getEditor(name);
        editor.clear();
        editor.apply();
    }
}