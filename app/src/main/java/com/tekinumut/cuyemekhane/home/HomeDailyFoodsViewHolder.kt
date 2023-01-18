package com.tekinumut.cuyemekhane.home

import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.FoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.databinding.RowDailyFoodsBinding

class HomeDailyFoodsViewHolder(
    private val binding: RowDailyFoodsBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(foodItem: FoodUIModel) {
        with(binding) {
            imageFoodIcon.setImageUrl(foodItem.imageUrl)
            textTitle.text = foodItem.name
            textCategory.text = foodItem.category
            textCalorie.text = context.getString(R.string.food_calorie_at_end, foodItem.calorie)
        }
    }
}