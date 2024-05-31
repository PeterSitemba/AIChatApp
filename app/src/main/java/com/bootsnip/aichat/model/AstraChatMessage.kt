package com.bootsnip.aichat.model

import com.aallam.openai.api.core.Role
import kotlinx.serialization.Serializable

@Serializable
data class AstraChatMessage(
    val role: Role,
    val content: String?,
    val isImagePrompt: Boolean,
)
