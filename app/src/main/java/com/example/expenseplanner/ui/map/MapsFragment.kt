package com.example.expenseplanner.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expenseplanner.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsFragment : Fragment() {

    lateinit var mGoogleMap: GoogleMap
    val nextActionCode = 0

    val mapViewModel: MapViewModel by lazy {
        ViewModelProvider(this).get(MapViewModel::class.java)
    }


    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun getShopLocations(){
        mapViewModel.listShops.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                val marker = ArrayList<Marker>()
                for(shop in it){
                    val location = shop.adress
                    marker.add(mGoogleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(shop.name)
                            .icon(//To)
                            )


                    ))
                    mGoogleMap.setOnInfoWindowClickListener {

                        if(nextActionCode == 1){

                        }else if(nextActionCode == 2){


                        }


                      }


                  }

                }

             })

        }

    fun goToProductList(shopId: String){
        val bundle = Bundle()
        bundle.putString("shopId", shopId)
        this.findNavController().navigate(R.id.action_mapsFragment_to_productListFragment)

    }
}