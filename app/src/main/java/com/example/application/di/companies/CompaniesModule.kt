/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.di.companies

import androidx.lifecycle.ViewModel
import com.example.application.di.ViewModelKey
import com.example.application.companies.CompaniesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CompaniesModule {


    @Binds
    @IntoMap
    @ViewModelKey(CompaniesViewModel::class)
    fun bindCompaniesViewModel(viewModel: CompaniesViewModel): ViewModel
}