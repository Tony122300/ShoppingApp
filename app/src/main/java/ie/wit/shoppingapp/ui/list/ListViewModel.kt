package ie.wit.shoppingapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreModel

class ListViewModel : ViewModel() {
    private val productList = MutableLiveData<List<StoreModel>>()

    val observableProductsList: LiveData<List<StoreModel>>
        get() = productList

    init {
        load()
    }

    fun load() {
        productList.value = StoreJSONStore.findAll()
    }
}