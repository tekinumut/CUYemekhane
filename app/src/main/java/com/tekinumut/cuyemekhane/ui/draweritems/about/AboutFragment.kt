package com.tekinumut.cuyemekhane.ui.draweritems.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragmentDB
import com.tekinumut.cuyemekhane.databinding.FragmentAboutBinding
import com.tekinumut.cuyemekhane.library.Utility

class AboutFragment : BaseFragmentDB<FragmentAboutBinding>(R.layout.fragment_about) {

    private val aboutViewModel: AboutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            aboutVm = aboutViewModel
            sendFeedback.setOnClickListener { sendFeedBack() }
            rateApp.setOnClickListener { rateApp() }
            githubSourceCode.setOnClickListener { openGithubPage() }
        }
    }

    //Geri bildirim gönder. ( Mail yolu ile )
    private fun sendFeedBack() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myGmailAddress)))
        startActivity(Intent.createChooser(intent, getString(R.string.send_mail)))
    }

    //Uygulamayı değerlendirme sayfasına git
    private fun rateApp() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context?.packageName)))
        } catch (e: ActivityNotFoundException) {
            val backUpURL = "https://play.google.com/store/apps/details?id=" + context?.packageName
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(backUpURL)))
        }
    }

    private fun openGithubPage() {
        Utility.openWebSite(requireContext(), getString(R.string.github_address))
    }
}