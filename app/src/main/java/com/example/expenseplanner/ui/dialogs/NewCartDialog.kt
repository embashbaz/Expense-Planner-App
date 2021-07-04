package com.example.expenseplanner.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.ui.expenselist.ExpenseListViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class NewCartDialog (viewModel: ExpenseListViewModel): DialogFragment(), AdapterView.OnItemSelectedListener{

    val viewModel = viewModel
    val otherType = activity?.let { TextInputLayout(it) }
    lateinit var typeSpinner: Spinner
    var itemSpinner = ""



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

       return activity?.let {
            val builder = AlertDialog.Builder(it)

           val inflater = requireActivity().layoutInflater;
           val view = inflater.inflate(R.layout.new_cart_dialog, null)
           typeSpinner = view.findViewById(R.id.cart_type_spinner)
           typeSpinner.onItemSelectedListener = this



           builder.setView(view)
               // Add action buttons
               .setPositiveButton("Save",
                   DialogInterface.OnClickListener { dialog, id ->
                       saveDataToDb(view)
                   })
               .setNegativeButton("Cancel",
                   DialogInterface.OnClickListener { dialog, id ->
                       getDialog()?.cancel()
                   })
           builder.create()


       } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveDataToDb(view: View?) {
        if(!itemSpinner.isEmpty()){
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val date =sdf.format(Date()).toString()

            val cart = Cart(0,itemSpinner,1,date,0.0)

        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            itemSpinner = parent.getItemAtPosition(position).toString()
            if (itemSpinner.equals("Other")){
                otherType?.visibility = View.VISIBLE
                itemSpinner = otherType.toString()
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}