package com.example.test_app.app

import android.app.Application
import com.example.test_app.di.DaggerComponent
import com.example.test_app.di.DaggerDaggerComponent
import com.example.test_app.di.DataModule

class App: Application() {

    lateinit var component: DaggerComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerDaggerComponent.builder().dataModule(DataModule(this)).build()
        component.inject(this)
    }
}