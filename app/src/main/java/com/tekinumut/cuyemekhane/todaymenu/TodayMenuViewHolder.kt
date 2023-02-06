package com.tekinumut.cuyemekhane.todaymenu

import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.databinding.RowTodayFoodsBinding

class TodayMenuViewHolder(
    private val binding: RowTodayFoodsBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(food: TodayFoodUIModel) {
        with(binding) {
            imageFoodIcon.setImageUrl(food.imageUrl)
            textTitle.text = food.name
            textCategory.text = food.category
            textCalorie.text = context.getString(R.string.food_calorie_at_end, food.calorie)
        }
    }
}