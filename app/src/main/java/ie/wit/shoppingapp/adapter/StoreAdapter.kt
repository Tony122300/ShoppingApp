package ie.wit.shoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                binding.root.setOnClickListener { listener.onReportClick(store) }
            }
        }
    }