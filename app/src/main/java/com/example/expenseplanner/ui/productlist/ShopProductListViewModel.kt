package com.example.expenseplanner.ui.productlist

import androidx.lifecycle.*
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.data.Repository
import com.example.expenseplanner.data.ShopProduct
import kotlinx.coroutines.launch

class ShopProductListViewModel(pRepository: Repository): ViewModel() {
    val repository = pRepository

    private var _shopListProduct = MutableLiveData<List<ShopProduct>>()
    val shopListProduct : LiveData<List<ShopProduct>>
        get() = _shopListProduct

    var _newCartId = MutableLiveData<Long>()
    val  newCartId: LiveData<Long>
    get() = _newCartId

    fun getListShopProduct(uId: String){
        _shopListProduct = repository.getShopProducts(uId)
    }

    fun getActiveCartIdIfExist(statusParam: Int, param2: String): LiveData<List<Cart>>{
        return  repository.getActiveCartIdIfExist(statusParam, param2)
    }

    fun insertCart(cart: Cart) = viewModelScope.launch{
        _newCartId!!.value = repository.insertExpense(cart)!!
    }

    fun insertItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.insertItemProduct(item)

    }

}

class ShopProductListViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShopProductListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}