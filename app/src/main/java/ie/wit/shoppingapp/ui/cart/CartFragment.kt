//https://razorpay.com/docs/payments/payment-gateway/android-integration/standard/build-integration
//https://www.youtube.com/watch?v=uDf21HIsSSU&t=198s&ab_channel=DevEasy
package ie.wit.shoppingapp.ui.cart


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener


import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.adapter.CartAdapter
import ie.wit.shoppingapp.models.StoreModel

import org.json.JSONObject


//cart
class CartFragment : Fragment(), PaymentResultListener {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var products: List<StoreModel>
    private lateinit var cartRecyclerView: RecyclerView
    lateinit var pay: Button
    lateinit var card: CardView
    lateinit var success: TextView
    lateinit var failed: TextView
    private var totalPrice = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val placeOrderButton = view.findViewById<Button>(R.id.placeOrderButton)
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)
        products = cartViewModel.getProductsInCart()

        // Set up the cart RecyclerView
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        val cartAdapter = CartAdapter()
        cartRecyclerView.layoutManager = layoutManager
        cartRecyclerView.adapter = cartAdapter

        // Calculate the total price of all products in the cart
        totalPrice = products.fold(0.0) { acc, product ->
            acc + product.price
        }

        // Set the total price text view
        val totalPriceTextView = view.findViewById<TextView>(R.id.orderTotalTextView)
        totalPriceTextView.text = "Total Price: $totalPrice"
        pay = view.findViewById(R.id.placeOrderButton)
        pay.setOnClickListener {
            payment()
        }
        // Display the products in the cart
        cartAdapter.submitList(products)

        return view
    }

    private fun payment(){
        val checkOut = Checkout()
        checkOut.setKeyID("rzp_test_H2OYtXSJ3TZkKg")
        try{
            val options = JSONObject()
            options.put("name","tony")
            options.put("description","tony")
            options.put("image","image\",\"https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg\"")
            options.put("theme.color","#3399cc")
            options.put("currency","EUR")
            options.put("amount",(totalPrice*100).toString())

            val prefill = JSONObject()
            prefill.put("email","")
            prefill.put("contact","")

            options.put("prefill",prefill)
            checkOut.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0:String?){
        Toast.makeText(requireContext(),"successful $p0",Toast.LENGTH_LONG).show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(requireContext(),"Failed $p0",Toast.LENGTH_LONG).show()
    }
}