package com.tekinumut.cuyemekhane.ui.draweritems.dailylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.DailyMonthlyListAdapter
import com.tekinumut.cuyemekhane.library.*
import com.tekinumut.cuyemekhane.models.Food
import com.tekinumut.cuyemekhane.models.FoodWithDetailComp
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_daily_list.*

class DailyListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val dailyListViewModel: DailyListViewModel by viewModels()
    private lateinit var mainPref: MainPref
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

        observeDailyList()
        getFoodData(false)

        btn_open_web_page.setOnClickListener {
            Utility.openWebSite(requireContext(), ConstantsOfWebSite.MainPageURL)
        }

    }

    private fun init() {
        mainPref = MainPref.getInstance(requireContext())
        refreshDailyList.setOnRefreshListener(this)
        recyclerDailyList.layoutManager = LinearLayoutManager(context)
        recyclerDailyList.setHasFixedSize(true)
    }

    /**
     *
     */
    private fun observeDailyList() {
        dailyListViewModel.getDailyList.observe(viewLifecycleOwner, Observer { dateWithAll ->
            dateWithAll?.let {
                mainViewModel.updateActionTitle(it.foodDate.name)
                if (it.yemekWithComponentComp.isNullOrEmpty()) {
                    // Eğer günün menüsü boş ise yemekhane tatil uyarısı yap
                    val model = manuelFoodDetailModel(getString(R.string.no_food_holiday))
                    recyclerDailyList.adapter = DailyMonthlyListAdapter(model, ConstantsGeneral.dailyFragmentKey)
                } else {
                    // Sorun yoksa
                    recyclerDailyList.adapter = DailyMonthlyListAdapter(it.yemekWithComponentComp, ConstantsGeneral.dailyFragmentKey)
                }
                switchView(true)
            } ?: run {
                //İnternet olmadan giriş yaptığında veya bir şekilde tüm veri null gelirse
                switchView(false)
            }

        })
    }

    private fun switchView(isDaily: Boolean) {
        if (isDaily) {
            recyclerDailyList.visibility = View.VISIBLE
            svDailyInfo.visibility = View.GONE
        } else {
            recyclerDailyList.visibility = View.GONE
            svDailyInfo.visibility = View.VISIBLE
        }
    }

    private fun manuelFoodDetailModel(foodName: String): List<FoodWithDetailComp> {
        val foodModel = Food(food_id = -1, name = foodName, dateCreatorId = -1)
        val foodWithDetail = FoodWithDetailComp(foodModel, null)
        return listOf(foodWithDetail)
    }

    /**
     * Web sitesinden günlük verileri al
     */
    private fun getFoodData(isSwipeRefresh: Boolean) {
        if (shouldAutoRefreshData(isSwipeRefresh)) {
            dailyListViewModel.getDailyListData(ConstantsGeneral.defMonthlyImgQuality).observe(viewLifecycleOwner, Observer {
                when (it) {
                    Resource.InProgress -> if (!isSwipeRefresh) loadingDialog.show()
                    is Resource.Success -> {
                        mainPref.save(ConstantsGeneral.prefCheckDailyListWorkedBefore, true)
                        onSuccessAndError(getString(R.string.data_loaded))
                    }
                    is Resource.Error -> {
                        val message = "${getString(R.string.error_loading_data)} \nHata sebebi: ${it.exception.message}"
                        onSuccessAndError(message)
                    }
                }
            })
        }

    }

    /**
     * Bağlantı sonuçlanınca
     */
    private fun onSuccessAndError(message: String) {
        loadingDialog.dismiss()
        refreshDailyList.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    /**
     * "İlgili sayfa otomatik güncellenecek mi?" sorusunun cevabı
     * @return otomatik güncelleme durumu
     * Algoritma adımları :
     * 1 -> Kullanıcı kendi isteği ile yenilemek istiyorsa 6. adıma git. İşlem otomatik gerçekleşiyorsa 2. adıma git
     * 2 -> Ayarlar menüsünden otomatik güncelleme kapatılmış mı diye bak.
     * 3 -> Eğer kapatılmış ise false dön ve bitir. Kapatılmamış ise 4. adıma git
     * 4 -> Kullanıcı daha önce uygulama açıkken güncelleme yapmış mı bak.
     * 5 -> Eğer yapmış ise false dön ve bitir. Yapmamışsa 6. adıma git
     * 6 -> True dön ve bitir.
     */
    private fun shouldAutoRefreshData(isSwipeRefresh: Boolean): Boolean {
        val autoUpdateVal = mainPref.getBoolean(ConstantsGeneral.prefDailyAutoUpdateKey, ConstantsGeneral.defValDailyAutoUpdate)
        val isWorkedBefore = mainPref.getBoolean(ConstantsGeneral.prefCheckDailyListWorkedBefore, false)
        // Otomatik güncelleme olduğu durumda oluşacak sonucu değere ata
        var autoUpdateResult = if (autoUpdateVal) !isWorkedBefore else false
        // Eğer kullanıcı kendi yenilemek istediyse direk true yap
        if (isSwipeRefresh) autoUpdateResult = true
        return autoUpdateResult
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    override fun onRefresh() {
        refreshDailyList.post { getFoodData(true) }
    }

}