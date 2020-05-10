/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.di.companies

import com.example.application.companies.CompaniesListFragment
import com.example.application.companies.DetailedCompanyInfoFragment
import dagger.Subcomponent

@Subcomponent(modules = [CompaniesModule::class])
interface CompaniesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : CompaniesComponent
    }

    fun inject(companiesListFragment: CompaniesListFragment)
    fun inject(detailedCompanyFragment: DetailedCompanyInfoFragment)
}