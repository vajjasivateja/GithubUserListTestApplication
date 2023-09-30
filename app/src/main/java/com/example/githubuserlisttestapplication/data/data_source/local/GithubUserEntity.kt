package com.example.githubuserlisttestapplication.data.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_users")
data class GithubUserEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatarUrl: String,
    var note: String?
)