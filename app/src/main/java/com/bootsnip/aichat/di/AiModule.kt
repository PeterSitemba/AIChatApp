package com.bootsnip.aichat.di

import com.bootsnip.aichat.repo.AiRepo
import com.bootsnip.aichat.repo.IAiRepo
import com.bootsnip.aichat.service.ApiService
import com.bootsnip.aichat.service.IApiService
import com.bootsnip.aichat.service.httpClientAndroid
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
interface AiModule {

    @Binds
    fun bindIApiService(apiService: ApiService): IApiService

    @Binds
    fun bindIAiRepo(aiRepo: AiRepo): IAiRepo

    companion object {
        @Provides
        fun provideHttpClient(): HttpClient {
            return httpClientAndroid
        }

        @Provides
        fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }

}