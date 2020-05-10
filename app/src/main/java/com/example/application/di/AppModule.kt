/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.di

import com.example.application.api.CompaniesApi
import com.example.application.repository.CompaniesRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideNewsRepository(newsNetwork: CompaniesApi): CompaniesRepository = CompaniesRepository(newsNetwork)

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}