package com.example.conversormoedas.data.di

import com.example.conversormoedas.data.repository.CurrencyRepositoryImpl
import com.example.conversormoedas.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DateModule {
    @Binds
    @Singleton
    fun bindDateRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository
}