package com.bootsnip.aichat.repo

import com.bootsnip.aichat.model.QuotesResults
import com.bootsnip.aichat.service.IApiService
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class AiRepo @Inject constructor(
    private val service: IApiService,
    private val ioDispatcher: CoroutineDispatcher
): IAiRepo {
    override suspend fun getQuotes(): Result<QuotesResults> =
        withContext(ioDispatcher) {
            try {
                Result.success(service.getQuotes().body())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

}