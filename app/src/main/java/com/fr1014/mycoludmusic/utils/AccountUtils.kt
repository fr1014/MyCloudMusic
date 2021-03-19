package com.fr1014.mycoludmusic.utils

import com.fr1014.mycoludmusic.musicmanager.Preferences
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesConst
import com.fr1014.mycoludmusic.utils.sharedpreferences.SharedPreferencesUtil

object AccountUtils {

    fun getLoginStatus(): Boolean {
        var isLogin = false
        val userProfile = Preferences.getUserProfile()
        if (userProfile != null) {
            isLogin = true
        }
        return isLogin
    }

    fun getLoginTourist(): Boolean {
        return SharedPreferencesUtil.getBoolean(SharedPreferencesConst.ACCOUNT, SharedPreferencesConst.ACCOUNT_TOURIST_KEY, false)
    }

    fun saveLoginTourist(tourist: Boolean) {
        SharedPreferencesUtil.putBoolean(SharedPreferencesConst.ACCOUNT, SharedPreferencesConst.ACCOUNT_TOURIST_KEY, tourist)
    }

    fun saveCookies(cookies: Set<String>) {
        SharedPreferencesUtil.saveStringSet(SharedPreferencesConst.ACCOUNT, SharedPreferencesConst.ACCOUNT_COOKIES_KEY, cookies)
    }

    fun getCookie(): Set<String> {
        return SharedPreferencesUtil.getStringSet(SharedPreferencesConst.ACCOUNT, SharedPreferencesConst.ACCOUNT_COOKIES_KEY, HashSet<String>());
    }
}