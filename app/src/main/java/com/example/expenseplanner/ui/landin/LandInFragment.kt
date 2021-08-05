package com.example.expenseplanner.ui.landin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.ui.cart.CartViewModel
import com.example.expenseplanner.ui.cart.CartViewModelFactory
import com.example.expenseplanner.ui.dialogs.LoginDialog
import com.example.expenseplanner.ui.dialogs.NoticeDialogFragment
import com.example.expenseplanner.ui.register.RegistrationViewModel


class LandInFragment : Fragment(), LoginDialog.LoginDialogListener{
    var uId = ""
  val landinViewModel : LandinViewModel by lazy {
      ViewModelProvider(this).get(LandinViewModel::class.java)
  }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_land_in, container, false)
        val goToExpense = view.findViewById<CardView>(R.id.go_expense_card)
        val goToMap = view.findViewById<CardView>(R.id.go_to_shops_map)
        val goToOrders = view.findViewById<CardView>(R.id.go_to_orders)


        goToExpense.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("code", 1)
            this.findNavController().navigate(R.id.action_landInFragment_to_expenseListFragment, bundle)
        }

        goToMap.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("order_passed", 2)
            this.findNavController().navigate(R.id.action_landInFragment_to_mapsFragment, bundle)
        }

        goToOrders.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt("code", 2)
            this.findNavController().navigate(R.id.action_landInFragment_to_expenseListFragment, bundle)

        }

        view.findViewById<CardView>(R.id.go_to_profile).setOnClickListener{
            openLoginDialog()
        }


        return view
    }

    fun openLoginDialog(){
        val dialog = LoginDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Please login")

    }
    override fun onLoginBtClick(email: String, password: String) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty() ) {
            landinViewModel.login(email, password)

            landinViewModel.loginOutput.observe(viewLifecycleOwner, {
                Toast.makeText(activity, it["status"] + ": " + it["value"], Toast.LENGTH_LONG)
                    .show()
                if (it["status"] == "success") {
                    (activity?.application as ExpensePlanner).uId = it["value"].toString()
                    uId = it["value"].toString()
                    Toast.makeText(activity,"You have been logged in successfully", Toast.LENGTH_SHORT).show()
                    val bundle = Bundle()
                    bundle.putInt("code", 2)
                    this.findNavController().navigate(R.id.action_landInFragment_to_registerFragment, bundle)

                }else if(it["status"] == "failed"){
                    openNoticeDialog(it["value"]!!, "Error")
                }
            })
        }
    }

    override fun onRegisterBtClick() {
        this.findNavController().navigate(R.id.action_landInFragment_to_registerFragment)
    }

    override fun onForgotPasswdClick(email: String) {
        TODO("Not yet implemented")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }

    fun openNoticeDialog(messageText: String, tag: String){
        val dialog = NoticeDialogFragment(messageText)
        dialog.show(parentFragmentManager, tag)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.landin_menu, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout_menu){
            (activity?.application as ExpensePlanner).uId = ""
        }

        return super.onOptionsItemSelected(item)
    }

}