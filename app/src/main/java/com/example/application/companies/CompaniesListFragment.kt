/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.companies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.application.App
import com.example.application.R
import com.example.application.databinding.CompaniesListFragmentBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class CompaniesListFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<CompaniesViewModel> { viewModelFactory }
    private lateinit var binding: CompaniesListFragmentBinding
    private lateinit var newsListAdapter: CompaniesListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent
            .companiesComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.companies_list_fragment, container, false)

        setupList()
        setupViewModel()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.reloadCompanies()
        }

        return binding.root
    }

    private fun setupList() {
        newsListAdapter = CompaniesListAdapter(
            CompaniesListAdapter.CompaniesItemClickListener {
                findNavController().navigate(
                    CompaniesListFragmentDirections
                        .actionCompaniesListFragmentToDetailedCompanyInfoFragment(it.id)
                )
            }
        )

        with(binding.companiesList) {
            adapter = newsListAdapter
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            viewModel.isNetworkErrorShown.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    isNetworkErrorShown.value = false
                    Snackbar.make(binding.root, getString(R.string.network_error), LENGTH_LONG)
                        .setAction(getString(R.string.retry)) { viewModel.reloadCompanies() }
                        .show()
                    binding.networkErrorLayout.visibility =
                        if (companiesList.value.isNullOrEmpty())
                            VISIBLE
                        else
                            INVISIBLE
                }
            })

            viewModel.isProgressLoadingShown.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })

            companiesList.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) binding.networkErrorLayout.visibility = INVISIBLE
                newsListAdapter.submitList(it)
            })

            if (viewModel.companiesList.value!!.isEmpty()) reloadCompanies()
        }
    }
}
