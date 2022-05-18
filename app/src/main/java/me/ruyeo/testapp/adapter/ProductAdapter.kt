package me.ruyeo.testapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import me.ruyeo.testapp.databinding.ProductItemBinding
import me.ruyeo.testapp.model.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.VH>() {
    private val dif = AsyncListDiffer(this, ITEM_DIFF)
    var onClick: ((Product) -> Unit)? = null

    inner class VH(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val product = dif.currentList[adapterPosition]
            binding.apply {
                name.text = product.name
                price.text = product.price.toString()
                Picasso.get().load(product.photoUrl).into(image)

                discount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                root.setOnClickListener {
                    onClick?.invoke(product)
                }
            }
        }
    }

    fun submitList(list: List<Product>) {
        dif.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = dif.currentList.size

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}