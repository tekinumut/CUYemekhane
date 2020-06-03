package com.tekinumut.cuyemekhane.ui.draweritems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.DailyMonthlyListAdapter
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.library.Resource
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.Food
import com.tekinumut.cuyemekhane.models.FoodWithDetailComp
import com.tekinumut.cuyemekhane.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_daily_list.*

class DailyListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mainPref: MainPref
    private lateinit var recyclerDaily: RecyclerView
    private lateinit var refreshDaily: SwipeRefreshLayout
    private val loadingDialog by lazy { Utility.getLoadingDialog(requireActivity()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init(requireView())

        observeDailyList()
        getFoodData(false)

        btn_open_web_page.setOnClickListener {
            Utility.openListWebSite(requireContext())
        }

    }

    private fun init(view: View) {
        mainPref = MainPref.getInstance(requireContext())
        recyclerDaily = view.findViewById(R.id.recyclerDailyList)
        refreshDaily = view.findViewById(R.id.refreshDailyList)
        refreshDaily.setOnRefreshListener(this)
        recyclerDaily.layoutManager = LinearLayoutManager(context)
        recyclerDaily.setHasFixedSize(true)

    }

    /**
     *
     */
    private fun observeDailyList() {
        mainViewModel.getDailyList.observe(viewLifecycleOwner, Observer { dateWithAll ->
            dateWithAll?.let {
                mainViewModel.updateActionTitle(it.foodDate.name)
                if (it.yemekWithComponentComp.isNullOrEmpty()) {
                    // Eğer günün menüsü boş ise yemekhane tatil uyarısı yap
                    val model = manuelFoodDetailModel(getString(R.string.no_food_holiday))
                    recyclerDaily.adapter = DailyMonthlyListAdapter(model, ConstantsGeneral.dailyFragmentKey)
                } else {
                    // Sorun yoksa
                    recyclerDaily.adapter = DailyMonthlyListAdapter(it.yemekWithComponentComp, ConstantsGeneral.dailyFragmentKey)
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
            recyclerDaily.visibility = View.VISIBLE
            svDailyInfo.visibility = View.GONE
        } else {
            recyclerDaily.visibility = View.GONE
            svDailyInfo.visibility = View.VISIBLE
        }
    }

    private fun manuelFoodDetailModel(foodName: String): List<FoodWithDetailComp> {
        val foodModel = Food(food_id = -1, name = foodName, dateCreatorId = -1)
        val foodWithDetail = FoodWithDetailComp(foodModel, null)
        return listOf(foodWithDetail)
    }

    private fun getFoodData(isSwipeRefresh: Boolean) {
        if (shouldAutoRefreshData(isSwipeRefresh)) {
            mainViewModel.getFoodData(ConstantsGeneral.dbNameDaily, ConstantsGeneral.defDailyImgQuality)
                .observe(viewLifecycleOwner, Observer {
                    when (it) {
                        Resource.InProgress -> loadingDialog.show()
                        is Resource.Success -> {
                            Toast.makeText(context, getString(R.string.data_loaded), Toast.LENGTH_SHORT).show()
                            onSuccessAndError()
                        }
                        is Resource.Error -> {
                            val message = "${getString(R.string.error_loading_data)} \nHata sebebi: ${it.exception.message}"
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            onSuccessAndError()
                        }
                    }
                })
        }

    }

    private fun onSuccessAndError() {
        mainPref.save(ConstantsGeneral.prefCheckDailyListWorkedBefore, true)
        loadingDialog.dismiss()
        refreshDaily.isRefreshing = false
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
        refreshDaily.post { getFoodData(true) }
    }

}