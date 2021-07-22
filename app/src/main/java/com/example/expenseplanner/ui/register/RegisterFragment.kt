package com.example.expenseplanner.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.GeneralUser
import com.google.android.material.textfield.TextInputLayout


class RegisterFragment : Fragment() {

    lateinit var nameTl: TextInputLayout
    lateinit var emailTl: TextInputLayout
    lateinit var phoneNumberTl: TextInputLayout
    lateinit var passwordTl: TextInputLayout
    lateinit var confirmPasswordTl: TextInputLayout
    lateinit var addressTl: TextInputLayout
    lateinit var cityTl: TextInputLayout
    lateinit var registerBt: Button

    val registrationViewModel : RegistrationViewModel by lazy {
        ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view  =  inflater.inflate(R.layout.fragment_register, container, false)
        setViews(view)

        registerBt.setOnClickListener{
            saveNewUser()
        }

        return view
    }

    fun setViews(view: View){
        nameTl = view.findViewById(R.id.name_register)
        emailTl = view.findViewById(R.id.email_register)
        phoneNumberTl = view.findViewById(R.id.phone_register)
        passwordTl = view.findViewById(R.id.password_register)
        confirmPasswordTl = view.findViewById(R.id.confirm_password)
        cityTl = view.findViewById(R.id.city_adress)
        registerBt = view.findViewById(R.id.register_button)
        addressTl = view.findViewById(R.id.address_tl)
    }
    private fun saveNewUser() {

        if(!nameTl.editText?.text.isNullOrEmpty() || !emailTl.editText?.text.isNullOrEmpty() || !passwordTl.editText?.text.isNullOrEmpty() ||
            !addressTl.editText?.text.isNullOrEmpty()
            || !cityTl.editText?.text.isNullOrEmpty()){
            if(passwordTl.editText?.text.toString() == confirmPasswordTl.editText?.text.toString()){

                    val user = GeneralUser("",emailTl.editText?.text.toString(),nameTl.editText?.text.toString() ,phoneNumberTl.editText?.text.toString().toLong(),
                         addressTl.editText?.text.toString(), cityTl.editText?.text.toString())

                    registrationViewModel.signUp(user, passwordTl.editText?.text.toString())

                    registrationViewModel.registrationOutput.observe(viewLifecycleOwner,{

                        Toast.makeText(activity, it["status"]+": "+it["value"], Toast.LENGTH_LONG).show()
                        if(it["status"] == "success"){
                            (activity?.application as ExpensePlanner).uId = it["value"].toString()
                        }
                    })
                    }


            else{
                Toast.makeText(activity, "Please make sure the two password are the same", Toast.LENGTH_LONG).show()
            }

        }else {
            Toast.makeText(activity, "Please make sure you fill all the field, phone number and description are not mandatory", Toast.LENGTH_LONG).show()
        }
     }

}