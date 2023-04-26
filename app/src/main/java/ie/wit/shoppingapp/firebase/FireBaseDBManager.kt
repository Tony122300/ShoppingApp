//import androidx.lifecycle.MutableLiveData
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.*
//
//import ie.wit.shoppingapp.models.StoreModel
//import ie.wit.shoppingapp.models.StoreStore
//import timber.log.Timber
//
//object FireBaseDBManager : StoreStore {
//    var database: DatabaseReference = FirebaseDatabase.getInstance("https://shoppingapp-496b2-default-rtdb.europe-west1.firebasedatabase.app/").reference
//
//    // ref = Database.database("https://<databaseName><region>.firebasedatabase.app")
//    override fun findAll(productList: MutableLiveData<List<StoreModel>>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun findAll(userid: String, productList: MutableLiveData<List<StoreModel>>) {
//        database.child("user-products").child(userid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                    Timber.i("Firebase store error : ${error.message}")
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val localList = ArrayList<StoreModel>()
//                    val children = snapshot.children
//                    children.forEach {
//                        val product = it.getValue(StoreModel::class.java)
//                        localList.add(product!!)
//                    }
//                    database.child("user-products").child(userid)
//                        .removeEventListener(this)
//
//                    productList.value = localList
//                }
//            })
//    }
//
//    override fun findById(userid: String, productid: String, product: MutableLiveData<StoreModel>) {
//        database.child("user-products").child(userid)
//            .child(productid).get().addOnSuccessListener {
//                product.value = it.getValue(StoreModel::class.java)
//                Timber.i("firebase Got value ${it.value}")
//            }.addOnFailureListener{
//                Timber.e("firebase Error getting data $it")
//            }
//    }
//
//    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, product: StoreModel) {
//        Timber.i("Firebase DB Reference : $database")
//
//        val uid = firebaseUser.value!!.uid
//        val key = database.child("products").push().key
//        if (key == null) {
//            Timber.i("Firebase Error : Key Empty")
//            return
//        }
//        product.uid = key
//        val productValues = product.toMap()
//
//        val childAdd = HashMap<String, Any>()
//        childAdd["/products/$key"] = productValues
//        childAdd["/user-products/$uid/$key"] = productValues
//
//        database.updateChildren(childAdd)
//    }
//
//}
