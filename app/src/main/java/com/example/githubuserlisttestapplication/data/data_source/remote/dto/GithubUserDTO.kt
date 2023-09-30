package com.example.githubuserlisttestapplication.data.data_source.remote.dto


data class GithubUserDTO(
    val id: Int,
    val avatar_url: String,
    val login: String,
    val note: String? // Add a note field
)