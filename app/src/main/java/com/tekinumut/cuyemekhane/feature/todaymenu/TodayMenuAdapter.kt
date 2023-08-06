package com.tekinumut.cuyemekhane.feature.todaymenu

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.TodayFoodUIModel
import com.tekinumut.cuyemekhane.common.extensions.isBiggerThanZero
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowTodayFoodsBinding

class TodayMenuAdapter(
    private val onItemClickListener: (imageUrl: String) -> Unit
) : ListAdapter<TodayFoodUIModel, TodayMenuAdapter.TodayMenuViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMenuViewHolder {
        return TodayMenuViewHolder(parent.viewBinding(RowTodayFoodsBinding::inflate))
    }

    override fun onBindViewHolder(holder: TodayMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodayMenuViewHolder(
        private val binding: RowTodayFoodsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(food: TodayFoodUIModel) {
            with(binding) {
                imageFoodIcon.setImageUrl(
                    url = food.imageUrl,
                    requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                )
                textTitle.text = food.name
                textCategory.text = food.category
                food.calorie.isBiggerThanZero {
                    textCalorie.text = context.getString(R.string.food_calorie_at_end, it)
                }
                root.setOnClickListener { onItemClickListener(food.imageUrl) }
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