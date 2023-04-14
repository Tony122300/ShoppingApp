package ie.wit.shoppingapp.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ie.wit.shoppingapp.R
import com.bumptech.glide.Glide
import ie.wit.shoppingapp.models.StoreJSONStore

//import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreModel

import ie.wit.shoppingapp.ui.cart.CartViewModel

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var cartViewModel: CartViewModel
    private lateinit var viewModel: DetailsViewModel
    private val args by navArgs<DetailsFragmentArgs>()
    lateinit var products: StoreModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        var product = StoreJSONStore.findById(args.productid)
        Toast.makeText(context,"product ID Selected : ${args.productid}", Toast.LENGTH_LONG).show()

        // Get the cart view model
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)

        val photoImageView = view.findViewById<ImageView>(R.id.photo)
        val productNameTextView = view.findViewById<TextView>(R.id.product_name)
        val priceTextView = view.findViewById<TextView>(R.id.price)
        val descriptionTextView = view.findViewById<TextView>(R.id.ProductDescription)

        // Find the product by ID in the store
        product = StoreJSONStore.findById(args.productid)

        // Populate the UI with the product details
        if (product != null) {
            productNameTextView?.text = product.productName
            descriptionTextView?.text = product.productDescription
        }

        priceTextView.text = getString(R.string.price_format, product?.price)

        // Get a reference to the "Add to Cart" button
        val addToCartButton = view.findViewById<Button>(R.id.addToCartButton)

        // Set a click listener for the "Add to Cart" button
        addToCartButton.setOnClickListener {
            // Add the product to the cart
            if (product != null) {
                cartViewModel.addToCart(product)
            }

            // Show a toast message to indicate the product has been added to the cart
            Toast.makeText(context, "Product added to cart!", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}