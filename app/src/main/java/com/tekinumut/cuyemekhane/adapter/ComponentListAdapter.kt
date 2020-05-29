package com.tekinumut.cuyemekhane.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R

class ComponentListAdapter(private val recyclerList: List<String>
) : RecyclerView.Adapter<ComponentListAdapter.ComponentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        return ComponentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rec_in_component_list, parent, false))
    }

    override fun getItemCount(): Int = recyclerList.size

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val model = recyclerList[position]
        holder.title.text = model
    }

    class ComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.rec_in_tv_component)
    }


}