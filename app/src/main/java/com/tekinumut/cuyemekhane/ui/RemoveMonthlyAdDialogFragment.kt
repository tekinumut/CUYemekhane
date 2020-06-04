package com.tekinumut.cuyemekhane.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.databinding.DialogRemoveBannerAdBinding
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import com.tekinumut.cuyemekhane.viewmodel.RewardAdDialogViewModel

class RemoveMonthlyAdDialogFragment : DialogFragment() {

    private val rewardVM: RewardAdDialogViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var mView: View

    //private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: DialogRemoveBannerAdBinding
    private lateinit var mainPref: MainPref
    private lateinit var rewardedAd: RewardedAd
    private lateinit var btnAccept: Button
    private lateinit var llAdErrorRefresh: LinearLayout
    private var currentToast: Toast? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            mView = requireActivity().layoutInflater.inflate(R.layout.dialog_remove_banner_ad, null)
            init(it)
            binding.root.findViewById<Button>(R.id.btnRejectRemoveBanner).setOnClickListener { dismiss() }
            btnAccept.setOnClickListener { startRewardAd() }
            llAdErrorRefresh.setOnClickListener { createAndLoadRewardedAd() }
            // Diyalog ilk açıldığında reklamı yüklemeye başla
            createAndLoadRewardedAd()

            AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun init(it: FragmentActivity) {
        @SuppressLint("InflateParams")
        binding = DataBindingUtil.inflate(LayoutInflater.from(it.applicationContext), R.layout.dialog_remove_banner_ad, null, false)
        binding.viewmodel = rewardVM
        binding.lifecycleOwner = it
        mainPref = MainPref.getInstance(it)
        btnAccept = binding.root.findViewById(R.id.btnAcceptRemoveBanner)
        llAdErrorRefresh = binding.root.findViewById(R.id.llAdErrorRefresh)
        //firebaseAnalytics = FirebaseAnalytics.getInstance(it)
    }

    /**
     * Ödüllü reklamı init ediyoruz.
     */
    private fun createAndLoadRewardedAd() {
        rewardVM.updateAdStatus(0)
        rewardedAd = RewardedAd(binding.root.context, getString(R.string.reward_ad_unit_id))
        rewardedAd.loadAd(AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                super.onRewardedAdLoaded()
                rewardVM.updateAdStatus(1)
            }

            override fun onRewardedAdFailedToLoad(p0: Int) {
                super.onRewardedAdFailedToLoad(p0)
                rewardVM.updateAdStatus(2)
            }
        })
    }

    private fun startRewardAd() {
        if (rewardedAd.isLoaded) {

            // onRewardedAdClosed hep çalıştığı için kullanıyorum
            var isEarned = false
            val adCallback = object : RewardedAdCallback() {

                override fun onRewardedAdOpened() {
                    // Ad opened.
                    isEarned = false
                }

                override fun onRewardedAdClosed() {
                    // Ad closed.
                    if (isEarned) {
                        mainPref.save(getString(R.string.isBannerExpire), ConstantsGeneral.nextTwoWeekTimeStamp())
                        mainViewModel.updateIsRewardEearned(true)
                        showCurrentToast(getString(R.string.ad_rewarded), Toast.LENGTH_LONG)
                        dismiss()
                    } else {
                        createAndLoadRewardedAd()
                        showCurrentToast(getString(R.string.ad_closed), Toast.LENGTH_SHORT)
                    }
                }

                override fun onUserEarnedReward(p0: RewardItem) {
                    isEarned = true
//                    val bundle = Bundle()
//                    bundle.putString("is_reward_watched", "reklam izlendi")
//                    firebaseAnalytics.logEvent("reward_earned_count", bundle)
                }

                override fun onRewardedAdFailedToShow(errorCode: Int) {
                    val errorCause = when (errorCode) {
                        ERROR_CODE_INTERNAL_ERROR -> "Teknik bir hata meydana geldi. Lütfen tekrar deneyiniz."
                        ERROR_CODE_AD_REUSED -> "Reklam zaten bir kere gösterildi."
                        ERROR_CODE_NOT_READY -> getString(R.string.ad_failed_load)
                        ERROR_CODE_APP_NOT_FOREGROUND -> "Reklam uygulama ön planda değilken oynatılamaz."
                        else -> "Teknik bir hata meydana geldi. Lütfen tekrar deneyiniz."
                    }
                    showCurrentToast(errorCause, Toast.LENGTH_LONG)
                }
            }
            rewardedAd.show(requireActivity(), adCallback)
        }
    }

    private fun showCurrentToast(text: String, length: Int) {
        currentToast?.cancel()
        currentToast = Toast.makeText(requireActivity(), text, length)
        currentToast?.show()
    }


}