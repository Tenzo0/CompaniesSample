/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.api

import com.example.application.vo.CompanyInfo
import com.example.application.vo.DetailedCompanyInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val COMPANIES_ENDPOINT = "http://megakohz.bget.ru/test_task/"

interface CompaniesApi {

    @GET("test.php")
    suspend fun getAllCompanies(): Response<List<CompanyInfo>>

    @GET("test.php")
    suspend fun getCompanyDetails(
        @Query("id") companyId: Long
    ): Response<List<DetailedCompanyInfo>>
}