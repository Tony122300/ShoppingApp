package ie.wit.shoppingapp.models
import android.net.Uri


data class StoreModel ( var id: Long = 0,var productName: String = "", var productDescription: String = "", var price: Double = 0.00,var image: Uri = Uri.EMPTY)