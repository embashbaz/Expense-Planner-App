package com.example.expenseplanner.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.expenseplanner.R
import com.google.android.material.textfield.TextInputLayout

class LoginDialog: DialogFragment() {

    internal lateinit var listener: LoginDialogListener


    lateinit var emailTl: TextInputLayout
    lateinit var passwordTl: TextInputLayout
    lateinit var forgotPwdBt : Button
    lateinit var registerBt : Button
    lateinit var loginBt : Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.login_dialog, null)

            emailTl = view.findViewById(R.id.email_login)
            passwordTl = view.findViewById(R.id.password_login)
            forgotPwdBt = view.findViewById(R.id.forgot_password)
            registerBt = view.findViewById(R.id.register_login)
            loginBt = view.findViewById(R.id.login_button)


            loginBt.setOnClickListener{
                listener.onLoginBtClick(emailTl.editText?.text.toString(),
                    passwordTl.editText?.text.toString())
                dialog!!.dismiss()
            }

            registerBt.setOnClickListener{
                listener.onRegisterBtClick()
                dialog!!.dismiss()
            }

            forgotPwdBt.setOnClickListener{

                listener.onForgotPasswdClick(emailTl.editText?.text.toString())
                dialog!!.dismiss()
            }

            builder.setView(view)
                .setNegativeButton("Cancel"
                ) { dialog, id ->
                    // Send the negative button event back to the host activity
                    listener.onDialogNegativeClick(this)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    interface LoginDialogListener{

        fun onLoginBtClick(email: String, password: String)
        fun onRegisterBtClick()
        fun onForgotPasswdClick(email: String)
        fun onDialogNegativeClick(dialog: DialogFragment)



    }

    fun setListener(listener: LoginDialogListener) {
        this.listener = listener
    }
}




