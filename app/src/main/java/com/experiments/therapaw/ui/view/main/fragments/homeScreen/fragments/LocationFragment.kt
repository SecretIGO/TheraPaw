package com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.experiments.therapaw.R
import com.experiments.therapaw.databinding.FragmentLocationBinding
import com.experiments.therapaw.ui.view.main.fragments.homeScreen.fragments.utils.location.UserDirectionOverlay
import com.experiments.therapaw.data.viewmodel.LocationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.max
import kotlin.math.min

class LocationFragment : Fragment(), SensorEventListener {

    private lateinit var binding: FragmentLocationBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationViewModel: LocationViewModel
    private var userOverlay: UserDirectionOverlay? = null
    private var userGeoPoint: GeoPoint? = null
    private var firebaseGeoPoint: GeoPoint? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationViewModel = LocationViewModel()

        locationViewModel.fetchLocationData()
        locationViewModel.locationData.observe(viewLifecycleOwner) { location ->
            firebaseGeoPoint = GeoPoint(location.latitude, location.longitude)
            addMarkerToMap(firebaseGeoPoint!!)

            if (userGeoPoint != null && firebaseGeoPoint != null) {
                zoomToFitBoth(userGeoPoint!!, firebaseGeoPoint!!)
            }
        }

        Configuration.getInstance().userAgentValue = requireActivity().packageName
        val map: MapView = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMinUpdateDistanceMeters(2.0f)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    userGeoPoint = GeoPoint(location.latitude, location.longitude)
                    updateMapLocation()
                }
            }
        }

        requestLocationPermission()
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            startLocationUpdates()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun updateMapLocation() {
        val map = binding.map

        if (userGeoPoint != null && firebaseGeoPoint != null) {

            zoomToFitBoth(userGeoPoint!!, firebaseGeoPoint!!)
        }

        map.overlays.remove(userOverlay)
        userOverlay = userGeoPoint?.let { UserDirectionOverlay(it) }
        userOverlay?.let { map.overlays.add(it) }

        map.invalidate()
    }

    private fun addMarkerToMap(location: GeoPoint) {
        val map = binding.map
        val marker = Marker(map)
        marker.position = location
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ico_location)
        marker.title = "Pet Location"

        map.overlays.add(marker)
        map.invalidate()
    }

    private fun zoomToFitBoth(userLocation: GeoPoint, firebaseLocation: GeoPoint) {
        val map = binding.map

        map.postDelayed({
            val latPadding = 0.01
            val lonPadding = 0.01

            val boundingBox =
            if (userLocation.longitude < firebaseLocation.longitude) {
                BoundingBox(
                    min(userLocation.latitude, firebaseLocation.latitude) + latPadding,
                    max(userLocation.longitude, firebaseLocation.longitude) + lonPadding,
                    max(userLocation.latitude, firebaseLocation.latitude) - latPadding,
                    min(userLocation.longitude, firebaseLocation.longitude) - lonPadding
                )
            } else {
                BoundingBox(
                    max(userLocation.latitude, firebaseLocation.latitude) + latPadding,
                    min(userLocation.longitude, firebaseLocation.longitude) + lonPadding,
                    min(userLocation.latitude, firebaseLocation.latitude) - latPadding,
                    max(userLocation.longitude, firebaseLocation.longitude) - lonPadding
                )
            }

            map.zoomToBoundingBox(boundingBox, true)
        }, 500)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
            SensorManager.SENSOR_DELAY_UI
        )
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        stopLocationUpdates()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)

            val azimuthInRadians = orientation[0]
            val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

            updateUserDirection(azimuthInDegrees)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun updateUserDirection(rotationDegrees: Float) {
        userOverlay?.updateRotation(rotationDegrees)
        binding.map.invalidate()
    }
}
