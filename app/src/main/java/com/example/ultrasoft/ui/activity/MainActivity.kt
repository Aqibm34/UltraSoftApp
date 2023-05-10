package com.example.ultrasoft.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseActivity
import com.example.ultrasoft.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun installTheme() {
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()
    }

    override fun setUpViews() {

    }
}