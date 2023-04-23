package ie.wit.shoppingapp.ui.details

import android.annotation.SuppressLint
import android.net.Uri
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
import com.squareup.picasso.Picasso
import ie.wit.shoppingapp.databinding.FragmentDetailsBinding
import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreJSONStore

//import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreModel

import ie.wit.shoppingapp.ui.cart.CartViewModel
//detials fragment
class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var cartViewModel: CartViewModel
    private lateinit var viewModel: DetailsViewModel
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val product = StoreJSONStore.findById(args.productid)
        Toast.makeText(context,"product ID Selected : ${args.productid}", Toast.LENGTH_LONG).show()

        // Get the cart view model
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)

        // Populate the UI with the product details
        if (product != null) {
            Picasso.get().load(product.image).into(binding.productImage)

            binding.productName.text = product.productName
            binding.ProductDescription.text = product.productDescription
            binding.price.text = getString(R.string.price_format, product.price)
            binding.barcode.setText(product.barcode.toString())
            binding.asile.setText(product.asile.toString())
            binding.producer.text = product.producer
            // Set a click listener for the "Add to Cart" button
            binding.addToCartButton.setOnClickListener {
                // Add the product to the cart
                cartViewModel.addToCart(product)

                // Show a toast message to indicate the product has been added to the cart
                Toast.makeText(context, "Product added to cart!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}