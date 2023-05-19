package com.example.ultrasoft.utility

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

class AppPreferences(context: Context) {
    private val appPrefsName = "app_prefs_ultrasoft"
    private var mPreferences = context.getSharedPreferences(appPrefsName, Context.MODE_PRIVATE)
    private var mEditor: SharedPreferences.Editor? = null

    companion object {
        private var appPreferences: AppPreferences? = null
        fun getInstance(context: Context): AppPreferences {
            if (appPreferences == null) {
                appPreferences = AppPreferences(context)
            }
            return appPreferences as AppPreferences
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                mPreferences = EncryptedSharedPreferences.create(
                    context,
                    appPrefsName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e: GeneralSecurityException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        mEditor = mPreferences.edit()
    }


    //Wallet
    private val token = "token"
    private val loggedIn = "loggedIn"
    private val defaultValue = "NA"


    fun clearPreferences() {
        try {
            mEditor?.clear()?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setToken(token: String) {
        mEditor?.putString(this.token, token)
        mEditor?.apply()
    }

    fun getToken(): String {
        return mPreferences.getString(token, defaultValue)!!
    }

    fun setIsLoggedIn(value: String) {
        mEditor?.putString(this.loggedIn, token)
        mEditor?.apply()
    }

    fun getIsLoggedIn(): String {
        return mPreferences.getString(loggedIn, defaultValue)!!
    }
}