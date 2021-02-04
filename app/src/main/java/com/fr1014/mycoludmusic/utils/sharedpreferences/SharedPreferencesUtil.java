package com.fr1014.mycoludmusic.utils.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

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