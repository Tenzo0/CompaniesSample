/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.vo

import com.example.application.domain.DetailedCompanyItem

data class DetailedCompanyInfo (
    val id: Long,
    val name: String?,
    val img: String?,
    val description: String?,
    val lat: Double?,
    val lon: Double?,
    val www: String?,
    val phone: String?
)

fun DetailedCompanyInfo.asDomainObject(): DetailedCompanyItem = DetailedCompanyItem(
    id, name?:"", img?:"", description?:"", lat?:0.0, lon?:0.0, www?:"", phone?:""
)