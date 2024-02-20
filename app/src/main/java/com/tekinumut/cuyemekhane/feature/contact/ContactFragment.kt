package com.tekinumut.cuyemekhane.feature.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.tekinumut.cuyemekhane.base.BaseFragment
import com.tekinumut.cuyemekhane.common.extensions.setupToolbar
import com.tekinumut.cuyemekhane.common.ui.CuToolbar
import com.tekinumut.cuyemekhane.databinding.FragmentContactBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactFragment : BaseFragment<FragmentContactBinding>(FragmentContactBinding::inflate) {

    private val mapUrl = "https://maps.app.goo.gl/MLG85Dk7sE54JY2Y9"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        with(binding) {
            setupToolbar(
                toolbar = toolbar,
                config = CuToolbar.ConfigModel(
                    displayNavIcon = true,
                    navIconListener = { findNavController().popBackStack() }
                )
            )
            buttonCreateNavigation.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl)))
            }
        }
    }
}