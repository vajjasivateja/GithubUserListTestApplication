package com.example.githubuserlisttestapplication.data.data_source.mapper

import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserEntity
import com.example.githubuserlisttestapplication.data.data_source.remote.dto.GithubUserDTO
import com.example.githubuserlisttestapplication.domain.models.GithubUserModel

fun GithubUserDTO.toUserItemEntity(): GithubUserEntity {
    return GithubUserEntity(
        id = id,
        login = login,
        avatarUrl = avatar_url,
        note = note
    )
}

fun GithubUserEntity.toUserItemModel(): GithubUserModel {
    return GithubUserModel(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        note = note
    )
}