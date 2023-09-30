package com.example.githubuserlisttestapplication.data.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubuserlisttestapplication.data.data_source.local.GithubUserEntity

@Dao
interface GithubUserDao {

    //get all users list from database
    @Query("SELECT * FROM github_users")
    fun getUsers(): PagingSource<Int, GithubUserEntity>

    //search user by name
    @Query("SELECT * FROM github_users WHERE login LIKE '%' || :query || '%'")
    fun searchUsers(query: String): PagingSource<Int, GithubUserEntity>

    //insert all users into database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<GithubUserEntity>)

    @Query("SELECT * FROM github_users WHERE id = :userId")
    fun getUserById(userId: Int): GithubUserEntity

    @Query("DELETE FROM github_users")
    suspend fun clearAll()

    @Update
    suspend fun updateUser(user: GithubUserEntity)


    @Query("UPDATE github_users SET note = :note WHERE id = :userId")
    suspend fun updateUserNoteById(userId: Int, note: String)

}