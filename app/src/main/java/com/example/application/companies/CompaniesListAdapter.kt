/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.companies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.R
import com.example.application.api.COMPANIES_ENDPOINT
import com.example.application.databinding.CompaniesListItemBinding
import com.example.application.domain.CompanyListItem


class CompaniesListAdapter (
    private val clickListener: CompaniesItemClickListener
)
    : ListAdapter<CompanyListItem, CompaniesListAdapter.ViewHolder>(CompaniesDiffCallback()
) {

    inner class ViewHolder(private val binding: CompaniesListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CompanyListItem) {
            with(binding) {
                company = item
                companiesItemClickListener = clickListener

                if (item.img.isNotBlank()) {
                    Glide.with(binding.root)
                        .load("${COMPANIES_ENDPOINT}${item.img}")
                        .centerCrop()
                        .into(image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context)
                .inflate(R.layout.companies_list_item, parent, false)
        return createViewHolderFrom(parent)
    }

    private fun createViewHolderFrom(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CompaniesListItemBinding.inflate(
            layoutInflater, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CompaniesItemClickListener(val clickListener: (company : CompanyListItem) -> Unit) {
        fun onClick(company: CompanyListItem) = clickListener(company)
    }

    class CompaniesDiffCallback: DiffUtil.ItemCallback<CompanyListItem>() {
        override fun areItemsTheSame(oldItem: CompanyListItem, newItem: CompanyListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CompanyListItem, newItem: CompanyListItem): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.img == oldItem.img
        }
    }
}