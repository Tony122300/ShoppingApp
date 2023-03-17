package ie.wit.shoppingapp.ui.list

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.adapter.ReportClickListener
import ie.wit.shoppingapp.adapter.StoreAdapter

import ie.wit.shoppingapp.databinding.FragmentListBinding
import ie.wit.shoppingapp.main.StoreApp
import ie.wit.shoppingapp.models.StoreModel

class ListFragment : Fragment(), ReportClickListener {
    lateinit var store : StoreApp
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = activity?.application as StoreApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root = binding.root
        binding.recyclerView.layoutManager = GridLayoutManager(activity,2)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.observableProductsList.observe(viewLifecycleOwner, Observer {
                products ->
            products?.let { render(products) }
        })
        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToProductsFragment()
            findNavController().navigate(action)
        }
        return root
    }

    private fun render(storeList: List<StoreModel>) {
        binding.recyclerView.adapter = StoreAdapter(storeList,this)
        if (storeList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.noProducts.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.noProducts.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onReportClick(products: StoreModel) {
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment(products.id)
        findNavController().navigate(action)

    }
    override fun onResume() {
        super.onResume()
        listViewModel.load()
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}