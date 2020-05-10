/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application

import android.app.Application
import com.example.application.di.AppComponent
import com.example.application.di.DaggerAppComponent
import timber.log.Timber

class App: Application() {

    val appComponent: AppComponent by lazy {
        initializeAppComponent()
    }

    private fun initializeAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}