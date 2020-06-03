package com.tekinumut.cuyemekhane.ui.draweritems

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tekinumut.cuyemekhane.R
import kotlinx.android.synthetic.main.fragment_contact.*


class ContactFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_to_google_maps.setOnClickListener {

            // Haritaya hangi ölçekte zoom yapılacak
            val zoomLevel = 16.0f
            // Harita açılınca hangi terim aransın
            val searchingAddress = "Çukurova+Üniversitesi+Merkezi+Kafeterya"

            val gmmIntentUri = Uri.parse("geo:37.059232,35.354404?z=$zoomLevel&q=$searchingAddress")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(requireContext(), getString(R.string.cant_open_google_maps), Toast.LENGTH_LONG).show()
            }
        }
    }
}