package com.tekinumut.cuyemekhane.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.Duyurular

class DuyurularAdapter(private val duyuruList: List<Duyurular>) : RecyclerView.Adapter<DuyurularAdapter.DuyurularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuyurularViewHolder {
        return DuyurularViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rec_in_duyurular, parent, false))
    }

    override fun getItemCount(): Int = duyuruList.size

    override fun onBindViewHolder(holder: DuyurularViewHolder, position: Int) {
        val model = duyuruList[position]
        Utility.setImageViewWithBase64(holder.image, model.picBase64)
        holder.title.text = model.title
        holder.content.text = model.content
    }


    class DuyurularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_duyurular)
        val title: TextView = itemView.findViewById(R.id.tv_duyuru_title)
        var content: TextView = itemView.findViewById(R.id.tv_duyuru_content)
    }
}

