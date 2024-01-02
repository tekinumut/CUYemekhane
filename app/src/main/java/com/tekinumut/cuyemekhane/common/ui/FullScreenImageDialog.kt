package com.tekinumut.cuyemekhane.common.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseDialogFragment
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.databinding.DialogFullScreeImageBinding

class FullScreenImageDialog : BaseDialogFragment<DialogFullScreeImageBinding>(
    DialogFullScreeImageBinding::inflate
) {
    private val navArgs: FullScreenImageDialogArgs by navArgs()
    private val imageUrl by lazy { navArgs.imageUrl }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initArgs() {
        if (imageUrl.isEmpty()) {
            dismiss()
        }
    }

    private fun initUI() {
        with(binding) {
            imageFullScreen.setImageUrl(imageUrl)
            imageCloseDialog.setOnClickListener { dismiss() }
        }
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }
}