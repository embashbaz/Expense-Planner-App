package com.example.expenseplanner.ui.landin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseplanner.data.Repository

class LandinViewModel : ViewModel() {

    val repository = Repository()

    private var _loginOutput = MutableLiveData<HashMap<String, String>>()
    val loginOutput: LiveData<HashMap<String, String>>
        get() = _loginOutput
    fun login (email: String, password: String){
        _loginOutput = repository.login(email, password)
    }
}