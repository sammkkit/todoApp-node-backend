package com.example.todoapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.data.api.AuthApiService
import com.example.todoapp.data.api.taskApiService
import com.example.todoapp.data.repository.AppRepository
import com.example.todoapp.presentation.AppViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//val baseUrl = "http://10.0.2.2:8001/"
val baseUrl = "https://todo-node-fjad.vercel.app/"
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider()
        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
val networkModule: Module = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    single {

        val sharedPreferences: SharedPreferences = get()
        val tokenProvider = { sharedPreferences.getString("auth_token", null) }
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider)) // Add the interceptor
            .build()
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single {
        get<Retrofit>().create(taskApiService::class.java)
    }
    single {
        get<Retrofit>().create(AuthApiService::class.java)
    }
}
val repositoryModule: Module = module {
    single { AppRepository(get(),get()) }
}
val viewModelModule: Module = module {
    viewModel { AppViewModel(get(),get()) }
}


