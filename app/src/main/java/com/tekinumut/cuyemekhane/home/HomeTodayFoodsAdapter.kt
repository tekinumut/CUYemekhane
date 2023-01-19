package com.tekinumut.cuyemekhane.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowTodayFoodsBinding

class HomeTodayFoodsAdapter : ListAdapter<TodayFoodUIModel, HomeTodayFoodsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTodayFoodsViewHolder {
        return HomeTodayFoodsViewHolder(parent.viewBinding(RowTodayFoodsBinding::inflate))
    }

    override fun onBindViewHolder(holder: HomeTodayFoodsViewHolder, position: Int) {
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