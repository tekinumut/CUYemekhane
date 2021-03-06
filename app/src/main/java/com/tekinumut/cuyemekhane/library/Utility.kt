package com.tekinumut.cuyemekhane.library

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.tekinumut.cuyemekhane.R
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Utility {
    companion object {

        /**
         * Yükleniyor dialog penceresini dönderir.
         */
        fun getLoadingDialog(activity: Activity): AlertDialog {
            val dialog = AlertDialog.Builder(activity)
                .setCancelable(false)
                .setView(R.layout.dialog_loading)
                .create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_def_dialog)
            return dialog
        }

        fun getRemoveDayDialog(activity: Activity): AlertDialog = AlertDialog.Builder(activity)
            .setTitle("Seçili menüyü sil")
            .setMessage("Seçili güne ait menü silinecek. Listeyi güncellediğinizde seçili güne ait menü mevcutsa tekrar yüklenir.")
            .setNegativeButton(R.string.iptal_et) { dialog, _ -> dialog.dismiss() }
            .create()


        /**
         * Aldığı URL'yi base64 formatına çevirir.
         */
        fun imgURLToBase64(foodImg: String): String {
            val input = URL(foodImg).openStream()
            val bitmap = BitmapFactory.decodeStream(input)
            val baos = ByteArrayOutputStream()
            // CompressFormat PNG olursa quality'i dikkate almaz.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        /**
         * Base64'ü bitmap nesnesine çevirip imageView nesnesine yükler
         * Değer null ise varsayılan resmi yükler
         */
        fun setImageViewWithBase64(imageView: ImageView, base64Str: String? = null) {
            base64Str?.let {
                val decodedString: ByteArray = Base64.decode(base64Str, Base64.DEFAULT)
                val bitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageView.setImageBitmap(bitmap)
            } ?: run {
                imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.no_image))
            }
        }

        /**
         * Base64'ü bitmap nesnesine çevirir
         */
        fun base64ToString(base64Str: String): Bitmap {
            val decodedString: ByteArray = Base64.decode(base64Str, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        /**
         * Listelerin alındığı web sayfasını açar
         */
        fun openWebSite(context: Context, url: String) {

            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            val colorSchemeBuilder = CustomTabColorSchemeParams.Builder().run {
                setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .build()
            }
            builder.setDefaultColorSchemeParams(colorSchemeBuilder)
            builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left)
            builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right)

            val tabIntent = builder.build()
            tabIntent.launchUrl(context, Uri.parse(url))
        }

        /**
         * Calendar değerini belirlenmiş zaman formatında dönderir
         * @return Ex: 11.05.2020 biçiminde zaman değişkeni
         */
        fun getCalendarSDF(dayOfMonth: Int, monthOfYear: Int, year: Int): String {
            val sdf = ConstantsGeneral.defaultSDF
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            sdf.calendar = calendar
            return sdf.format(calendar.time)
        }

        /**
         * Aldığı defaultSDF'e uygun date verisini calendar biçiminde arraylist'e ekler
         * @param dateList başlangıcı dd.MM.yyyy biçiminde tarih bilgisi içeren list
         * @return calendar nesnesi içeren array
         */
        fun getCalendarArray(dateList: List<String>): Array<Calendar> {
            val calendar = Calendar.getInstance()
            val calendarList = ArrayList<Calendar>()
            dateList.forEach { datePart ->
                try {
                    val cloned = calendar.clone() as Calendar
                    val dayMonthYear = datePart.substring(0, 10).split(".").map { it.toInt() }
                    // 11.01.2020 tarihini gün ay yıl (11, 01, 2020) olarak 3 parçaya böler
                    cloned.set(dayMonthYear[2], dayMonthYear[1] - 1, dayMonthYear[0])
                    calendarList.add(cloned)
                } catch (e: Exception) {
                    // Firebase bilgilendir.
                }
            }
            return calendarList.toTypedArray()
        }

        /**
         * Gece ve gündüz modu seçimini yapar
         * @param type -1 -> Cihaz seçimini kullanır
         *              1 -> Gündüz modu
         *              2 -> Gece modu
         */
        fun setTheme(type: Int) {
            when (type) {
                -1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        /**
         * Banner'in gizlenme tarihinin bitme durumu
         */
        fun isBannerTimeExpired(context: Context): Boolean {
            val mainPref = MainPref.getInstance(context)
            val expireTimeStamp = mainPref.getLong(context.getString(R.string.isBannerExpire), ConstantsGeneral.defRewardExpireDate.time)
            return expireTimeStamp <= ConstantsGeneral.currentTimeStamp()
        }

        /**
         * Milisaniye cinsinden long değer uygun formata çevirir
         * Örneğin 59:00 dakika kaldı gibi.
         */
        fun timeMillisToHourFormat(remainingTime: Long): String {
            val diffSeconds: Long = remainingTime / 1000 % 60
            val diffMinutes: Long = remainingTime / (60 * 1000) % 60
            val sdf = SimpleDateFormat("mm:ss", Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MINUTE, diffMinutes.toInt())
            calendar.set(Calendar.SECOND, diffSeconds.toInt())
            val formattedText = sdf.format(calendar.time)
            val type: String = if (diffMinutes == 0L) "saniye" else "dakika"
            return " Video Reklam $formattedText $type sonra açılacak"
        }

        /**
         * Güncel zaman verilen değeri ekleyerek ileri bir tarih değeri elde eder.
         */
        fun addExtraTimeToCurrent(delayTime: DelayTime): Long {
            val extraTime = when (delayTime) {
                DelayTime.RemoveBanner -> ConstantsGeneral.defRemoveBannerDelaytime
                DelayTime.UpdateMonthylList -> ConstantsGeneral.defUpdateMonthlyListDelayTime
            }
            return ConstantsGeneral.currentTimeStamp() + extraTime * 1000
        }

        /**
         * Koyu tema seçili mi
         */
        fun isDarkThemeSelected(context: Context): Boolean {
            return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> false
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        }
    }

    enum class DelayTime {
        RemoveBanner,
        UpdateMonthylList
    }

}

