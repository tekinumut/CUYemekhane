package com.tekinumut.cuyemekhane.ui.draweritems.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.databinding.FragmentAboutBinding
import com.tekinumut.cuyemekhane.library.Utility
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    private val aboutViewModel: AboutViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)
        binding.aboutVm = aboutViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        send_feedback.setOnClickListener { sendFeedBack() }
        rate_app.setOnClickListener { rateApp() }
        github_source_code.setOnClickListener { openGithubPage() }

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