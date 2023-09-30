package com.example.githubuserlisttestapplication.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserEntity
import com.example.githubuserlisttestapplication.data.data_source.remote.dto.GithubUserProfileResponseDTO
import com.example.githubuserlisttestapplication.domain.repository.GithubRepository
import com.example.githubuserlisttestapplication.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: GithubRepository
) : ViewModel() {

    suspend fun getUserProfileByUsername(username: String): ResultWrapper<GithubUserProfileResponseDTO> {
        return userRepository.getUserProfileByUsername(username)
    }
}
