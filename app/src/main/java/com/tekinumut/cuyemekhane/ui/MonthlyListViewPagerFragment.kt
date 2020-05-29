package com.tekinumut.cuyemekhane.ui

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.DateWithFoodDetailComp
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_monthly_list.*

class MonthlyListViewPagerFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }
    private var rewardAdInfoDialog: Dialog? = null
    private var currentToast: Toast? = null
    private var isFabOpen = false

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_monthly_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Değişkenleri yükle
        init()
        // Veritabanını izle
        getMonthlyListDataAndLoadViewPager()
    }

    private fun init() {
        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)
        viewPager2Monthly.offscreenPageLimit = 1
        btn_update_monthly_list.setOnClickListener(listener)
        fabMain.setOnClickListener(listener)
        fabRemove.setOnClickListener(listener)
        fabRefresh.setOnClickListener(listener)

    }


    /**
     * Aylık liste veritabanını izler.
     * Listenin null ve boş olma durumuna göre viewSwitcher ayarlanır.
     */
    private fun getMonthlyListDataAndLoadViewPager() {
        mainViewModel.getMonthlyList.observe(requireActivity(), Observer {
            if (!it.isNullOrEmpty()) {
                viewPager2Monthly.adapter = SectionPagerAdapter(this, it)
                TabLayoutMediator(tab_layout_monthly, viewPager2Monthly) { tab, position ->
                    val model = it[position].foodDate
                    tab.text = model.name
                }.attach()
                switchView(true)
            } else {
                switchView(false)
            }
        })
    }

    private fun switchView(isMonthly: Boolean) {
        if (isMonthly) {
            clMonthlyList.visibility = View.VISIBLE
            clMonthlyInfo.visibility = View.GONE
        } else {
            clMonthlyList.visibility = View.GONE
            clMonthlyInfo.visibility = View.VISIBLE
        }
    }

    /**
     * Sunucuya bağlanarak güncel aylık listeyi indirir.
     */
    private fun getMonthlyListData(isRefresh: Boolean) {
        mainViewModel.getFoodData(ConstantsGeneral.dbNameMonthly).observe(requireActivity(), Observer {
            when (it) {
                Resource.InProgress -> loadingDialog.show()
                is Resource.Success -> onDateResult(getString(R.string.data_loaded), isRefresh)
                is Resource.Error -> {
                    val message = "${getString(R.string.error_loading_data)} ${getString(R.string.error_cause, it.exception.cause)}"
                    onDateResult(message, isRefresh)
                }
            }
        })
    }

    /**
     * Sunucudan veriler alındıktan sonra yapılacak işlemler
     * Sucess-Error durumu
     */
    private fun onDateResult(message: String, isRefresh: Boolean) {
        showCurrentToast(message, false)
        loadingDialog.dismiss()
        if (isRefresh) {
            closeFab()
        } else {
            rewardAdInfoDialog?.dismiss()
        }
    }

    /**
     * Button click listener tanımlaması
     */
    private val listener = View.OnClickListener { view: View ->
        when (view) {
            btn_update_monthly_list -> loadAndShowDialog()
            fabMain -> animateFab()
            fabRemove -> {
                // Seçile güne ait menüyü silinmesi hakkında bilgi içeren diyaloğun tanımı
                val removeDialog = Utility.getRemoveDayDialog(requireActivity())
                removeDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.remove)) { dialog, _ ->
                    // Seçile güne ait tarih bilgisi
                    val currentTabText = tab_layout_monthly.getTabAt(viewPager2Monthly.currentItem)?.text
                    dialog.dismiss()
                    currentTabText?.let {
                        // getMonthlyList izleyicisini tetikler.
                        mainViewModel.removeSelectedDay(it.toString())
                        showCurrentToast(getString(R.string.food_removed_of_date, currentTabText), true)
                    } ?: run {
                        showCurrentToast(getString(R.string.date_not_selected), false)
                    }
                    animateFab()
                }
                // Diyaloğu göster
                removeDialog.show()
            }
            fabRefresh -> {
                getMonthlyListData(true)
            }
            else -> {
            }
        }
    }

    private fun loadAndShowDialog() {
        rewardAdInfoDialog = Dialog(requireActivity()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_watch_reward_ad)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btnAccept = findViewById<Button>(R.id.btnAccept)
            val btnReject = findViewById<Button>(R.id.btnReject)

            btnReject.setOnClickListener { dismiss() }

            btnAccept.setOnClickListener { getMonthlyListData(false) }
            show()
        }
    }

    // An equivalent ViewPager2 adapter class
    class SectionPagerAdapter(
        fragment: Fragment,
        private val dateWithAll: List<DateWithFoodDetailComp>) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = dateWithAll.size

        // position 0-1 olarak gelecek
        // eğer ilk personel göster deniyorsa 1-0 olarak veri gönderilecek.
        override fun createFragment(position: Int): Fragment {
            return MonthlyListFragment.newInstance(dateWithAll[position].foodDate.name)
        }
    }


    /**
     * Fab açılıyor.
     */
    private fun openFab() {
        fabMain.startAnimation(rotateForward)
        fabRefresh.startAnimation(fabOpen)
        fabRemove.startAnimation(fabOpen)
        fabRefresh.isClickable = true
        fabRemove.isClickable = true
        isFabOpen = true
    }

    /**
     * Fab kapatılıyor.
     */
    private fun closeFab() {
        fabMain.startAnimation(rotateBackward)
        fabRefresh.startAnimation(fabClose)
        fabRemove.startAnimation(fabClose)
        fabRefresh.isClickable = false
        fabRemove.isClickable = false
        isFabOpen = false
    }

    private fun animateFab() {
        if (!isFabOpen) openFab() else closeFab()
    }


    private fun showCurrentToast(text: String, isLong: Boolean) {
        currentToast?.cancel()
        currentToast = Toast.makeText(requireActivity(), text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    override fun onPause() {
        super.onPause()
        rewardAdInfoDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }
}