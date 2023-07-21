package com.tekinumut.cuyemekhane.common.ui

import android.os.Bundle
import android.view.View
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseDialogFragment
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.common.util.Constants
import com.tekinumut.cuyemekhane.databinding.DialogFullScreeImageBinding

class FullScreenImageDialog : BaseDialogFragment<DialogFullScreeImageBinding>(
    DialogFullScreeImageBinding::inflate
) {
    private var imageData: String = Constants.STRING_EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initArgs() {
        imageData = arguments?.getString(ARG_KEY_IMAGE_DATA).orEmpty()
        if (imageData.isEmpty()) {
            dismiss()
        }
    }

    private fun initUI() {
        with(binding) {
            imageFullScreen.setImageUrl(imageData)
            imageCloseDialog.setOnClickListener { dismiss() }
        }
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    companion object {
        const val TAG = "fullScreenImageDialogTag"
        const val ARG_KEY_IMAGE_DATA = "image_data"

        fun newInstance(
            imageData: String
        ) = FullScreenImageDialog().apply {
            arguments = Bundle().apply {
                putString(ARG_KEY_IMAGE_DATA, imageData)
            }
        }
    }
}