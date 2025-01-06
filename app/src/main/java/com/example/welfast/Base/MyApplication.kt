package com.example.welfast.Base

import android.app.Application

class MyApplication: Application()  {
    override fun onCreate() {
        super.onCreate()
        instance = this
        PreferenceHelper.init(applicationContext)
    }
    companion object {
        lateinit var instance: MyApplication
            private set
    }



}