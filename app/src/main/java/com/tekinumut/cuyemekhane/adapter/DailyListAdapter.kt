package com.tekinumut.cuyemekhane.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.FoodWithDetailComp

class DailyListAdapter(private val recyclerList: List<FoodWithDetailComp>) : RecyclerView.Adapter<DailyListAdapter.DailyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyListViewHolder {

        return DailyListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rec_in_daily_list, parent, false))
    }

    override fun getItemCount(): Int = recyclerList.size

    override fun onBindViewHolder(holder: DailyListViewHolder, position: Int) {
        val model = recyclerList[position]
        model.foodDetailAndComp?.let { holder.image.setImageBitmap(Utility.base64toBitmap(it.foodDetail.picBase64)) }
        holder.title.text = model.yemek.name

        model.yemek.category?.let {
            if (it.isEmpty()) return@let null
            else holder.category.text = it
        } ?: run { holder.category.visibility = View.GONE }

        model.yemek.calorie?.let {
            if (it.isEmpty()) return@let null
            else holder.calorie.text = it
        } ?: run { holder.calorie.visibility = View.GONE }
    }

    class DailyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_food_image)
        val title: TextView = itemView.findViewById(R.id.tv_food_title)
        var category: TextView = itemView.findViewById(R.id.tv_category)
        var calorie: TextView = itemView.findViewById(R.id.tv_calorie)

    }
}