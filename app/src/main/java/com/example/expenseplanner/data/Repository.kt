package com.example.expenseplanner.data

import android.content.ContentValues
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow


class Repository(mExpenseDao: ExpenseDao? = null)  {

    var mFirebaseAuth: FirebaseAuth
    var mFirebaseDb : FirebaseFirestore
    val  expenseDao: ExpenseDao? = mExpenseDao


    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDb = FirebaseFirestore.getInstance()

    }

    val allCarts = expenseDao?.getAllCart()

    fun getItemsForCart(id: Int): LiveData<List<ItemProduct>>{
        return expenseDao!!.getAllItemForCart(id)
    }

    fun getActiveCartIdIfExist(statusParam: Int, param2: String): LiveData<List<Cart>>{
        return  expenseDao!!.getActiveCartIdIfExist(statusParam, param2)
    }

    @WorkerThread
    suspend fun insertExpense(cart: Cart): Long{
        return expenseDao!!.insertExpense(cart)
    }

    @WorkerThread
    suspend fun insertItemProduct(item: ItemProduct){
        expenseDao!!.insertItemProduct(item)
    }

    @WorkerThread
    suspend fun updateCart(cart: Cart){
        expenseDao!!.updateCart(cart)
    }

    @WorkerThread
    suspend fun updateItemProduct(item: ItemProduct){
        expenseDao!!.updateItemProduct(item)
    }

    @WorkerThread
    suspend fun deleteCart(cart: Cart){
        expenseDao!!.deleteCart(cart)
        expenseDao.deleteAllItemForCart(cart.id)
    }

    @WorkerThread
    suspend fun deleteItemProduct(item: ItemProduct){
        expenseDao!!.deleteItemProduct(item)
    }

    fun getShops(): MutableLiveData<List<ShopKeeper>> {
        val data = MutableLiveData<List<ShopKeeper>>()

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
                data.value = emptyList()
            }

        return data



    }

    fun placeOrder(order: Order): MutableLiveData<HashMap<String, String>>{
        val operationOutput = MutableLiveData<HashMap<String, String>>()

        var status = hashMapOf<String, String>()
        mFirebaseDb.collection("shops").document(order.shopId).collection("orders").add(order)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot written")
                status.put("status", "success")
                status.put("value","Order Placed" )

            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
                status.put("status", "failed")
                status.put("value",e.toString() )
            }

        operationOutput.postValue(status)


        return operationOutput
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

    fun getShopProducts(uId:String): MutableLiveData<List<ShopProduct>>{

        val data = MutableLiveData<List<ShopProduct>>()

        val productRef = mFirebaseDb.collection("shops").document(uId).collection("products")

        productRef
            .get()
            .addOnSuccessListener {

                val dataList = ArrayList<ShopProduct>()
                for (snapshot in it){

                    val docObject = snapshot.toObject(ShopProduct::class.java)
                    docObject.docId = snapshot.id

                    dataList.add(docObject)
                }
                data.value = dataList

            }.addOnFailureListener {
                data.value = emptyList()
            }

        return data
    }

    fun getShopOrders(uId:String): MutableLiveData<List<Order>>{

        val data = MutableLiveData<List<Order>>()

        val productRef = mFirebaseDb.collectionGroup("orders")

        productRef
            .whereEqualTo("userId", uId)
            .get()
            .addOnSuccessListener {

                val dataList = ArrayList<Order>()
                for (snapshot in it){

                    val docObject = snapshot.toObject(Order::class.java)
                    if(docObject.id.isNullOrEmpty())
                    docObject.id = snapshot.id

                    dataList.add(docObject)
                }
                data.value = dataList

            }.addOnFailureListener {
                Log.e("ERROR", it.toString())
                data.value = emptyList()
            }

        return data
    }

    fun getShop(uId: String): MutableLiveData<ShopKeeper?>{
        val data = MutableLiveData<ShopKeeper?>()

        val shopRef = mFirebaseDb.collection("shops").document(uId)

        shopRef
            .get()
            .addOnSuccessListener {

                data.value = it.toObject<ShopKeeper>()

            }.addOnFailureListener {
                data.value = null
            }

        return data
    }

    fun getUser(uId: String): MutableLiveData<GeneralUser?>{
        val data = MutableLiveData<GeneralUser?>()

        val userRef = mFirebaseDb.collection("users").document(uId)

        userRef
            .get()
            .addOnSuccessListener {

                data.value = it.toObject<GeneralUser>()

            }.addOnFailureListener {
                data.value = null
            }

        return data
    }



}