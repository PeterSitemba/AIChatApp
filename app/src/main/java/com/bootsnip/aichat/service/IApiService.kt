package com.bootsnip.aichat.service

import io.ktor.client.statement.HttpResponse

interface IApiService {
    suspend fun getQuotes(): HttpResponse
}