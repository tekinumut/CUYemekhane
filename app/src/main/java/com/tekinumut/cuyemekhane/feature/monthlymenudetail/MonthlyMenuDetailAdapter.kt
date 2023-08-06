package com.tekinumut.cuyemekhane.feature.monthlymenudetail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.isBiggerThanZero
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowMonthlyMenuDetailFoodsBinding

class MonthlyMenuDetailAdapter :
    ListAdapter<DailyFoodUIModel, MonthlyMenuDetailAdapter.MonthlyMenuDetailViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyMenuDetailViewHolder {
        return MonthlyMenuDetailViewHolder(
            parent.viewBinding(RowMonthlyMenuDetailFoodsBinding::inflate)
        )
    }

    override fun onBindViewHolder(holder: MonthlyMenuDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MonthlyMenuDetailViewHolder(
        private val binding: RowMonthlyMenuDetailFoodsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(food: DailyFoodUIModel) {
            with(binding) {
                imageFoodIcon.setImageUrl(url = food.imageUrl)
                textTitle.text = food.name
                food.calorie.isBiggerThanZero {
                    textCalorie.text = context.getString(R.string.food_calorie_at_end, it)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyFoodUIModel>() {
            override fun areItemsTheSame(
                oldItem: DailyFoodUIModel,
                newItem: DailyFoodUIModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DailyFoodUIModel,
                newItem: DailyFoodUIModel
            ) = oldItem == newItem
        }
    }
}