package ie.wit.shoppingapp.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface StoreStore {
    fun findAll() : List<StoreModel>
    fun findById(id: Long) : StoreModel?
    fun create(store: StoreModel)
    fun findByCat(category: String): StoreModel?
}
