package com.example.githubuserlisttestapplication.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.githubuserlisttestapplication.presentation.screens.viewmodel.NetworkViewModel
import com.example.githubuserlisttestapplication.utils.NetworkStatusLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideNetworkStatusLiveData(
        @ApplicationContext context: Context,
        connectivityManager: ConnectivityManager
    ): NetworkStatusLiveData {
        return NetworkStatusLiveData(connectivityManager)
    }

    @Provides
    @Singleton
    fun provideNetworkViewModel(networkStatusLiveData: NetworkStatusLiveData): NetworkViewModel {
        return NetworkViewModel(networkStatusLiveData)
    }
}
