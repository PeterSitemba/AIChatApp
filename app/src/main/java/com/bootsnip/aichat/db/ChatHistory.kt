package com.bootsnip.aichat.db

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bootsnip.aichat.model.AstraChatMessage
import java.util.UUID

@Entity(tableName = "chat_history_table")
@Keep
data class ChatHistory(
    @PrimaryKey(true)
    val uid: Int? = null,
    @ColumnInfo("assistant_type")
    val assistantType: String,
    @TypeConverters(Converter::class)
    @ColumnInfo("chat_message_list")
    val chatMessageList: List<AstraChatMessage>?,
    val fav : Int,
    @ColumnInfo("cloud_sync_id")
    val cloudSyncId: String = UUID.randomUUID().toString(),
    @ColumnInfo("modified_at")
    val modifiedAt: Long
)

@Keep
data class ChatHistoryUpdateFav(
    val uid: Int,
    val fav : Int
)

@Keep
data class ChatHistoryUpdate(
    val uid: Int?,
    @TypeConverters(Converter::class)
    @ColumnInfo("chat_message_list")
    val chatMessageList: List<AstraChatMessage>?,
    @ColumnInfo("modified_at")
    val modifiedAt: Long
)
