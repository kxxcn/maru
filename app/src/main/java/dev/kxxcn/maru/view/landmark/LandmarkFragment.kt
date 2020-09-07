package dev.kxxcn.maru.view.landmark

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.LandmarkFragmentBinding
import dev.kxxcn.maru.util.CAMERA_MOVE_ANIMATION_DURATION
import dev.kxxcn.maru.util.REQUEST_CODE_PERMISSION
import dev.kxxcn.maru.util.extension.displayHeight
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class LandmarkFragment : BaseFragment(), OnMapReadyCallback, LocationListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: LandmarkFragmentBinding

    private lateinit var map: NaverMap

    private var locationManager: LocationManager? = null

    private val pathSet = mutableSetOf<PathOverlay>()
    private val markerSet = mutableSetOf<Marker>()

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<LandmarkViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LandmarkFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@LandmarkFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListener()
        setupOnBackPressed()
        setupLocationManager()
        setupMap()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> setupLocation()
        }
    }

    override fun onDestroyView() {
        locationManager?.removeUpdates(this)
        super.onDestroyView()
    }

    override fun onMapReady(map: NaverMap) {
        this.map = map
        setupLocation()
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            onBackPressed()
        })
        viewModel.directionEvent.observe(viewLifecycleOwner, EventObserver {
            drawPathAndMarker(it?.route?.traoptimal?.get(0)?.path)
            expandBottomSheet()
        })
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBackPressed()
        }
    }

    private fun setupPagerAdapter(location: Location?) {
        if (!isAdded) return
        with(binding.landmarkPager) {
            adapter = LandmarkPagerAdapter(childFragmentManager, location)
            this.setPadding(10.px, 0, 30.px, 0)
            this.pageMargin = 10.px
        }
    }

    private fun setupLocationManager() {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }

    private fun setupMap() {
        with(childFragmentManager) {
            val northWest = LatLng(31.43, 122.37)
            val southEast = LatLng(44.35, 132.0)
            val options = NaverMapOptions()
                .mapType(NaverMap.MapType.Navi)
                .minZoom(5.0)
                .maxZoom(18.0)
                .extent(LatLngBounds(northWest, southEast))
                .nightModeEnabled(PreferenceUtils.useDarkMode)
            val id = R.id.landmark_map
            findFragmentById(id) as? MapFragment
                ?: MapFragment.newInstance(options).also {
                    beginTransaction().add(id, it).commit()
                }
        }.also {
            it.getMapAsync(this)
        }
    }

    private fun setupLocation() {
        if (checkPermission()) {
            syncLocation()
        }
    }

    private fun checkPermission(): Boolean {
        val activity = activity ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CODE_PERMISSION
            )
            false
        } else {
            true
        }
    }

    private fun syncLocation() {
        try {
            locationManager
                ?.also {
                    it.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0L,
                        0f,
                        this
                    )
                }
                ?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?.let {
                    setupPagerAdapter(it)
                    if (::map.isInitialized) {
                        CameraUpdate
                            .scrollTo(LatLng(it.latitude, it.longitude))
                            .also { update -> map.moveCamera(update) }
                    }
                }
                ?: throw RuntimeException()
        } catch (e: SecurityException) {
            viewModel.handleSyncFailure()
        } catch (e: RuntimeException) {
            viewModel.invalidLocation()
        }
    }

    private fun drawPathAndMarker(path: List<List<Double>>?) {
        clearPathAndMarker()
        if (::map.isInitialized) {
            val context = context ?: return
            val start = path?.firstOrNull() ?: return
            val end = path.lastOrNull() ?: return
            val startCoords = LatLng(start[1], start[0])
            val endCoords = LatLng(end[1], end[0])
            PathOverlay().apply {
                color = ContextCompat.getColor(context, R.color.colorSecondary)
                coords = path
                    .map { LatLng(it[1], it[0]) }
                    .toMutableList()
                map = this@LandmarkFragment.map
                pathSet.add(this)
            }
            Marker().apply {
                position = startCoords
                map = this@LandmarkFragment.map
                markerSet.add(this)
            }
            Marker().apply {
                position = endCoords
                map = this@LandmarkFragment.map
                markerSet.add(this)
            }
            CameraUpdate
                .fitBounds(
                    LatLngBounds(
                        startCoords,
                        endCoords
                    ),
                    50.px
                )
                .animate(CameraAnimation.Fly, CAMERA_MOVE_ANIMATION_DURATION)
                .also { map.moveCamera(it) }
        }
    }

    private fun expandBottomSheet() {
        with(BottomSheetBehavior.from(binding.landmarkBottomSheet.landmarkBottomParent)) {
            state = BottomSheetBehavior.STATE_EXPANDED
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, offset: Float) {
                    val bottom =
                        displayHeight() - binding.landmarkBottomSheet.landmarkBottomParent.top
                    map.setContentPadding(0, 0, 0, bottom)
                }

                override fun onStateChanged(view: View, newState: Int) {

                }
            })
        }
    }

    private fun clearPathAndMarker() {
        for (marker in markerSet) marker.map = null
        for (path in pathSet) path.map = null
        markerSet.clear()
        pathSet.clear()
    }

    private fun onBackPressed() {
        val behavior =
            BottomSheetBehavior.from(binding.landmarkBottomSheet.landmarkBottomParent)
        if (behavior != null && behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            findNavController().popBackStack()
        }
    }
}
