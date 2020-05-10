/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.vo

import com.example.application.domain.CompanyListItem


data class CompanyInfo(
    val id: Long,
    val name: String,
    val img: String
)

fun CompanyInfo.asDomainObject(): CompanyListItem = CompanyListItem(
    id, name, img
)

fun List<CompanyInfo>.asDomainObject(): List<CompanyListItem> = map { it.asDomainObject() }