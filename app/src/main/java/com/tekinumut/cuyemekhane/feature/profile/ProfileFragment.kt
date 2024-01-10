package com.tekinumut.cuyemekhane.feature.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.orEmptyString
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.ui.CuToolbar
import com.tekinumut.cuyemekhane.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        with(binding) {
            setupToolbar(
                toolbar = toolbar,
                config = CuToolbar.ConfigModel(
                    mainTitle = findNavController().currentDestination?.label.orEmptyString()
                )
            )
            textTabAnnouncements.setOnClickListener {
                navigateToDirections(
                    action = ProfileFragmentDirections.actionProfileMenuFragmentToAnnouncementsFragment()
                )
            }
            textTabPricing.setOnClickListener {
                navigateToDirections(
                    action = ProfileFragmentDirections.actionProfileMenuFragmentToPricingFragment()
                )
            }
        }
    }

    private fun navigateToDirections(action: NavDirections) {
        findNavController().navigate(action)
    }
}