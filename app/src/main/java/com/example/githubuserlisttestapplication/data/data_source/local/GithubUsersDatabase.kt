package com.example.githubuserlisttestapplication.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GithubUserEntity::class],
    version = 1
)
abstract class GithubUsersDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao

}