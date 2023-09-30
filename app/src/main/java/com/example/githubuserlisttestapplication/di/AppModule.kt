package com.example.githubuserlisttestapplication.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserDao
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUsersDatabase
import com.example.githubuserlisttestapplication.data.data_source.mediator.UserRemoteMediator
import com.example.githubuserlisttestapplication.data.data_source.remote.GithubApiService
import com.example.githubuserlisttestapplication.data.data_source.remote.GithubApiService.Companion.BASE_URL
import com.example.githubuserlisttestapplication.domain.repository.GithubRepository
import com.example.githubuserlisttestapplication.utils.NetworkStatusLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request().newBuilder()
                .addHeader("Accept", "application/json;charset=UTF-8")
                .addHeader("Request-Type", "Android")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
            val actualRequest = request.build()
            it.proceed(actualRequest)
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(logging: HttpLoggingInterceptor, interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubService(okHttpClient: OkHttpClient): GithubApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GithubApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): GithubUsersDatabase {
        return Room.databaseBuilder(
            context,
            GithubUsersDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGithubUserDao(database: GithubUsersDatabase): GithubUserDao {
        return database.githubUserDao()
    }

    @Provides
    @Singleton
    fun provideUserRemoteMediator(
        gitHubService: GithubApiService,
        userDao: GithubUserDao
    ): UserRemoteMediator {
        return UserRemoteMediator(gitHubService, userDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        gitHubService: GithubApiService,
        userDao: GithubUserDao,
        userRemoteMediator: UserRemoteMediator
    ): GithubRepository {
        return GithubRepository(gitHubService, userDao, userRemoteMediator)
    }
}
