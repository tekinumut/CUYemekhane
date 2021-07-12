package com.tekinumut.cuyemekhane.ui.dialogfragments.removebanner

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.databinding.DialogRemoveBannerAdBinding
import com.tekinumut.cuyemekhane.interfaces.RemoveBannerAdCallBack
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Utility

class RemoveBannerAdDialogFragment : DialogFragment() {

    private val removeBannerVM: RemoveBanneradViewModel by viewModels()
    private lateinit var listener: RemoveBannerAdCallBack

    //private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: DialogRemoveBannerAdBinding
    private lateinit var mainPref: MainPref
    private lateinit var btnAccept: Button
    private var mRewardedAd: RewardedAd? = null
    private lateinit var llAdErrorRefresh: LinearLayout
    private var currentToast: Toast? = null

    // is user watched reward ad successfully
    private var isEarned = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_remove_banner_ad, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_def_dialog)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            init(it)
            binding.root.findViewById<Button>(R.id.btnRejectRemoveBanner).setOnClickListener { dismiss() }
            btnAccept.setOnClickListener { showRewardedAd() }
            llAdErrorRefresh.setOnClickListener { loadRewardedAd() }
            // Diyalog ilk açıldığında reklamı yüklemeye başla
            loadRewardedAd()

            AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun init(it: FragmentActivity) {
        @SuppressLint("InflateParams")
        binding = DataBindingUtil.inflate(LayoutInflater.from(it), R.layout.dialog_remove_banner_ad, null, false)
        binding.viewmodel = removeBannerVM
        binding.lifecycleOwner = it
        mainPref = MainPref.getInstance(it)
        btnAccept = binding.root.findViewById(R.id.btnAcceptRemoveBanner)
        llAdErrorRefresh = binding.root.findViewById(R.id.llAdErrorRefresh)
        //firebaseAnalytics = FirebaseAnalytics.getInstance(it)
    }

    private fun loadRewardedAd() {
        if (mRewardedAd == null) {
            removeBannerVM.updateAdStatus(0)
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                requireContext(),
                getString(R.string.watch_ad_remove_banner_unit_id),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        onAdError()
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        mRewardedAd = rewardedAd
                        removeBannerVM.updateAdStatus(1)
                    }
                }
            )
        }
    }

    private fun showRewardedAd() {
        mRewardedAd?.let {
            it.fullScreenContentCallback = fullScreenContentCallback
            it.show(requireActivity()) {
                isEarned = true
            }
        } ?: kotlin.run {
            onAdError()
        }
    }


    private val fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() = Unit

        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            val errorCause = when (adError?.code) {
                ERROR_CODE_INTERNAL_ERROR -> "Teknik bir hata meydana geldi. Lütfen tekrar deneyiniz."
                ERROR_CODE_AD_REUSED -> "Reklam zaten gösterimde."
                ERROR_CODE_NOT_READY -> getString(R.string.ad_failed_load)
                ERROR_CODE_APP_NOT_FOREGROUND -> "Reklam uygulama ön planda değilken oynatılamaz."
                else -> "Teknik bir hata meydana geldi. Lütfen tekrar deneyiniz."
            }
            onAdError()
            showCurrentToast(errorCause, Toast.LENGTH_LONG)
        }

        override fun onAdDismissedFullScreenContent() {
            // Called when ad is dismissed before watch it.
            if (isEarned) {
                val timeToAdd: Long = Utility.addExtraTimeToCurrent(Utility.DelayTime.RemoveBanner)
                mainPref.save(getString(R.string.isBannerExpire), timeToAdd)
                listener.onAdWatched(true)
                showCurrentToast(getString(R.string.banner_ad_removed), Toast.LENGTH_LONG)
                dismiss()
            } else {
                onAdError()
                showCurrentToast(getString(R.string.ad_closed), Toast.LENGTH_SHORT)
            }

        }
    }

    private fun onAdError() {
        mRewardedAd = null
        removeBannerVM.updateAdStatus(2)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as RemoveBannerAdCallBack
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement RemoveBannerAdCallBack")
        }
    }

    private fun showCurrentToast(text: String, length: Int) {
        currentToast?.cancel()
        currentToast = Toast.makeText(binding.root.context, text, length)
        currentToast?.show()
    }


}