//package ie.wit.shoppingapp.models
//import timber.log.Timber
//
//var lastId = 0L
//
//internal fun getId(): Long {
//    return lastId++
//}
//object StoreManager : StoreStore {
//
//    val products = ArrayList<StoreModel>()
//
//    override fun findAll(): List<StoreModel> {
//        return products
//    }
//
//    override fun findById(id:Long) : StoreModel? {
//        val foundProduct: StoreModel? = products.find { it.id == id }
//        return foundProduct
//    }
//
//    override fun create(product: StoreModel) {
//        product.id = getId()
//        products.add(product)
//        logAll()
//    }
//
//    fun logAll() {
//        Timber.v("**Products List **")
//        products.forEach { Timber.v("Products ${it}") }
//    }
//}