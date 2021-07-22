package com.example.expenseplanner.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseplanner.data.GeneralUser
import com.example.expenseplanner.data.Repository

class RegistrationViewModel : ViewModel() {

    val repository = Repository()


    private var _registrationOutput = MutableLiveData<HashMap<String, String>>()
    val registrationOutput: LiveData<HashMap<String, String>>
        get() = _registrationOutput


    fun signUp (user: GeneralUser, password: String){
        _registrationOutput = repository.register(user, password)
    }



}