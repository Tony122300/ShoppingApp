package ie.wit.shoppingapp.ui.addProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.shoppingapp.main.StoreApp
import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreModel
import timber.log.Timber

class ProductsViewModel : ViewModel() {
  //  lateinit var app : StoreApp
    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status


    fun addProduct(product: StoreModel) {
        status.value = try {
            StoreJSONStore.create(product)
//            StoreManager.create(product)
            true
        }catch (e: IllegalArgumentException) {
            Timber.e(e,"ADD ERROR")
            false

        }
    }


//    fun addProduct(firebaseUser: MutableLiveData<FirebaseUser>, product: StoreModel) {
//        Timber.i("Attempting to add product: $product")
//        status.value = try {
//            FireBaseDBManager.create(firebaseUser, product)
//            true
//        } catch (e: IllegalArgumentException) {
//            Timber.e(e, "Error adding product: $product")
//            false
//        }
//    }


}