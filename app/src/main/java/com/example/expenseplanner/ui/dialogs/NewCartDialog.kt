package com.example.expenseplanner.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import com.example.expenseplanner.ui.expenselist.ExpenseListViewModel
import com.google.android.material.textfield.TextInputLayout

class NewCartDialog (viewModel: ExpenseListViewModel): DialogFragment(), AdapterView.OnItemSelectedListener{

    val viewModel = viewModel
    val otherType = activity?.let { TextInputLayout(it) }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

       // return activity?.let {
           // val builder = AlertDialog.Builder(it)
            // Get the layout inflater
         //   val inflater = requireActivity().layoutInflater;
       // }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}