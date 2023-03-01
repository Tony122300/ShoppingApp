package ie.wit.shoppingapp.models

interface StoreStore {
    fun findAll() : List<StoreModel>
    fun findById(id: Long) : StoreModel?
    fun create(store: StoreModel)
}