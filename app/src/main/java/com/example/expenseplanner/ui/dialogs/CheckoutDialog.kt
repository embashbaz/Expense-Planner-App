package com.example.expenseplanner.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.expenseplanner.R

class CheckoutDialog : DialogFragment(){

    internal lateinit var listener: CheckoutInterface

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Choose checkout option")
                .setItems(R.array.check_out_type,
                    DialogInterface.OnClickListener { dialog, which ->
                      listener.checkoutOptionCode(which)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setListener(listener: CheckoutInterface){
        this.listener = listener
    }


    interface CheckoutInterface{
        fun checkoutOptionCode(code: Int)
    }
}