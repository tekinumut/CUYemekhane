package com.tekinumut.cuyemekhane.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.library.Utility
import com.tekinumut.cuyemekhane.models.FoodWithDetailComp

class DailyMonthlyListAdapter(
    private val recyclerList: List<FoodWithDetailComp>, private val hostFragmentKey: Int
) : RecyclerView.Adapter<DailyMonthlyListAdapter.DailyListViewHolder>() {

    private var currentToast: Toast? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyListViewHolder {
        return DailyListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rec_in_daily_monthly_list, parent, false))
    }

    override fun getItemCount(): Int = recyclerList.size

    override fun onBindViewHolder(holder: DailyListViewHolder, position: Int) {
        val model = recyclerList[position]
        // imageView nesnesini tanımla
        if (hostFragmentKey == ConstantsGeneral.monthlyFragmentKey) {
            Utility.setImageViewWithBase64(holder.image, model.foodDetailAndComp?.foodDetail?.picBase64)
        } else {
            Glide.with(holder.image).load(model.yemek.detailURL).into(holder.image)
        }

        holder.title.text = model.yemek.name

        // Eğer kategori değeri boş veya null ise arka plan rengini de yok et
        model.yemek.category?.let {
            if (it.isEmpty()) return@let null
            else holder.category.text = it
        } ?: run { holder.category.visibility = View.GONE }

        model.yemek.calorie?.let {
            if (it.isEmpty()) return@let null
            else holder.calorie.text = it
        } ?: run { holder.calorie.visibility = View.GONE }

        holder.itemView.setOnClickListener {
            model.foodDetailAndComp?.let { detailWithComp ->
                val navController = Navigation.findNavController(holder.itemView)
                val bundle = Bundle()
                val componentList: ArrayList<String> = listToArrayList(detailWithComp.foodComponent.map { it.name })
                bundle.putString(ConstantsGeneral.bundleFoodName, model.yemek.name)
                bundle.putString(ConstantsGeneral.bundleImgKey, model.foodDetailAndComp.foodDetail.picBase64)
                bundle.putStringArrayList(ConstantsGeneral.bundleComponentListKey, componentList)

                if (navController.currentDestination?.id == getCurrentFragmentID)
                    navController.navigate(getActionKey, bundle)
            } ?: run {
                showCurrentToast(holder.itemView.context)

            }
        }
    }


    /**
     * Yemek içeriğinin açıldığı fragment'a göre ilgili destination'a git
     */
    private val getActionKey: Int = if (hostFragmentKey == ConstantsGeneral.dailyFragmentKey)
        R.id.action_nav_daily_list_to_componentFragment
    else
        R.id.action_nav_monthly_list_to_componentFragment

    /**
     * Hangi fragment'tan yemek içerik penceresini açıyorum?
     */
    private val getCurrentFragmentID: Int = if (hostFragmentKey == ConstantsGeneral.dailyFragmentKey)
        R.id.nav_daily_list
    else
        R.id.nav_monthly_list


    private fun showCurrentToast(context: Context) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, context.getString(R.string.no_component_found), Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    private fun listToArrayList(list: List<String>): ArrayList<String> {
        val arrayList = ArrayList<String>()
        list.forEach { arrayList.add(it) }
        return arrayList
    }

    class DailyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_food_image)
        val title: TextView = itemView.findViewById(R.id.tv_food_title)
        var category: TextView = itemView.findViewById(R.id.tv_category)
        var calorie: TextView = itemView.findViewById(R.id.tv_calorie)

    }
}
//    private fun createOnClickListener(videoId: String): View.OnClickListener {
//        return View.OnClickListener {
//            val directions = DailyListFragment.viewVideoDetails(videoId)
//            val extras = FragmentNavigatorExtras(
//                binding.videoTitle to "title_$videoId",
//                binding.videoDuration to "duration_$videoId",
//                binding.videoThumbnail to "thumbnail_$videoId")
//            it.findNavController().navigate(directions, extras)
//        }
//    }
