package com.example.expenseplanner.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.data.ShopKeeper
import com.example.expenseplanner.ui.dialogs.LoginDialog
import com.example.expenseplanner.ui.dialogs.NoticeDialogFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsFragment : Fragment(), LoginDialog.LoginDialogListener {

    lateinit var mGoogleMap: GoogleMap
    lateinit var order: Order
    val nextActionCode = 0

    val uId : String by lazy {  ( activity?.application as ExpensePlanner).uId }

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


        getShopLocations()

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
                            //.icon()


                    ))
                    mGoogleMap.setOnInfoWindowClickListener {

                        if(nextActionCode == 1){

                        }else if(nextActionCode == 2){
                            goToProductList(shop.id)

                        }


                    }


                  }

                }

             })

        }

    fun placeOrder(shop: ShopKeeper) {
        order.shopId = shop.id
        order.shopName = shop.name
        (activity?.application as ExpensePlanner).order = order

        if(!uId.isNullOrEmpty()){


        }else{
            openLoginDialog()
        }


    }

    fun sendOrder(){
        val finalOrder = (activity?.application as ExpensePlanner).order!!
        finalOrder.userId = uId
        finalOrder.userName = FirebaseAuth.getInstance().currentUser?.displayName.toString()

        mapViewModel.placeOrder(finalOrder)
        mapViewModel.placeOrderOutput.observe(viewLifecycleOwner, {
            if(it["status"] == "success"){
                openNoticeDialog("Your order has been placed", "success")

            }else if(it["status"] == "failed"){
                openNoticeDialog(it["value"]!!, "Error Placing order")
            }

        })



    }

    fun openNoticeDialog(messageText: String, tag: String){
        val dialog = NoticeDialogFragment(messageText)
        dialog.show(parentFragmentManager, tag)

    }

    fun openLoginDialog(){
        val dialog = LoginDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Please login")

    }

    fun goToProductList(shopId: String){
        val bundle = Bundle()
        bundle.putString("shopId", shopId)
        this.findNavController().navigate(R.id.action_mapsFragment_to_productListFragment)

    }

    override fun onLoginBtClick(email: String, password: String) {
        if (email.isNullOrEmpty() && !password.isNullOrEmpty() ) {
            mapViewModel.signUp(email, password)

            mapViewModel.loginOutput.observe(viewLifecycleOwner, {
                Toast.makeText(activity, it["status"] + ": " + it["value"], Toast.LENGTH_LONG)
                    .show()
                if (it["status"] == "success") {
                    (activity?.application as ExpensePlanner).uId = it["value"].toString()
                   }
            })
        }
    }

    override fun onRegisterBtClick() {
        this.findNavController().navigate(R.id.action_mapsFragment_to_registerFragment)
    }

    override fun onForgotPasswdClick(email: String) {
        TODO("Not yet implemented")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
       dialog.dismiss()
    }


}