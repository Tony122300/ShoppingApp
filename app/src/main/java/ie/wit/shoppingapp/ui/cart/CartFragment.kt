package ie.wit.shoppingapp.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.adapter.CartAdapter
import ie.wit.shoppingapp.models.StoreModel
//cart
class CartFragment : Fragment() {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var products: List<StoreModel>
    private lateinit var cartRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
        products = cartViewModel.getProductsInCart()

        // Set up the cart RecyclerView
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        val cartAdapter = CartAdapter()
        cartRecyclerView.layoutManager = layoutManager
        cartRecyclerView.adapter = cartAdapter

        // Display the products in the cart
        cartAdapter.submitList(products)

        return view
    }
}
