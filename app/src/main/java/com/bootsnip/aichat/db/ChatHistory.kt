package com.bootsnip.aichat.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bootsnip.aichat.model.AstraChatMessage

@Entity(tableName = "chat_history_table")
data class ChatHistory(
    @PrimaryKey(true)
    val uid: Int? = null,
    @ColumnInfo("assistant_type")
    val assistantType: String,
    @TypeConverters(Converter::class)
    @ColumnInfo("chat_message_list")
    val chatMessageList: List<AstraChatMessage>?,
    val fav : Int
)

data class ChatHistoryUpdateFav(
    val uid: Int,
    val fav : Int
)
