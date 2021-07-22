package com.example.expenseplanner.data

import android.content.ContentValues
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow


class Repository(private val expenseDao: ExpenseDao) {

    var mFirebaseAuth: FirebaseAuth
    var mFirebaseDb : FirebaseFirestore

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDb = FirebaseFirestore.getInstance()

    }

    val allCarts = expenseDao.getAllCart()

    fun getItemsForCart(id: Int): LiveData<List<ItemProduct>>{
        return expenseDao.getAllItemForCart(id)
    }

    @WorkerThread
    suspend fun insertExpense(cart: Cart){
        expenseDao.insertExpense(cart)
    }

    @WorkerThread
    suspend fun insertItemProduct(item: ItemProduct){
        expenseDao.insertItemProduct(item)
    }

    @WorkerThread
    suspend fun updateCart(cart: Cart){
        expenseDao.updateCart(cart)
    }

    @WorkerThread
    suspend fun updateItemProduct(item: ItemProduct){
        expenseDao.updateItemProduct(item)
    }

    @WorkerThread
    suspend fun deleteCart(cart: Cart){
        expenseDao.deleteCart(cart)
        expenseDao.deleteAllItemForCart(cart.id)
    }

    @WorkerThread
    suspend fun deleteItemProduct(item: ItemProduct){
        expenseDao.deleteItemProduct(item)
    }

    fun getShops(): MutableLiveData<List<ShopKeeper>?> {
        val data = MutableLiveData<List<ShopKeeper>?>()

        val productRef = mFirebaseDb.collection("shops")

        productRef
            .get()
            .addOnSuccessListener {

                val dataList = ArrayList<ShopKeeper>()
                for (snapshot in it){

                    dataList.add(snapshot.toObject(ShopKeeper::class.java))
                }
                data.value = dataList

            }.addOnFailureListener {
                data.value = null
            }

        return data



    }

    fun getShopProducts(uId:String): MutableLiveData<List<ShopProduct>?> {

        val data = MutableLiveData<List<ShopProduct>?>()

        val productRef = mFirebaseDb.collection("shops").document(uId).collection("products")

        productRef
            .get()
            .addOnSuccessListener {

                val dataList = ArrayList<ShopProduct>()
                for (snapshot in it){

                    dataList.add(snapshot.toObject(ShopProduct::class.java))
                }
                data.value = dataList

            }.addOnFailureListener {
                data.value = null
            }

        return data
    }

    fun login(email: String, password: String): MutableLiveData<HashMap<String, String>>{
        val operationOutput = MutableLiveData<HashMap<String, String>>()
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                val data : HashMap<String, String> = hashMapOf()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")

                    data.put("status", "success")
                    data.put("value", mFirebaseAuth.uid.toString())

                } else {
                    data.put("Status", "Failed")
                    data.put("value",  "Operation Failed with error"+ task.exception)

                    Log.d(ContentValues.TAG, "Failed withe errt", task.exception)

                }

                operationOutput.postValue(data)

            }


        return operationOutput
    }


    fun register(user: GeneralUser, password: String): MutableLiveData<HashMap<String, String>>{

        val operationOutput = MutableLiveData<HashMap<String, String>>()
        var status = hashMapOf<String, String>()
        mFirebaseAuth.createUserWithEmailAndPassword( user.email,  password)
            .addOnCompleteListener (){ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val userId = mFirebaseAuth.uid
                    if (userId != null) {
                        user.id = userId.toString()
                    }

                    mFirebaseDb.collection("users").document(userId.toString()).set(user)
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "DocumentSnapshot written")
                            status.put("status", "success")
                            status.put("value", userId.toString())
                            operationOutput.postValue(status)

                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                            status.put("status", "Failed")
                            status.put("value",
                                "Operation Failed with error"+ e.toString()
                            )
                            operationOutput.postValue(status)
                        }

                } else {
                    status.put("status", "Failed")
                    status.put("value",
                        "Operation Failed with error"+ task.exception
                    )
                }
                operationOutput.postValue(status)

            }

        return operationOutput

    }



}