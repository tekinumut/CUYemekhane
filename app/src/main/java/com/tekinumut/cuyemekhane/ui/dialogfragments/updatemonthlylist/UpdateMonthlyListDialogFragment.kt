package com.tekinumut.cuyemekhane.ui.dialogfragments.updatemonthlylist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.tekinumut.cuyemekhane.databinding.DialogUpdateMonthlyListBinding
import com.tekinumut.cuyemekhane.interfaces.UpdateMonthlyListCallback
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.specificmodels.MonthlyDialogCallBackModel

class UpdateMonthlyListDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(isRefresh: Boolean) = UpdateMonthlyListDialogFragment().apply {
            arguments = Bundle().apply { putBoolean("isRefresh", isRefresh) }
        }
    }

    private lateinit var listener: UpdateMonthlyListCallback

    private val localViewMoel: UpdateMonthlyListViewModel by viewModels()

    //private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: DialogUpdateMonthlyListBinding
    private lateinit var mainPref: MainPref
    private var mRewardedAd: RewardedAd? = null
    private lateinit var btnAccept: Button
    private lateinit var llAdErrorRefresh: LinearLayout
    private var countDownTimer: CountDownTimer? = null
    private var currentToast: Toast? = null

    private var isRefresh = false
    private var isDlImage = true
    private var isEarned = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_update_monthly_list, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_def_dialog)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            init(it)
            btnAccept.setOnClickListener { showRewardedAd() }
            llAdErrorRefresh.setOnClickListener { loadRewardedAd() }
            // Diyalog ilk açıldığında reklamı yüklemeye başla
            loadRewardedAd()

            binding.root.findViewById<Button>(R.id.btnRejectUpdateMonthly).setOnClickListener { dismiss() }
            AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun init(it: FragmentActivity) {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_update_monthly_list, null, false)
        binding.viewmodel = localViewMoel
        binding.lifecycleOwner = it
        mainPref = MainPref.getInstance(it)
        btnAccept = binding.root.findViewById(R.id.btnAcceptUpdateMonthly)
        llAdErrorRefresh = binding.root.findViewById(R.id.llAdErrorRefreshMonthlyList)
        initCheckBox()
        initRemainingTimeObserve()
        isRefresh = arguments?.getBoolean("isRefresh", false) ?: false
        //firebaseAnalytics = FirebaseAnalytics.getInstance(it)
    }


    /**
     * Listeyi sınırsız yenileme hakkından kalan süreyi izle
     */
    private fun initRemainingTimeObserve() {
        val expireTime: Long = mainPref.getLong(ConstantsGeneral.prefMonthlyCountDown, 0)
        val remainingTime = expireTime - ConstantsGeneral.currentTimeStamp()
        if (remainingTime > 0L) {
            localViewMoel.updateTimeRunningStatus(true)

            countDownTimer = object : CountDownTimer(remainingTime, 1000) {
                override fun onFinish() {
                    localViewMoel.updateTimeRunningStatus(false)
                    loadRewardedAd()
                }

                override fun onTick(millisUntilFinished: Long) {
                    localViewMoel.updateRemainingTimeText(Utility.timeMillisToHourFormat(millisUntilFinished))
                }
            }.start()
        }
    }

    /**
     * Resim indirip indirmeyeceği bilgisini güncelliyoruz
     */
    private fun initCheckBox() {
        val checkBoxDlImage: CheckBox = binding.root.findViewById(R.id.cbDownloadPic)
        checkBoxDlImage.setOnCheckedChangeListener { _, isChecked ->
            isDlImage = isChecked
        }
    }

    private fun loadRewardedAd() {
        if (mRewardedAd == null && localViewMoel.isTimeRunning.value != true) {
            localViewMoel.updateAdStatus(0)
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                requireContext(),
                getString(R.string.adUnitIdMonthlyList),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        // Eğer sunucudan reklam gelmezse adErrorCount'u arttır.
                        if (p0.code == 3) {
                            localViewMoel.incAdErrorCount()
                        }
                        // Birden fazla sunucudan reklam gelmezse
                        // kullanıcı reklamsız listeyi indirebilecek.
                        if (localViewMoel.adErrorCount.value ?: 0 >= 2) {
                            localViewMoel.updateAdStatus(3)
                        } else {
                            onAdError()
                        }
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        mRewardedAd = rewardedAd
                        localViewMoel.updateAdStatus(1)
                    }
                }
            )
        }
    }

    private fun showRewardedAd() {
        if (localViewMoel.isTimeRunning.value == true || localViewMoel.adErrorCount.value ?: 0 >= 2) {
            listener.onAdWatched(MonthlyDialogCallBackModel(isRefresh, isDlImage))
            dismiss()
        } else {
            mRewardedAd?.let {
                it.fullScreenContentCallback = fullScreenContentCallback
                it.show(requireActivity()) {
                    isEarned = true
                }
            } ?: kotlin.run {
                onAdError()
            }
        }
    }

    private val fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() = Unit

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            val errorCause = when (adError.code) {
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
                val timeToAdd = Utility.addExtraTimeToCurrent(Utility.DelayTime.UpdateMonthylList)
                mainPref.save(ConstantsGeneral.prefMonthlyCountDown, timeToAdd)
                listener.onAdWatched(MonthlyDialogCallBackModel(isRefresh, isDlImage))
                dismiss()
            } else {
                onAdError()
                showCurrentToast(getString(R.string.ad_closed), Toast.LENGTH_SHORT)
            }

        }
    }

    private fun onAdError() {
        mRewardedAd = null
        localViewMoel.updateAdStatus(2)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as UpdateMonthlyListCallback
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UpdateMonthlyListListener")
        }
    }

    private fun showCurrentToast(text: String, length: Int) {
        currentToast?.cancel()
        currentToast = Toast.makeText(binding.root.context, text, length)
        currentToast?.show()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

}