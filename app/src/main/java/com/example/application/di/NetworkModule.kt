/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.di

import com.example.application.api.COMPANIES_ENDPOINT
import com.example.application.api.CompaniesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule
{
    @Provides
    @Singleton
    fun getCompaniesApi(retrofitService: Retrofit) : CompaniesApi  =
        retrofitService.create(CompaniesApi::class.java)

    @Provides
    @Singleton
    fun getMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun getRetrofitService(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(COMPANIES_ENDPOINT)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}