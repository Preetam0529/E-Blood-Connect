//package com.php.ebloodconnect.donor
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.mapbox.maps.MapView
//import com.mapbox.maps.Style
//import com.mapbox.maps.plugin.locationcomponent.location
//import com.mapbox.navigation.core.MapboxNavigation
//import com.mapbox.navigation.ui.maps.NavigationMapRoute
//import com.mapbox.geojson.Point
//import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
//import com.mapbox.navigation.ui.maps.route.NavigationMapRoute
//import com.mapbox.maps.Mapbox
//import com.mapbox.maps.plugin.Plugin.Mapbox
//import com.mapbox.maps.plugin.animation.camera
//import com.mapbox.maps.plugin.locationcomponent.LocationComponentOptions
//import com.php.ebloodconnect.R
//
//class DonorMapFragment : Fragment() {
//
//    private lateinit var mapView: MapView
//    private lateinit var mapboxNavigation: MapboxNavigation
//    private lateinit var navigationMapRoute: NavigationMapRoute
//    private lateinit var locationComponent: LocationComponentPlugin
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val binding = FragmentDonorMapBinding.inflate(inflater, container, false)
//
//        mapView = binding.mapView
//
//        // Initialize the Mapbox instance
//        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))
//        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
//
//        // Initialize Mapbox Navigation
//        mapboxNavigation = MapboxNavigation(requireContext(), getString(R.string.mapbox_access_token))
//
//        // Initialize the NavigationMapRoute
//        navigationMapRoute = NavigationMapRoute(null, mapView, mapboxNavigation)
//
//        // Initialize Location component
//        locationComponent = mapView.location
//        locationComponent.updateSettings(LocationComponentOptions.Builder(requireContext()).build())
//
//        // Show user's location on the map
//        locationComponent.location?.let {
//            mapView.camera.flyTo(it)
//        }
//
//        // Example acceptor locations (latitude, longitude)
//        val acceptorLocations = listOf(
//            Point.fromLngLat(77.216721, 28.613939), // Example: Delhi
//            Point.fromLngLat(78.962883, 20.593684)  // Example: India center
//        )
//
//        // Display markers for acceptors
//        acceptorLocations.forEach { location ->
//            mapView.getMapboxMap().getStyle { style ->
//                style.addImage(
//                    "acceptor_marker",
//                    BitmapFactory.decodeResource(resources, R.drawable.ic_location)
//                )
//
//                style.addSource(GeoJsonSource("acceptors") {
//                    it.withProperties(
//                        PropertyFactory.iconImage("acceptor_marker")
//                    )
//                })
//                acceptorLocations.forEach { acceptor ->
//                    val point = Point.fromLngLat(acceptor.longitude(), acceptor.latitude())
//                    style.addLayer(SymbolLayer("acceptor-markers", "acceptors")
//                        .withProperties(PropertyFactory.iconImage("acceptor_marker"))
//                    )
//                }
//            }
//        }
//
//        // When an acceptor is selected (clicking on the marker), draw the route
//        mapView.getMapboxMap().addOnMapClickListener { point ->
//            val selectedAcceptor = acceptorLocations.firstOrNull {
//                it.latitude() == point.latitude() && it.longitude() == point.longitude()
//            }
//            selectedAcceptor?.let {
//                val routeRequest = mapboxNavigation.requestRoute(
//                    RouteOptions.Builder()
//                        .apply {
//                            coordinates(
//                                listOf(
//                                    Point.fromLngLat(it.longitude(), it.latitude()), // Donor's location
//                                    Point.fromLngLat(77.216721, 28.613939) // Destination location
//                                )
//                            )
//                        }
//                )
//                mapboxNavigation.startNavigation(routeRequest)
//                true
//            } ?: false
//        }
//
//        return binding.root
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mapView.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mapView.onStop()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mapView.onDestroy()
//        mapboxNavigation.onDestroy()
//    }
//}
