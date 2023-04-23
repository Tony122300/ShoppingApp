package ie.wit.shoppingapp.ui.addProducts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.databinding.FragmentProductsBinding
import ie.wit.shoppingapp.main.StoreApp
import ie.wit.shoppingapp.models.StoreModel
import timber.log.Timber.Forest.i
import java.io.File
import java.util.*

class ProductsFragment : Fragment() {
    lateinit var store : StoreApp
    private lateinit var storeApp: StoreModel
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeApp = StoreModel()
        store = activity?.application as StoreApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root = binding.root
        activity?.title = getString(R.string.addButton)
        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        productsViewModel.observableStatus.observe(
            viewLifecycleOwner,
            Observer { status -> status?.let { render(status) }
        })
        setButtonListener(binding)


        val datePicker = view?.findViewById<DatePicker>(R.id.date_Picker)
        val today = Calendar.getInstance()
        if (datePicker != null) {
            datePicker.init(
                today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ) { _, year, month, dayOfMonth ->
                val month = month + 1
                val msg = "You Selected: $dayOfMonth/$month/$year"
                Toast.makeText(requireContext(), "Selected date: $msg", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                }
            }
            false -> Toast.makeText(context,getString(R.string.error), Toast.LENGTH_LONG).show()
        }
    }

    private val pickImageContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.let { intent ->
                intent.data?.let { uri ->
                    storeApp.image = uri
                    Picasso.get().load(uri).into(binding.productImage)
                    }
                }
            }
        }
    private fun setButtonListener(layout: FragmentProductsBinding){
        layout.addButtom.setOnClickListener{
            val name = binding.ProductName.text.toString()
            val description = binding.ProductDescription.text.toString()
            val bestBeforeDate = binding.datePicker
            val priceStr = binding.price.text.toString()
            val price = if (priceStr.isNotEmpty()) priceStr.toDouble() else 0.0
            val barcode = binding.barcode.text.toString()
            val category = binding.category.text.toString()
            val producer = binding.producer.text.toString()
            val asile = binding.asile.text.toString().toInt()
            if (name.isNotEmpty() && description.isNotEmpty()) {
                val product = StoreModel(
                    productName = name,
                    productDescription = description,
                    bestBeforeDate = bestBeforeDate,
                    price = price,
                    image = storeApp.image, // Set the image for the product
                    barcode = barcode,
                    category = category,
                    producer = producer,
                    asile = asile
                )
                Snackbar.make(binding.root, "Product added successfully", Snackbar.LENGTH_LONG)
                    .show()
                binding.ProductName.setText("")
                binding.ProductDescription.setText("")
                binding.price.setText("")
                binding.barcode.setText("")
                productsViewModel.addProduct(product, store)

                // Reset the image to empty after adding the product
                storeApp.image = Uri.EMPTY
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter both name and description of the Product",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
        binding.chooseImage.setOnClickListener {
            pickImageContract.launch(Intent(Intent.ACTION_PICK).setType("image/*"))
        }
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProductsFragment().apply {
                arguments = Bundle().apply {}
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}