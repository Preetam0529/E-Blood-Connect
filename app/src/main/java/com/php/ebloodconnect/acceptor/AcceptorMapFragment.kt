//package com.php.ebloodconnect.acceptor
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.mapbox.geojson.Point
//import com.mapbox.maps.MapView
//import com.mapbox.maps.Style
//import com.mapbox.maps.plugin.annotation.AnnotationConfig
//import com.mapbox.maps.plugin.annotation.annotations
//import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
//import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
//import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
//import com.google.firebase.firestore.FirebaseFirestore
//import com.php.ebloodconnect.R
//
//class AcceptorMapFragment : Fragment() {
//
//    private lateinit var mapView: MapView
//    private lateinit var annotationManager: PointAnnotationManager
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_acceptor_map, container, false)
//        mapView = view.findViewById(R.id.mapView)
//
//        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
//            setupAnnotationManager()
//            fetchAndShowDonors()
//        }
//
//        return view
//    }
//
//    private fun setupAnnotationManager() {
//        val annotationApi = mapView.annotations
//        annotationManager = annotationApi.createPointAnnotationManager(AnnotationConfig())
//    }
//
//    private fun fetchAndShowDonors() {
//        val firestore = FirebaseFirestore.getInstance()
//        val donorsRef = firestore.collection("donors")
//
//        donorsRef.get().addOnSuccessListener { documents ->
//            for (document in documents) {
//                val lat = document.getDouble("latitude")
//                val lon = document.getDouble("longitude")
//                val name = document.getString("name")
//
//                if (lat != null && lon != null) {
//                    addDonorMarker(lat, lon, name ?: "Donor")
//                }
//            }
//        }
//    }
//
//    private fun addDonorMarker(latitude: Double, longitude: Double, title: String) {
//        val point = Point.fromLngLat(longitude, latitude)
//
//        val pointAnnotationOptions = PointAnnotation.Options()
//            .withPoint(point)
//            .withTextField(title)
//            .withIconImage("marker-15") // default Mapbox marker icon
//
//        annotationManager.create(pointAnnotationOptions)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        mapView.onStop()
//        mapView.onDestroy()
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
//}
