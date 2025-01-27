package com.experiments.therapaw.ui.view.main.fragments.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.FragmentLocationBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class LocationFragment : Fragment() {

    private lateinit var binding: FragmentLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().setUserAgentValue(requireActivity().packageName)

        val map: MapView = binding.map

        map.setTileSource(TileSourceFactory.MAPNIK)

        map.setMultiTouchControls(true)

        map.controller.setZoom(20.0)
        map.controller.setCenter(GeoPoint(14.551831011201454, 121.01378950272809))
    }
}