package com.example.githubuserlisttestapplication.domain.models

data class GithubUserModel(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val note: String?
)