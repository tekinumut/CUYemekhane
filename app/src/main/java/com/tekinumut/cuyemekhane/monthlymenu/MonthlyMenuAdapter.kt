package com.tekinumut.cuyemekhane.monthlymenu

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.domain.model.mainpage.DailyMenuUIModel
import com.tekinumut.cuyemekhane.common.extensions.isBiggerThanZero
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowDailyMenusBinding

class MonthlyMenuAdapter(
    private val onItemClickListener: (dailyMenu: DailyMenuUIModel) -> Unit
) : ListAdapter<DailyMenuUIModel, MonthlyMenuAdapter.MonthlyMenuViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyMenuViewHolder {
        return MonthlyMenuViewHolder(parent.viewBinding(RowDailyMenusBinding::inflate))
    }

    override fun onBindViewHolder(holder: MonthlyMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MonthlyMenuViewHolder(
        private val binding: RowDailyMenusBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(menu: DailyMenuUIModel) {
            val firstFood = menu.foodList.firstOrNull() ?: return
            with(binding) {
                textDate.text = menu.date
                imageFoodIcon.setImageUrl(
                    url = firstFood.imageUrl,
                    requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)
                )
                textTitle.text = firstFood.name
                menu.totalCalorie.isBiggerThanZero {
                    textCalorie.text = context.getString(R.string.food_calorie_at_end, it)
                }
                root.setOnClickListener { onItemClickListener(menu) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyMenuUIModel>() {
            override fun areItemsTheSame(
                oldItem: DailyMenuUIModel,
                newItem: DailyMenuUIModel
            ) = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: DailyMenuUIModel,
                newItem: DailyMenuUIModel
            ) = oldItem == newItem
        }
    }
}