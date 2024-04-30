package com.bootsnip.aichat.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class ApiService @Inject constructor(
    private val client: HttpClient
): IApiService {

    companion object {
        private const val END_POINT = "https://api.quotable.io/"
        private const val QUOTES = "quotes"
    }

    override suspend fun getQuotes(): HttpResponse =
        client.get("$END_POINT$QUOTES")
}