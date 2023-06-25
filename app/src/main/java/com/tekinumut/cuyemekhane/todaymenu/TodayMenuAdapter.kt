package com.tekinumut.cuyemekhane.todaymenu

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowTodayFoodsBinding

class TodayMenuAdapter : ListAdapter<TodayFoodUIModel, TodayMenuAdapter.TodayMenuViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMenuViewHolder {
        return TodayMenuViewHolder(parent.viewBinding(RowTodayFoodsBinding::inflate))
    }

    override fun onBindViewHolder(holder: TodayMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

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

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TodayFoodUIModel>() {
            override fun areItemsTheSame(
                oldItem: TodayFoodUIModel,
                newItem: TodayFoodUIModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: TodayFoodUIModel,
                newItem: TodayFoodUIModel
            ) = oldItem == newItem
        }
    }
}