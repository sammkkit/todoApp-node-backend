package com.example.todoapp

import android.app.Application
import com.example.todoapp.di.networkModule
import com.example.todoapp.di.repositoryModule
import com.example.todoapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(networkModule, repositoryModule,viewModelModule))
        }
    }
}
