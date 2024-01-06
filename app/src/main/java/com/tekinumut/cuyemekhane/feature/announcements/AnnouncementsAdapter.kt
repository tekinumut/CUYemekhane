package com.tekinumut.cuyemekhane.feature.announcements

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tekinumut.cuyemekhane.common.domain.model.announcements.AnnouncementsUIModel
import com.tekinumut.cuyemekhane.common.extensions.setImageUrl
import com.tekinumut.cuyemekhane.common.extensions.viewBinding
import com.tekinumut.cuyemekhane.databinding.RowAnnouncementBinding

class AnnouncementsAdapter(
    private val onItemClickListener: (descriptionUrl: String) -> Unit
) : ListAdapter<AnnouncementsUIModel, AnnouncementsAdapter.AnnouncementViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        return AnnouncementViewHolder(parent.viewBinding(RowAnnouncementBinding::inflate))
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AnnouncementViewHolder(
        private val binding: RowAnnouncementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(announcement: AnnouncementsUIModel) {
            with(binding) {
                imageLogo.setImageUrl(
                    url = announcement.logoImageUrl,
                    hideIfNull = false
                )
                textTitle.text = announcement.title
                textDescription.text = announcement.description
                root.setOnClickListener {
                    onItemClickListener(announcement.descriptionUrl)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AnnouncementsUIModel>() {
            override fun areItemsTheSame(
                oldItem: AnnouncementsUIModel,
                newItem: AnnouncementsUIModel
            ) = oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: AnnouncementsUIModel,
                newItem: AnnouncementsUIModel
            ) = oldItem == newItem
        }
    }
}