package ie.wit.shoppingapp.ui.addProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreModel

class ProductsViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    fun addProduct(product: StoreModel) {
        status.value = try {
            StoreManager.create(product)
            true
        }catch (e: IllegalArgumentException) {
            false
        }
    }
}