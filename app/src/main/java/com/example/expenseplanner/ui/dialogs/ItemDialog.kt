package com.example.expenseplanner.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.expenseplanner.R
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.ui.cart.CartViewModel

class ItemDialog(code: Int, viewModel: CartViewModel, itemProduct: ItemProduct?) : DialogFragment() {

    val mCode = code

    var itemName = ""
    var itemPrice = 0.0
    var numberItem = 0
    var description = ""
    var totalPrice = 0.0

    lateinit var itemTotal: TextView
    lateinit var saveBt: Button
    lateinit var ignoreBt: Button


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.product_dialog, null)
            itemTotal = view.findViewById(R.id.items_total_price)
            saveBt = view.findViewById(R.id.save_item_bt)
            ignoreBt =view.findViewById(R.id.ignore_item_bt)





            builder.setView(view)
                               .setNegativeButton("Cancel"
                               ) { dialog, id ->
                                   dismissDialog()
                               }
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getData(){

        itemName = ""
        itemPrice = 0.0
        numberItem = 0
        var description = ""
        var totalPrice = 0.0

    }

    private fun saveData(){
        getData()


    }

    private fun updateData(){
        getData()

    }

    private fun setData(){

    }

    private fun clearData(){

    }


    private fun dismissDialog(){
        getDialog()?.cancel()
    }

    private fun deleteData(){

    }

    private fun differentCode(){
        if(mCode ==1){
            saveBt.setOnClickListener {
                saveData()

            }

            ignoreBt.setOnClickListener{
                dismissDialog()
            }


        }else if(mCode == 2){
            setData()
            saveBt.text = "UPDATE"
            ignoreBt.text = "DELETE"

            saveBt.setOnClickListener {
               updateData()

            }

            ignoreBt.setOnClickListener{
                deleteData()
            }

        }else if(mCode == 3){


        }

    }


}