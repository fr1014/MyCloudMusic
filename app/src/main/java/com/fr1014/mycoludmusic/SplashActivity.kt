package com.fr1014.mycoludmusic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fr1014.mycoludmusic.ui.login.LoginActivity
import com.fr1014.mycoludmusic.utils.AccountUtils

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loginTourist = AccountUtils.getLoginTourist()
        val loginStatus = AccountUtils.getLoginStatus()
        if (loginTourist || loginStatus) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}