package ie.wit.shoppingapp.models
import android.net.Uri


data class StoreModel(
    var id: Long = 0,
    var productName: String = "",
    var productDescription: String = "",
    var bestBeforeDate: Long = 0,
    var price: Double = 0.00,
    var image: Uri = Uri.EMPTY,
    var category: String = "",
    val email: String = "tony@gmail.com",
    val barcode: String = "",
    val asile: Int = 0,
    val producer: String = ""
)


//{
//    @Exclude
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "uid" to uid,
//            "productName" to productName,
//            "productDescription" to productDescription,
//            "bestBeforeDate" to bestBeforeDate,
//            "price" to price,
//            "email" to email
//        )
//    }
//}