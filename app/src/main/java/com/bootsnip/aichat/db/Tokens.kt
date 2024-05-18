package com.bootsnip.aichat.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tokens_table")
data class Tokens(
    @PrimaryKey(true)
    val uid: Int = 1,
    @ColumnInfo("remaining_count")
    val remainingCount: Int,
    @ColumnInfo("unlimited")
    val unlimited: Boolean,
    @ColumnInfo("date")
    val date: String
)

data class TokensUpdate(
    val uid: Int,
    @ColumnInfo("remaining_count")
    val remainingCount: Int,
    @ColumnInfo("unlimited")
    val unlimited: Boolean,
    @ColumnInfo("date")
    val date: String
)