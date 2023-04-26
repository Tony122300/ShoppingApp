package ie.wit.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.shoppingapp.R
import ie.wit.shoppingapp.models.StoreModel
// cart adapter
class CartAdapter : ListAdapter<StoreModel, CartAdapter.CartViewHolder>(StoreModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_row, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
    fun deleteItem(position: Int) {
        // Get the item being deleted
        val deletedItem = getItem(position)

        // Remove the item from the adapter
        currentList.toMutableList().removeAt(position)
        notifyItemRemoved(position)

    }
    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)
        private val quantitySpinner: Spinner = itemView.findViewById(R.id.quantitySpinner)
        private val deleteProductButton: ImageButton = itemView.findViewById(R.id.deleteProductButton)
        private val productTotalPriceTextView: TextView = itemView.findViewById(R.id.productTotalPriceTextView)

        fun bind(product: StoreModel) {
            productNameTextView.text = product.productName
            Picasso.get().load(product.image).into(productImageView)


            // Set up quantity spinner
            val quantityArray = arrayOf("1", "2", "3", "4", "5")
            val adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, quantityArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            quantitySpinner.adapter = adapter


            // Calculate and set total price
            val totalPrice = product.price * quantitySpinner.selectedItem.toString().toInt()
            productTotalPriceTextView.text = itemView.context.getString(R.string.price_format, totalPrice)


        }
    }
}

class StoreModelDiffCallback : DiffUtil.ItemCallback<StoreModel>() {

    override fun areItemsTheSame(oldItem: StoreModel, newItem: StoreModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StoreModel, newItem: StoreModel): Boolean {
        return oldItem == newItem
    }
}
