package com.example.githubuserlisttestapplication.domain.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserDao
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserEntity
import com.example.githubuserlisttestapplication.data.data_source.mediator.UserRemoteMediator
import com.example.githubuserlisttestapplication.data.data_source.remote.GithubApiService
import com.example.githubuserlisttestapplication.data.data_source.remote.dto.GithubUserProfileResponseDTO
import com.example.githubuserlisttestapplication.utils.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GithubRepository @Inject constructor(
    private val githubApiService: GithubApiService,
    private val githubUserDao: GithubUserDao,
    private val userRemoteMediator: UserRemoteMediator
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }

    fun getUsers(viewModelScope: CoroutineScope): Flow<PagingData<GithubUserEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = userRemoteMediator,
            pagingSourceFactory = {
                githubUserDao.getUsers()
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getUserById(userId: Int): GithubUserEntity {
        return githubUserDao.getUserById(userId)
    }

    suspend fun updateUserNote(userId: Int, note: String) {
        try {
            val user = githubUserDao.getUserById(userId)
            user.note = note
            githubUserDao.updateUser(user)
            Log.d("GithubRepository", "User note updated successfully")
        } catch (e: Exception) {
            // Handle the error or log it
            Log.e("GithubRepository", "Error updating user note: ${e.message}")
        }
    }

    suspend fun getUserProfileByUsername(username: String): ResultWrapper<GithubUserProfileResponseDTO> {
        return try {
            val response = githubApiService.getUserProfileByUsername(username)
            if (response.isSuccessful) {
                val userProfile = response.body()
                if (userProfile != null) {
                    ResultWrapper.Success(userProfile)
                } else {
                    ResultWrapper.Error("User profile not found")
                }
            } else {
                ResultWrapper.Error("Error fetching user profile")
            }
        } catch (e: Exception) {
            ResultWrapper.Error("Network error: ${e.message}")
        }
    }
}