package com.example.githubuserlisttestapplication.data.data_source.remote

import com.example.githubuserlisttestapplication.data.data_source.remote.dto.GithubUserDTO
import com.example.githubuserlisttestapplication.data.data_source.remote.dto.GithubUserProfileResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("users")
    suspend fun getUsers(@Query("since") since: Int): Response<List<GithubUserDTO>>

    @GET("users/{username}")
    suspend fun getUserProfileByUsername(@Path("username") username: String): Response<GithubUserProfileResponseDTO>

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}