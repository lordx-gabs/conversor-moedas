package com.example.conversormoedas.data.di

import com.example.conversormoedas.data.network.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val API_KEY = "363d0fbf776a47ecb35be6f5"

    @Provides
    @Singleton
    fun provideHttpClient() : HttpClient{
        return  HttpClient {
            expectSuccess = true
            defaultRequest {
                url("https://v6.exchangerate-api.com/v6/$API_KEY/")
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    @Provides
    @Singleton
    fun provideKtorClient(
        client: HttpClient
    ): KtorClient {
        return KtorClient(client)
    }
}