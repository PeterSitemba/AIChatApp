package com.bootsnip.aichat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ChatHistory::class, Tokens::class], version = 7, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AstraRoomDatabase: RoomDatabase() {
    abstract fun ChatHistoryDao(): ChatHistoryDao

    abstract fun TokensDao(): TokensDao

}