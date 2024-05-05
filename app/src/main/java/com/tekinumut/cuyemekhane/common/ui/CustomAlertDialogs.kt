package com.tekinumut.cuyemekhane.common.ui

import android.app.AlertDialog
import android.content.Context
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.common.util.ThemeMode

object CustomAlertDialogs {

    fun showChangeThemeDialog(
        context: Context,
        currentOption: ThemeMode,
        onOptionChanged: (selectedMode: ThemeMode) -> Unit
    ) {
        val options: Array<ThemeMode> = ThemeMode.entries.toTypedArray()
        val optionTexts = options.map { context.getString(it.textResource) }.toTypedArray()

        AlertDialog.Builder(context).apply {
            setTitle(context.getString(R.string.settings_change_theme_text_title))
            setSingleChoiceItems(optionTexts, currentOption.mode) { dialog, which ->
                onOptionChanged.invoke(options[which])
                dialog.dismiss()
            }
        }.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }.show()
    }
}