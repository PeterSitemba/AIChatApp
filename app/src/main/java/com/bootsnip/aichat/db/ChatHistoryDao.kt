package com.bootsnip.aichat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ChatHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChatHistory(chatHistory: ChatHistory)

    @Transaction
    @Query("SELECT * FROM chat_history_table")
    fun getAllChatHistory(): Flow<List<ChatHistory>>

    fun getAllChatHistoryDistinct() = getAllChatHistory().distinctUntilChanged()

    @Transaction
    @Query("SELECT * FROM chat_history_table WHERE fav = 1")
    fun getAllFavChatHistory(): Flow<List<ChatHistory>>

    fun getAllFavChatHistoryDistinct() = getAllFavChatHistory().distinctUntilChanged()

    @Query("DELETE FROM chat_history_table WHERE uid = :id")
    suspend fun deleteChatHistory(id: Int)
}