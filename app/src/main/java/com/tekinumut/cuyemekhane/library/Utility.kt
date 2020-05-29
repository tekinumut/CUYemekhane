package com.tekinumut.cuyemekhane.library

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.tekinumut.cuyemekhane.R
import java.io.ByteArrayOutputStream
import java.net.URL

class Utility {
    companion object {

        /**
         * Yükleniyor dialog penceresini dönderir.
         */
        fun getLoadingDialog(activity: Activity): AlertDialog = AlertDialog.Builder(activity)
            .setCancelable(false)
            .setView(R.layout.dialog_loading)
            .create()


        fun getRemoveDayDialog(activity: Activity): AlertDialog = AlertDialog.Builder(activity)
            .setTitle("Seçili menüyü sil")
            .setMessage("Seçili güne ait menüyü silinecek. Listeyi güncellediğinizde seçili güne ait liste mevcutsa tekrar yüklenir.")
            .setNegativeButton(R.string.iptal_et) { dialog, _ -> dialog.dismiss() }
            .create()


        /**
         * Aldığı URL'yi base64 formatına çevirir.
         */
        fun imgURLToBase64(foodImg: String): String {
            val input = URL(foodImg).openStream()
            val bitmap = BitmapFactory.decodeStream(input)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        /**
         * Base64'ü bitmap nesnesine çevirir
         */
        fun base64toBitmap(base64Str: String): Bitmap {
            val decodedString: ByteArray = Base64.decode(base64Str, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        /**
         * Listelerin alındığı web sayfasını açar
         */
        fun openListWebSite(activity: Activity) {
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            builder.setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left)
            builder.setExitAnimations(activity, R.anim.slide_in_left, R.anim.slide_out_right)

            val tabIntent = builder.build()
            tabIntent.launchUrl(activity, Uri.parse(ConstantsOfWebSite.URL))

        }
    }

}