package com.bootsnip.aichat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChatHistory::class], version = 3, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AstraRoomDatabase: RoomDatabase() {
    abstract fun ChatHistoryDao(): ChatHistoryDao
}