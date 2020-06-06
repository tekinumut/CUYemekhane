package com.tekinumut.cuyemekhane.ui.draweritems.monthlylist

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.DailyMonthlyListAdapter
import com.tekinumut.cuyemekhane.databinding.FragmentMonthlyListBinding
import com.tekinumut.cuyemekhane.interfaces.UpdateMonthlyListCallback
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.SafeClickListener
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.specificmodels.MonthlyDialogCallBackModel
import com.tekinumut.cuyemekhane.ui.dialogfragments.updatemonthlylist.UpdateMonthlyListDialogFragment
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_monthly_list.*

class MonthlyListFragment : Fragment(), UpdateMonthlyListCallback {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val monthlyListViewModel: MonthlyListViewModel by viewModels()
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }
    private lateinit var binding: FragmentMonthlyListBinding
    private var datePickerDialog: DatePickerDialog? = null
    private var currentToast: Toast? = null

    // Sağ alt köşedeki floatActionButton açık olma durumu
    private var isFabOpen = false

    // Seçili menü tarihi
    private var selectedDay: String? = null

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_monthly_list, container, false)
        binding.monthlyVM = monthlyListViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }


    private fun init() {
        recyclerMonthly.layoutManager = LinearLayoutManager(context)
        recyclerMonthly.setHasFixedSize(true)

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        btn_update_monthly_list.setOnSafeClickListener { loadAndShowListUpdateDialog(false) }
        fabMain.setOnClickListener { animateFab() }
        fabRemove.setOnSafeClickListener { onRemoveFoodClick() }
        fabRefresh.setOnSafeClickListener { loadAndShowListUpdateDialog(true) }
        fabDate.setOnClickListener { onDatePickerClick() }

        getSelectedDayOfMonth()

        initCalendarAndObserveMonthlyList()
    }


    /**
     * Takvimi tanımla ve aylık listede var olan değişimleri takip et
     */
    private fun initCalendarAndObserveMonthlyList() {
        monthlyListViewModel.getDaysOfMonth.observe(viewLifecycleOwner, Observer { foodList ->
            // Takvimde gösterilecek günler
            val avaibleDates = Utility.getCalendarArray(foodList.map { it.name })
            // Güncel olarak seçilen gün
            val selectedDay = foodList.filter { it.selectedDay == 1 }.map { it.name }
            val selectedDayArray = Utility.getCalendarArray(selectedDay)
            // Eğer güncel olarak seçilmiş bir gün varsa onu takvimde seç
            // yoksa seçili günler arasındaki ilk tarihi seç
            if (avaibleDates.isNotEmpty()) {
                val selectedCalendar = if (!selectedDayArray.isNullOrEmpty()) selectedDayArray.first() else avaibleDates.first()
                datePickerDialog = DatePickerDialog.newInstance(
                    { _, year, monthOfYear, dayOfMonth ->
                        // Ex: 11.06.2020
                        val formattedDate = Utility.getCalendarSDF(dayOfMonth, monthOfYear, year)
                        monthlyListViewModel.updateSelectedDay(formattedDate)
                    }, selectedCalendar
                ) // Takvimde seçili olan günü belirle
                // Takvimde gösterilecek günleri ayarla
                datePickerDialog?.isThemeDark = Utility.isDarkThemeSelected(requireContext())
                datePickerDialog?.selectableDays = avaibleDates
                datePickerDialog?.autoDismiss(true)
                monthlyListViewModel.updateIsInfo(false)
            } else {
                // Eğer listede hiç gösterilecek gün yok ise
                datePickerDialog = null
                monthlyListViewModel.updateIsInfo(true)

            }
        })
    }

    /**
     * Aylık listeden seçilmiş olan güne ait verileri takip et
     * RecyclerView verisi burada güncellenir
     */
    private fun getSelectedDayOfMonth() {
        monthlyListViewModel.getSelectedDayOfMonth().observe(viewLifecycleOwner, Observer { allList ->
            // Seçilen gün verisini actionBar'a yükle
            // Başlangıçta hiçbir gün seçili olmayabilir
            allList?.foodDate?.name?.let { mainViewModel.updateActionTitle(it) }

            // Eğer seçilen güne ait yemek bilgisi var ise
            allList?.yemekWithComponentComp?.let {
                recyclerMonthly.adapter = DailyMonthlyListAdapter(it, ConstantsGeneral.monthlyFragmentKey)
                monthlyListViewModel.updateIsListEmpty(false)
                selectedDay = allList.foodDate.name
            } ?: run {
                recyclerMonthly.adapter = null
                mainViewModel.updateActionTitle(getString(R.string.monthly_action_title))
                monthlyListViewModel.updateIsListEmpty(true)
                selectedDay = null
            }

        })
    }

    private fun onDatePickerClick() {
        // Takvim zaten eklenmişse birdaha ekleme
        if (datePickerDialog?.isAdded != true) {
            if (datePickerDialog != null) {
                datePickerDialog?.show(parentFragmentManager, "def tag")
            } else {
                openFab()
                val rotateClockwise = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)
                fabRefresh.startAnimation(rotateClockwise)
                showCurrentToast(getString(R.string.no_monthly_list_pls_refresh), true)
            }
        } else {
            showCurrentToast(getString(R.string.date_picker_already_added), true)
        }
    }

    private fun onRemoveFoodClick() {
        // Eğer seçili bir liste yoksa
        if (monthlyListViewModel.isListEmpty.value == true) {
            showCurrentToast(getString(R.string.already_no_data), false)
        } else {
            // Seçile güne ait menüyü silinmesi hakkında bilgi içeren diyaloğun tanımı
            val removeDialog = Utility.getRemoveDayDialog(requireActivity())
            removeDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.remove)) { dialog, _ ->
                closeFab()
                dialog.dismiss()
                monthlyListViewModel.removeSelectedDay()
                showCurrentToast(getString(R.string.food_removed_of_date, selectedDay), true)
            }
            // Diyaloğu göster
            removeDialog.show()
        }
    }

    /**
     * Sunucuya bağlanarak güncel aylık listeyi indirir.
     * @param isRefresh Eğer mevcut menüleri güncelliyorsa -> true
     * @param imgQuality SeekBar ile seçilen resim kalitesi
     */
    private fun getMonthlyListData(isRefresh: Boolean, imgQuality: Int) {
        monthlyListViewModel.getMonthlyFoodData(imgQuality).observe(viewLifecycleOwner, Observer {
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
        }
    }

    private fun loadAndShowListUpdateDialog(isRefresh: Boolean) {
        UpdateMonthlyListDialogFragment.newInstance(isRefresh).show(childFragmentManager, "no tag")
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
        currentToast = Toast.makeText(requireContext(), text, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    /**
     * Alınan butonu 1 saniyeliğine disable eder.
     * Butonlara çift tıklamanın önüne geçmek için kullanılır
     */
    private fun View.setOnSafeClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener { onSafeClick(it) }
        setOnClickListener(safeClickListener)
    }

    override fun onAdWatched(callback: MonthlyDialogCallBackModel) {
        getMonthlyListData(callback.isRefresh, callback.imgQuality)

    }

}