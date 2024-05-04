package com.bootsnip.aichat.db

import androidx.room.TypeConverter
import com.bootsnip.aichat.model.AstraChatMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    @TypeConverter
    fun toChatMessageList(value: String?): List<AstraChatMessage>? {
        val type = object : TypeToken<List<AstraChatMessage>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toJsonChatMessageList(value: List<AstraChatMessage>?): String {
        val type = object : TypeToken<List<AstraChatMessage>>() {}.type
        return Gson().toJson(value, type)
    }

}