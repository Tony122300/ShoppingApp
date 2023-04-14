package ie.wit.shoppingapp.models


import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.shoppingapp.helperFiles.exists
import ie.wit.shoppingapp.helperFiles.read
import ie.wit.shoppingapp.helperFiles.write
//import ie.wit.shoppingapp.models.StoreManager.logAll
import timber.log.Timber
import java.lang.reflect.Type
import java.nio.file.Files.exists
import java.util.*

const val JSON_FILE = "Store.JSON"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<StoreModel>>() {}.type


fun generateRandomId(): Long {
    return Random().nextLong()
}

object StoreJSONStore : StoreStore {
    private lateinit var context: Context

    var products = mutableListOf<StoreModel>()

    fun initialize(context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<StoreModel> {
        logAll()
        return products
    }

    override fun findById(id: Long): StoreModel? {
        val foundProduct: StoreModel? = products.find { it.id == id }
        return foundProduct
    }

    override fun create(product: StoreModel) {
        product.id = generateRandomId()
        products.add(product)
        serialize()
    }

    fun delete(product: StoreModel) {
        products.remove(product)
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(products, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        products = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        products.forEach { Timber.i("$it") }
    }


}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}