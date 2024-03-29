package com.tekinumut.cuyemekhane.ui.draweritems.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tekinumut.cuyemekhane.R
import com.tekinumut.cuyemekhane.base.BaseFragmentVB
import com.tekinumut.cuyemekhane.databinding.FragmentContactBinding

class ContactFragment : BaseFragmentVB<FragmentContactBinding>(FragmentContactBinding::inflate) {

    // Haritaya hangi ölçekte zoom yapılacak
    private val zoomLevel = 16.0f

    // Harita açılınca hangi terim aransın
    private val searchingAddress = "Çukurova+Üniversitesi+Merkezi+Kafeterya"

    // Google Maps'te gidilecek konumun enlem ve boylam bilgileri
    private val latitude: Double = 37.059232
    private val longitude: Double = 35.354404

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToGoogleMaps.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("geo:$latitude,$longitude?z=$zoomLevel&q=$searchingAddress")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.cant_open_google_maps),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}