package com.bootsnip.aichat.repo

import com.bootsnip.aichat.model.QuotesResults

interface IAiRepo {
    suspend fun getQuotes() : Result<QuotesResults>
}