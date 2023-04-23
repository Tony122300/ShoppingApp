package ie.wit.shoppingapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreModel
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {
    private val productList = MutableLiveData<List<StoreModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val observableProductsList: LiveData<List<StoreModel>>
        get() = productList

    init {
        load()
    }

    fun load() {
        productList.value = StoreJSONStore.findAll()
    }

//    fun load() {
//        try {
//            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
//            FireBaseDBManager.findAll(liveFirebaseUser.value?.uid!!,productList)
//            Timber.i("Report Load Success : ${productList.value.toString()}")
//        }
//        catch (e: Exception) {
//            Timber.i("Report Load Error : $e.message")
//        }
//    }

    fun filterList(query: String) {
        val results = mutableListOf<StoreModel>()

        for (product in StoreJSONStore.findAll()) {
            if (product.productName.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true)) {
                results.add(product)
            }
        }


        productList.value = results
    }
}