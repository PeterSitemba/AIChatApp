package com.bootsnip.aichat.util

import java.text.DateFormat.getDateInstance
import java.util.Date

object DateUtil {
    fun currentDate(): String {
        val date = getDateInstance()
        return date.format(Date())
    }

    fun formatDate(date: Date): String {
        val dateFormat = getDateInstance()
        return dateFormat.format(date)
    }
}