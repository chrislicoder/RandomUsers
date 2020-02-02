package com.urgentx.randomusers

import android.app.Application
import com.urgentx.randomusers.di.DependencyInjector

class RandomUsersApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyInjector.initialize(this)
    }
}

