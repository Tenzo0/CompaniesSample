/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.repository

import android.util.LruCache
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.application.api.CompaniesApi
import com.example.application.domain.CompanyListItem
import com.example.application.domain.DetailedCompanyItem
import com.example.application.vo.asDomainObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CompaniesRepository @Inject constructor(
    private val companiesNetwork: CompaniesApi
) {

    private val _companies = MutableLiveData<List<CompanyListItem>>().apply { this.value = emptyList() }
    val companies: LiveData<List<CompanyListItem>>
        get() = _companies

    private val _detailedCompany = MutableLiveData<DetailedCompanyItem?>()
    val detailedCompany: LiveData<DetailedCompanyItem?>
        get() = _detailedCompany

    private val detailedCompaniesLRUCash = LruCache<Long, DetailedCompanyItem>(16)

    suspend fun reloadCompanies() {
        withContext(Dispatchers.IO) {

            val downloadedList = companiesNetwork
                .getAllCompanies().body()!!.asDomainObject()

            _companies.postValue(downloadedList)
        }
    }

    suspend fun reloadDetailedCompanyInfo(companyId: Long) {
        withContext(Dispatchers.IO) {
            _detailedCompany.postValue(companiesNetwork
                .getCompanyDetails(companyId).body()!!.first().asDomainObject())
        }
    }

    suspend fun getDetailedCompanyInfo(companyId: Long) {
        withContext(Dispatchers.IO) {

            //check for cache
            var detailedCompanyItem = detailedCompaniesLRUCash.get(companyId)
            if (detailedCompanyItem == null) {
                //if this item does not exist then download it and save to cache
                detailedCompanyItem = companiesNetwork
                    .getCompanyDetails(companyId).body()!!.first().asDomainObject()
                detailedCompaniesLRUCash.put(detailedCompanyItem.id, detailedCompanyItem)
            }

            _detailedCompany.postValue(detailedCompanyItem)
        }
    }
}