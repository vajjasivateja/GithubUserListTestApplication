package com.example.githubuserlisttestapplication.presentation.screens.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserlisttestapplication.utils.NetworkStatusLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkStatusLiveData: NetworkStatusLiveData
) : ViewModel() {

    fun getNetworkStatusLiveData(): LiveData<Boolean> {
        return networkStatusLiveData
    }
}
