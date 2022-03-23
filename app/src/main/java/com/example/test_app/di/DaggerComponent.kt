package com.example.test_app.di

import com.example.test_app.DetailFragment
import com.example.test_app.ListFragment
import com.example.test_app.app.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface DaggerComponent {
    fun inject(app: App)
    fun inject(fragment: ListFragment)
    fun inject(fragment: DetailFragment)
}