package com.example.ultrasoft.ui.activity

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.ultrasoft.R
import com.example.ultrasoft.base.BaseActivity
import com.example.ultrasoft.databinding.ActivityMainBinding
import com.example.ultrasoft.utility.AppConstants
import com.example.ultrasoft.utility.logE
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun installTheme() {
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()
    }

    override fun setUpViews() {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        if (appPreferences.getToken() == "NA") {
            graph.setStartDestination(R.id.loginFragment)
        } else {
            graph.setStartDestination(R.id.dashBoardFragment)
        }
        navHostFragment.navController.graph = graph
//        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->     }
    }


}