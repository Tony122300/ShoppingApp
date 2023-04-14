package ie.wit.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.databinding.ActivityMainBinding
import ie.wit.shoppingapp.databinding.HomeBinding
import ie.wit.shoppingapp.models.StoreModel

interface ReportClickListener {
    fun onReportClick(store: StoreModel)
}


    class StoreAdapter constructor(private var stores: List<StoreModel>,private val listener: ReportClickListener)
        : RecyclerView.Adapter<StoreAdapter.MainHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
            val binding = ActivityMainBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return MainHolder(binding)
        }

        override fun onBindViewHolder(holder: MainHolder, position: Int) {
            val store = stores[holder.adapterPosition]
            holder.bind(store, listener)
        }

        override fun getItemCount(): Int = stores.size

        inner class MainHolder(val binding: ActivityMainBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(store: StoreModel, listener: ReportClickListener) {
                binding.product = store
                binding.name.text = store.productName
                binding.price.text = store.price.toString()
                Picasso.get().load(store.image).into(binding.productImage)
                binding.root.setOnClickListener { listener.onReportClick(store) }
            }
        }
    }