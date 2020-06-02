package com.tekinumut.cuyemekhane.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.adapter.AutoUpdateAdapter
import com.tekinumut.cuyemekhane.interfaces.AutoUpdateCheckBoxInterface
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.MainPref
import com.tekinumut.cuyemekhane.models.specificmodels.AutoUpdateChecked

class AutoUpdateDialogFragment : DialogFragment(), AutoUpdateCheckBoxInterface {

    private lateinit var recyclerAutoUpdate: RecyclerView
    private lateinit var mView: View
    private lateinit var mainPref: MainPref

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            mView = requireActivity().layoutInflater.inflate(R.layout.auto_update_dialog_fragment, null)

            init()

            AlertDialog.Builder(it)
                .setNegativeButton(R.string.iptal_et) { dialog, _ -> dialog.dismiss() }
                .setView(mView)
                .setTitle(resources.getString(R.string.autoUpdateTitle))
                .setMessage(getString(R.string.auto_update_multi_list_dialog_message))
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun init() {
        recyclerAutoUpdate = mView.findViewById(R.id.recyclerAutoUpdate)
        mainPref = MainPref.getInstance(mView.context)
        recyclerAutoUpdate.layoutManager = LinearLayoutManager(context)
        recyclerAutoUpdate.setHasFixedSize(true)
        recyclerAutoUpdate.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerAutoUpdate.adapter = AutoUpdateAdapter(listOfAutoUpdate(), this)
    }

    /**
     * Gösterilecek ekranların listesi
     */
    private fun listOfAutoUpdate(): List<AutoUpdateChecked> {
        mainPref = MainPref.getInstance(mView.context)
        val dailyLastVal = mainPref.getBoolean(ConstantsGeneral.prefDailyAutoUpdateKey, ConstantsGeneral.defValDailyAutoUpdate)
        val duyurularLastVal = mainPref.getBoolean(ConstantsGeneral.prefDuyurularAutoUpdateKey, ConstantsGeneral.defValDuyurularAutoUpdate)
        val pricingLastVal = mainPref.getBoolean(ConstantsGeneral.prefPricingAutoUpdateKey, ConstantsGeneral.defValPricingAutoUpdate)

        return listOf(
            AutoUpdateChecked(getString(R.string.daily_action_title), dailyLastVal),
            AutoUpdateChecked(getString(R.string.duyurular_action_title), duyurularLastVal),
            AutoUpdateChecked(getString(R.string.pricing_action_title), pricingLastVal)
        )
    }

    /**
     * CheckBoxta meydana gelen değişimleri sharedPref'e yaz
     * @param autoUpdateChecked recyclerView'dan gelen obje
     * bu obje'de gelen title'a göre ilgili pref keyine erişiyoruz.
     */
    override fun onCheckChange(autoUpdateChecked: AutoUpdateChecked) {
        val prefKey = when (autoUpdateChecked.title) {
            getString(R.string.daily_action_title) -> ConstantsGeneral.prefDailyAutoUpdateKey
            getString(R.string.duyurular_action_title) -> ConstantsGeneral.prefDuyurularAutoUpdateKey
            getString(R.string.pricing_action_title) -> ConstantsGeneral.prefPricingAutoUpdateKey
            else -> return
        }
        mainPref.save(prefKey, autoUpdateChecked.isChecked)
    }
}