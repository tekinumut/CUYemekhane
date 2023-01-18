package com.tekinumut.cuyemekhane.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.FoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowDailyFoodsBinding

class HomeDailyFoodsAdapter : ListAdapter<FoodUIModel, HomeDailyFoodsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeDailyFoodsViewHolder {
        return HomeDailyFoodsViewHolder(parent.viewBinding(RowDailyFoodsBinding::inflate))
    }

    override fun onBindViewHolder(holder: HomeDailyFoodsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodUIModel>() {
            override fun areItemsTheSame(
                oldItem: FoodUIModel,
                newItem: FoodUIModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FoodUIModel,
                newItem: FoodUIModel
            ) = oldItem == newItem
        }
    }
}