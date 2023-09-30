package com.example.githubuserlisttestapplication.data.data_source.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserDao
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserEntity
import com.example.githubuserlisttestapplication.data.data_source.mapper.toUserItemEntity
import com.example.githubuserlisttestapplication.data.data_source.remote.GithubApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val gitHubService: GithubApiService,
    private val userDao: GithubUserDao
) : RemoteMediator<Int, GithubUserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubUserEntity>
    ): MediatorResult {
        return try {
            val since = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastUser = state.lastItemOrNull()
                    lastUser?.id ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = gitHubService.getUsers(since = since ?: 0)
            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                // Clear the table if it's a refresh, otherwise, insert new items
                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                }
                val items = users.map { it.toUserItemEntity() }
                userDao.insertUsers(items)
                MediatorResult.Success(endOfPaginationReached = users.isEmpty())
            } else {
                MediatorResult.Error(HttpException(response))
            }
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}