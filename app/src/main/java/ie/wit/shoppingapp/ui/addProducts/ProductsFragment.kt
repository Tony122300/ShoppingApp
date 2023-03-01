package ie.wit.shoppingapp.ui.addProducts

import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.databinding.FragmentProductsBinding
import ie.wit.shoppingapp.main.StoreApp
import ie.wit.shoppingapp.models.StoreModel
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
        //app = activity?.application as DonationXApp
        setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root = binding.root
        activity?.title = getString(R.string.addButton)
        productsViewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        productsViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })
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
        setButtonListener(binding)
        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.error), Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentProductsBinding){
        layout.addButtom.setOnClickListener{
            val name = binding.ProductName.text.toString()
            val description = binding.ProductDescription.text.toString()
            if (name.isNotEmpty() && description.isNotEmpty()) {
                val product = StoreModel(
                    productName = name,
                    productDescription = description
                )
                Snackbar.make(binding.root, "Product added successfully", Snackbar.LENGTH_LONG)
                    .show()
                binding.ProductName.setText("")
                binding.ProductDescription.setText("")
                productsViewModel.addProduct(product)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter both name and description of the Product",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_store, menu)
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
    override fun onResume() {
        super.onResume()
    }
}