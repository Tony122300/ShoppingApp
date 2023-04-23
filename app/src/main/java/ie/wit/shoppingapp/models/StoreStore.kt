package ie.wit.shoppingapp.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface StoreStore {
    fun findAll() : List<StoreModel>
    fun findById(id: Long) : StoreModel?
    fun create(store: StoreModel)
    fun findByCat(category: String): StoreModel?
}
//interface StoreStore {
//    fun findAll(productList: MutableLiveData<List<StoreModel>>)
//    fun findAll(userid:String, productList: MutableLiveData<List<StoreModel>>)
//    fun findById(userid:String, productid: String, product: MutableLiveData<StoreModel>)
//    fun create(firebaseUser: MutableLiveData<FirebaseUser>, product: StoreModel)
//}