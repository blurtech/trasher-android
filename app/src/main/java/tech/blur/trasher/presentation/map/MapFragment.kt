package tech.blur.trasher.presentation.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.blur.trasher.R
import tech.blur.trasher.common.ext.observe
import tech.blur.trasher.databinding.FragmentMapBinding
import tech.blur.trasher.presentation.BaseFragment
import tech.blur.trasher.presentation.view.SupportNavigationHide


class MapFragment : BaseFragment(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMarkerClickListener,
    SupportNavigationHide {
    override var hideNavigation: ((Boolean) -> Unit)? = null

    private val mapViewModel: MapViewModel by viewModel()

    private lateinit var googleMap: GoogleMap

    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var binding: FragmentMapBinding

    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_map,
            container,
            false
        )

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_mapFragment) as SupportMapFragment

        mapViewModel.loadTrashcans.onNext(Unit)
        mapFragment.getMapAsync(this)

        googleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()

        binding.executePendingBindings()

        return binding.root
    }

    override fun onResume() {
        hideNavigation?.invoke(false)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


//        configureBackdrop()
    }

//    private fun configureBackdrop() {
//        // Get the fragment reference
//        val fragment = childFragmentManager.findFragmentById(R.id.fragment_statistic_Home)
//
//        fragment?.let {
//            // Get the BottomSheetBehavior from the fragment view
//            BottomSheetBehavior.from(it.view)?.let { bsb ->
//                // Set the initial state of the BottomSheetBehavior to HIDDEN
//                bsb.state = BottomSheetBehavior.PEEK_HEIGHT_AUTO
//
//                // Set the reference into class attribute (will be used latter)
//                mBottomSheetBehavior = bsb
//            }
//        }
//    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setPadding(0, 0, 0, 100)

        val permList =
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)

        ActivityCompat.requestPermissions(activity!!, permList, 1) //TODO Wait for permissions

        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "No Location Permissions", Toast.LENGTH_LONG).show()
        } else {
            googleMap.apply {
                isMyLocationEnabled = true
                isBuildingsEnabled = true
                uiSettings.isZoomControlsEnabled = false
            }

            getCurrentLocation()
        }

        googleMap.setOnMarkerClickListener(this)

        paintMarkers()
    }

    private fun paintMarkers() {
        mapViewModel.trashcans.observe(this) { list ->
            list?.map {
                runBlocking {
                    googleMap.addMarker(
                        MarkerOptions().apply {
                            position(
                                it.latlng
                            )
                            icon(
                                BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_map_point))
                            )
                            draggable(false)
                            title("мусорка") //it.name
                        }
                    ).tag = it.id
                }
            }
        }
    }

    fun getBitmap(@DrawableRes drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)

        return bitmap
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(activity, marker.tag.toString(), Toast.LENGTH_SHORT).show()
        return true
    }

    private fun getCurrentLocation() {
        //googleMap.clear()
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        getFusedLocationProviderClient(activity as Activity).lastLocation
            .addOnSuccessListener {
                moveMap(it.latitude, it.longitude)
            }
    }

    private fun moveMap(latitude: Double, longitude: Double) {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        val latLng = LatLng(latitude, longitude)

        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15f).build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {
        getCurrentLocation()
    }

    override fun onConnectionSuspended(p0: Int) {

    }

}