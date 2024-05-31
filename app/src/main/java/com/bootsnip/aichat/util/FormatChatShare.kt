package com.bootsnip.aichat.util

import com.aallam.openai.api.core.Role
import com.bootsnip.aichat.model.AstraChatMessage

class FormatChatShare(private val gptList: List<AstraChatMessage>) {
    fun chatToShare(): String {
        return buildString {
            gptList.forEach { chatMessage ->
                val role = if (chatMessage.role == Role.User) {
                    "You"
                } else {
                    "Astra AI"
                }

                append("${role}: \n")
                append("${chatMessage.content} \n")
                append("\n")
            }
        }
    }
}