package ie.wit.shoppingapp.ui.cart

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel

import ie.wit.shoppingapp.models.StoreModel
//cart

class CartViewModel : ViewModel() {

    private val productsInCart = mutableListOf<StoreModel>()

    fun addToCart(product: StoreModel) {
        productsInCart.add(product)
    }

    fun removeFromCart(product: StoreModel) {
        productsInCart.remove(product)
    }

    fun getProductsInCart(): List<StoreModel> {
        return productsInCart
    }
}
