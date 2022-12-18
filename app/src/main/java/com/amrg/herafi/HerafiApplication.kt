package com.amrg.herafi

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HerafiApplication : Application() {
    /*
    override fun attachBaseContext(base: Context) {
        val config = Configuration()
        val locale = Locale("ar")
        config.setLocales(LocaleList(locale))
        super.attachBaseContext(base.createConfigurationContext(config))
    }
    */
    companion object {
        lateinit var instance: Application
        val applicationContext: Context
            get() = instance.applicationContext
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}