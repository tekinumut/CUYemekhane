package com.tekinumut.cuyemekhane.todaymenu

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowTodayFoodsBinding

class TodayMenuAdapter : ListAdapter<TodayFoodUIModel, TodayMenuViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMenuViewHolder {
        return TodayMenuViewHolder(parent.viewBinding(RowTodayFoodsBinding::inflate))
    }

    override fun onBindViewHolder(holder: TodayMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
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