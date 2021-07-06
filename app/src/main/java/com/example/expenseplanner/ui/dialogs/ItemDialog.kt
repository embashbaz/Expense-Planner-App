package com.example.expenseplanner.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.expenseplanner.R
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.ui.cart.CartViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class ItemDialog(code: Int, viewModel: CartViewModel,id: Int ,itemProduct: ItemProduct?) : DialogFragment() {

    val mCode = code
    val cartId = id
    val cartViewModel = viewModel
    val itemProduct = itemProduct


    var itemName = ""
    var itemPrice = 0.0
    var numberItem = 0.0
    var description = ""
    var totalPrice = 0.0

    lateinit var itemTotal: TextView
    lateinit var saveBt: Button
    lateinit var ignoreBt: Button

    lateinit var itemNameTl : TextInputLayout
    lateinit var itemPriceTl : TextInputLayout
    lateinit var itemNumberTl : TextInputLayout
    lateinit var itemDescriptionTl : TextInputLayout


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.product_dialog, null)
            itemTotal = view.findViewById(R.id.items_total_price)
            saveBt = view.findViewById(R.id.save_item_bt)
            ignoreBt =view.findViewById(R.id.ignore_item_bt)
            itemNameTl = view.findViewById(R.id.item_name_til)
            itemPriceTl = view.findViewById(R.id.item_price)
            itemNumberTl = view.findViewById(R.id.number_item)
            itemDescriptionTl = view.findViewById(R.id.item_description)

            calculateTotal()
            differentCode()



            builder.setView(view)
                               .setNegativeButton("Cancel"
                               ) { dialog, id ->
                                   dismissDialog()
                               }
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getData(): Boolean{

        itemName = itemNameTl.editText?.text.toString()
        itemPrice = itemPriceTl.editText?.text.toString().toDouble()
        numberItem = itemNumberTl.editText?.text.toString().toDouble()
        description = itemDescriptionTl.editText?.text.toString()

        return !itemName.isEmpty() && numberItem>0



    }

    private fun saveData(){
        if(getData()){

            val sdf = SimpleDateFormat("dd/M/yyyy")
            val date =sdf.format(Date()).toString()

            val newItemProduct = ItemProduct(0, cartId, itemName,date, itemPrice,
                     numberItem, totalPrice, description)

            cartViewModel.insertItemProduct(newItemProduct)


        }


    }

    private fun updateData(){
        if(getData()){
            if (itemProduct != null) {
                itemProduct.name = itemName
                itemProduct.price = itemPrice
                itemProduct.totalPriceNum = totalPrice
                itemProduct.quantity = numberItem
                itemProduct.description = description

                cartViewModel.updateItemProduct(itemProduct)
            }

        }

    }

    private fun setData(){
        itemTotal.text = itemProduct?.totalPriceNum.toString()
        itemNameTl.editText?.setText(itemProduct?.name)
        itemPriceTl.editText?.setText(itemProduct?.price.toString())
        itemNumberTl.editText?.setText(itemProduct?.quantity.toString())
        itemDescriptionTl.editText?.setText(itemProduct?.description)
    }

    private fun clearData(){
        itemTotal.text = ""
        itemNameTl.editText?.setText("")
        itemPriceTl.editText?.setText("")
        itemNumberTl.editText?.setText("")
        itemDescriptionTl.editText?.setText("")
    }

    private fun calculateTotal(){
        itemNumberTl.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()){
                    totalPrice = s?.toString().toDouble() * itemPriceTl.editText?.text.toString().toDouble()
                    itemTotal.text = totalPrice.toString()
                }
            }

        })
    }


    private fun dismissDialog(){
        getDialog()?.cancel()
    }

    private fun deleteData(){
        if(itemProduct != null)
            cartViewModel.deleteItemProduct(itemProduct)
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