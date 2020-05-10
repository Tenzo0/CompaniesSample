/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.companies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.application.domain.CompanyListItem
import com.example.application.domain.DetailedCompanyItem
import com.example.application.repository.CompaniesRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CompaniesViewModel @Inject constructor(
    private val repository: CompaniesRepository
) : ViewModel() {

    val companiesList: LiveData<List<CompanyListItem>> = repository.companies
    val detailedCompanyItem: LiveData<DetailedCompanyItem?> = repository.detailedCompany

    // Flag to show network error
    var isNetworkErrorShown = MutableLiveData<Boolean>()

    private var _isProgressLoadingShown = MutableLiveData<Boolean>()
    val isProgressLoadingShown: LiveData<Boolean>
        get() = _isProgressLoadingShown

    init {
        isNetworkErrorShown.value = false
        _isProgressLoadingShown.value = false
    }

    fun reloadCompanies() {
        viewModelScope.launch {
            _isProgressLoadingShown.value = true

            try {
                repository.reloadCompanies()
                isNetworkErrorShown.value = false
            }
            catch (e: Exception) {
                //Network error
                Timber.e("$e")
                isNetworkErrorShown.value = true
            }

            _isProgressLoadingShown.value = false
        }
    }

    fun getCompanyDetails(companyId: Long) {
        viewModelScope.launch {
            _isProgressLoadingShown.value = true

            try {
                repository.getDetailedCompanyInfo(companyId)
                isNetworkErrorShown.value = false
            }
            catch (e: Exception) {
                //Network error
                Timber.e("$e")
                isNetworkErrorShown.value = true
            }

            _isProgressLoadingShown.value = false
        }
    }

    fun reloadCompanyDetails(companyId: Long) {
        viewModelScope.launch {
            _isProgressLoadingShown.value = true

            try {
                repository.reloadDetailedCompanyInfo(companyId)
                isNetworkErrorShown.value = false
            }
            catch (e: Exception) {
                //Network error
                Timber.e("$e")
                isNetworkErrorShown.value = true
            }

            _isProgressLoadingShown.value = false
        }
    }
}
