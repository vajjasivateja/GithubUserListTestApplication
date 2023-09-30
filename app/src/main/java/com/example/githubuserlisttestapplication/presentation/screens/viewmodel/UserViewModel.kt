package com.example.githubuserlisttestapplication.presentation.screens.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserEntity
import com.example.githubuserlisttestapplication.data.data_source.mapper.toUserItemModel
import com.example.githubuserlisttestapplication.domain.models.GithubUserModel
import com.example.githubuserlisttestapplication.domain.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: GithubRepository) : ViewModel() {
//    var query by mutableStateOf("")

    private val usersFlow = repository.getUsers(viewModelScope)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val userFeedPagingFlow: Flow<PagingData<GithubUserModel>> = combine(
        query,
        repository.getUsers(viewModelScope)
    ) { query, pagingData ->
        if (query.isNotEmpty()) {
            pagingData.filter { user ->
                user.login.contains(query, ignoreCase = true)
            }
        } else {
            pagingData
        }
    }.map { pagingData ->
        pagingData.map { it.toUserItemModel() }
    }.cachedIn(viewModelScope)


    fun setSearchQuery(newQuery: String) {
        _query.value = newQuery
    }

    private val _user = mutableStateOf<GithubUserEntity?>(null)
    val user: State<GithubUserEntity?> = _user

    fun getUserById(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) { // Use IO
            try {
                val fetchedUser = repository.getUserById(userId)
                _user.value = fetchedUser
            } catch (e: Exception) {
                // Handle errors more gracefully (e.g., log or display an error message)
                // You can also provide a default value or handle different types of exceptions
                e.printStackTrace() // Log the exception for debugging
                _user.value = null
            }
        }
    }


    suspend fun updateNote(userId: Int, note: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateUserNote(userId, note)
                Log.d("UserViewModel", "Note updated successfully")
            } catch (e: Exception) {
                // Handle the error or log it
                Log.e("UserViewModel", "Error updating note: ${e.message}")
            }
        }
    }
}
