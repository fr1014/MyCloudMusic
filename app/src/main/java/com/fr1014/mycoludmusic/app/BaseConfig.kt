package com.fr1014.mycoludmusic.app

import com.fr1014.mycoludmusic.utils.AccountUtils

object BaseConfig {
    var isLogin = false

    fun initBaseConfig() {
        isLogin = AccountUtils.getLoginStatus()
    }

    fun refreshLoginStatus() {
        isLogin = AccountUtils.getLoginStatus()
    }
}