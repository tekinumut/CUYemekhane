package com.tekinumut.cuyemekhane.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.interfaces.AutoUpdateCheckBoxInterface
import com.tekinumut.cuyemekhane.models.specificmodels.AutoUpdateChecked

/**
 * Otomatik olarak güncellenecek olan ekranların seçildiği recyclerView adapter'ı
 */
class AutoUpdateAdapter(
    private val autoUpdateList: List<AutoUpdateChecked>,
    private val autoUpdateCheckedInterface: AutoUpdateCheckBoxInterface
) : RecyclerView.Adapter<AutoUpdateAdapter.AutoUpdateViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoUpdateViewHolder {
        return AutoUpdateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rec_in_auto_update, parent, false))
    }

    override fun getItemCount(): Int = autoUpdateList.size

    override fun onBindViewHolder(holder: AutoUpdateViewHolder, position: Int) {
        val model = autoUpdateList[position]
        holder.checkBox.text = model.title
        holder.checkBox.isChecked = model.isChecked

        holder.checkBox.setOnCheckedChangeListener { view, isChecked ->
            autoUpdateCheckedInterface.onCheckChange(AutoUpdateChecked(view.text.toString(), isChecked))
        }
    }


    class AutoUpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.rec_in_checkbox_list)
    }
}

