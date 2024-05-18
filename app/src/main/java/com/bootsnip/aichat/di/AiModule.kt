package com.bootsnip.aichat.di

import android.content.Context
import androidx.room.Room
import com.bootsnip.aichat.db.AstraRoomDatabase
import com.bootsnip.aichat.repo.AiRepo
import com.bootsnip.aichat.repo.IAiRepo
import com.bootsnip.aichat.service.ApiService
import com.bootsnip.aichat.service.IApiService
import com.bootsnip.aichat.service.httpClientAndroid
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

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

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext appContext: Context): AstraRoomDatabase =
            Room.databaseBuilder(
                appContext,
                AstraRoomDatabase::class.java,
                "astra_db"
            ).fallbackToDestructiveMigration().build()

        @Singleton
        @Provides
        fun provideChatHistoryDao(db: AstraRoomDatabase) = db.ChatHistoryDao()

        @Singleton
        @Provides
        fun provideTokensDao(db: AstraRoomDatabase) = db.TokensDao()
    }

}