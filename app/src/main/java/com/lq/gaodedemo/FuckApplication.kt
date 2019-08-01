package com.lq.gaodedemo

import android.app.Application
import android.content.Context

class FuckApplication : Application(){

    companion object{
        lateinit var context : Context
        var cityName = ""
        var cityCode = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}