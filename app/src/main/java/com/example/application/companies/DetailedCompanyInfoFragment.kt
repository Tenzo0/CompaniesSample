/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.companies

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.application.App
import com.example.application.R
import com.example.application.api.COMPANIES_ENDPOINT
import com.example.application.databinding.DetailedCompanyInfoFragmentBinding
import com.example.application.domain.DetailedCompanyItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class DetailedCompanyInfoFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<CompaniesViewModel> { viewModelFactory }
    private val safeArgs: DetailedCompanyInfoFragmentArgs by navArgs()
    private lateinit var binding: DetailedCompanyInfoFragmentBinding
    private lateinit var googleMapFragment: SupportMapFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as App).appComponent
            .companiesComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<DetailedCompanyInfoFragmentBinding>(inflater,
            R.layout.detailed_company_info_fragment,container, false)
                .apply {
                    swipeRefreshLayout.setOnRefreshListener {
                        reloadDetails()
                    }
                }

        googleMapFragment = childFragmentManager.findFragmentById(R.id.detailedCompanyMap) as SupportMapFragment
        childFragmentManager.beginTransaction().hide(googleMapFragment).commit()

        setupViewModel()

        requireActivity().actionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    private fun setupIfNotBlank(view: TextView, item: String): Boolean {
        val isItemBlank = item.isBlank()
        if (isItemBlank) {
            view.visibility = GONE
        } else {
            view.visibility = VISIBLE
            view.text = item
        }
        return isItemBlank
    }

    private fun setupViewModel() {
        with(viewModel) {
            getCompanyDetails(safeArgs.companyId)

            detailedCompanyItem.observe(viewLifecycleOwner, Observer {
                if (it != null && it.id == safeArgs.companyId) {
                    setViewValues(it)
                    binding.networkErrorLayout.visibility = INVISIBLE
                }
            })

            viewModel.isNetworkErrorShown.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    viewModel.isNetworkErrorShown.value = false
                    Snackbar.make(
                        binding.root, getString(R.string.network_error),
                        BaseTransientBottomBar.LENGTH_LONG
                    )
                        .setAction(getString(R.string.retry)) { reloadDetails() }
                        .show()
                    binding.networkErrorLayout.visibility =
                        if (viewModel.detailedCompanyItem.value != null
                            && viewModel.detailedCompanyItem.value!!.id == safeArgs.companyId)
                            INVISIBLE
                        else VISIBLE
                }
            })

            viewModel.isProgressLoadingShown.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
        }
    }

    private fun setViewValues(item: DetailedCompanyItem) {
        with(binding) {

            if (item.img.isBlank()) {
                binding.imageView.visibility = GONE
            }
            else {
                binding.imageView.visibility = VISIBLE
                Glide.with(binding.root)
                    .load("${COMPANIES_ENDPOINT}${item.img}")
                    .centerCrop()
                    .error(R.color.lightGray)
                    .into(imageView)
            }

            setupIfNotBlank(title, item.name)
            setupIfNotBlank(description, item.description)
            setupIfNotBlank(sourceLink, item.www)
            val isPhoneBlank = setupIfNotBlank(phone, item.phone)
            phoneText.visibility = if (isPhoneBlank) GONE else VISIBLE

            val isMapAvailable = item.lat != 0.0 && item.lon != 0.0
            if (isMapAvailable) {
                childFragmentManager.beginTransaction().show(googleMapFragment).commit()
                googleMapFragment.getMapAsync { googleMap ->
                    val position = LatLng(item.lat, item.lon)
                    googleMap.addMarker(MarkerOptions().position(position).title(item.name))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
                }
            } else
                childFragmentManager.beginTransaction().hide(googleMapFragment).commit()

            with(binding) {
                if (!isMapAvailable && item.phone.isBlank() && item.www.isBlank()) {
                    contactsText.visibility = GONE
                    delim.visibility = GONE
                } else {
                    contactsText.visibility = VISIBLE
                    delim.visibility = VISIBLE
                }
            }
        }
    }

    private fun reloadDetails() {
        viewModel.reloadCompanyDetails(safeArgs.companyId)
        binding.networkErrorLayout.visibility = INVISIBLE
    }
}
