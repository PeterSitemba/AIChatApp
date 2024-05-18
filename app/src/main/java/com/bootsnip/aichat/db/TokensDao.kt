package com.bootsnip.aichat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface TokensDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToken(tokens: Tokens)

    @Transaction
    @Query("SELECT * FROM tokens_table")
    fun getTokens(): Flow<List<Tokens>>

    fun getTokensDistinct() = getTokens().distinctUntilChanged()

    @Update(entity = Tokens::class)
    suspend fun updateTokens(tokensUpdate: TokensUpdate)

}