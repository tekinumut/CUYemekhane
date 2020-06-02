package com.tekinumut.cuyemekhane.library

import android.os.SystemClock
import androidx.preference.Preference

/**
 * Preference nesnesine 1 saniye içinde birden fazla tıklamayı engeller.
 * Diyalog pencerelerinin çift açılmasının önüne geçer.
 */
class SafePreferenceClickListener(
   private var defaultInterval: Int = 1000, private val onSafeCLick: (Preference) -> Unit) : Preference.OnPreferenceClickListener {
   private var lastTimeClicked: Long = 0
   override fun onPreferenceClick(preference: Preference): Boolean {
      if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
         return false
      }
      lastTimeClicked = SystemClock.elapsedRealtime()
      onSafeCLick(preference)
      return true
   }

}