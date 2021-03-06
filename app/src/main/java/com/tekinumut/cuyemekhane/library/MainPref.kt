package com.tekinumut.cuyemekhane.library

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Uygulamanın varsayılan sharedpreferences nesnesi çağrılır
 * @return preferences nesnesi
 */
@Suppress("unused")
@SuppressLint("ApplySharedPref")
class MainPref private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun save(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).commit()
    }

    fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).commit()
    }

    fun save(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).commit()
    }

    fun save(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).commit()
    }

    fun save(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).commit()
    }

    fun save(key: String, value: Set<String>) {
        sharedPreferences.edit().putStringSet(key, value).commit()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun getStringSet(key: String, defValue: MutableSet<String>?): MutableSet<String>? {
        return sharedPreferences.getStringSet(key, defValue)
    }

    fun getAll(): Map<String, *> {
        return sharedPreferences.all
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).commit()
    }

    fun clearPrefs() {
        sharedPreferences.edit().clear().commit()
    }

    companion object {

        private var rmPrefs: MainPref? = null

        fun getInstance(context: Context): MainPref {
            return if (rmPrefs == null) MainPref(context)
            else rmPrefs as MainPref
        }
    }
}