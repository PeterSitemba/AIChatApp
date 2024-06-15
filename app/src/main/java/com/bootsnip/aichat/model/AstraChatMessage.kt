package com.bootsnip.aichat.model

import androidx.annotation.Keep
import com.aallam.openai.api.core.Role
import kotlinx.serialization.Serializable

@Serializable
@Keep data class AstraChatMessage(
    val role: Role,
    val content: String?,
    val isImagePrompt: Boolean,
)
