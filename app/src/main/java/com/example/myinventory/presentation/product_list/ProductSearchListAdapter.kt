package com.example.myinventory.presentation.product_list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myinventory.R
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.databinding.ItemProductBinding

class ProductSearchListAdapter(
    private val mContext: Context,
    private val productList: List<ProductEntity>
) : RecyclerView.Adapter<ProductSearchListAdapter.ProductSearchViewHolder>() {
    class ProductSearchViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            productEntity: ProductEntity,
            mContext: Context
        ) {
            binding.apply {
                tvProductName.text = productEntity.productName
                tvPrice.text = mContext.resources.getString(
                    R.string.product_list_screen_product_price,
                    productEntity.price.toString()
                )
                tvTax.text = mContext.resources.getString(
                    R.string.product_list_screen_product_tax,
                    productEntity.tax.toString()
                )
                tvType.text = mContext.resources.getString(
                    R.string.product_list_screen_product_type,
                    productEntity.productType
                )

                Glide.with(mContext)
                    .load(productEntity.image)
                    .placeholder(R.drawable.ic_product_placeholder)
                    .into(ivProductImage)

            }
        }

        companion object {
            fun from(parent: ViewGroup): ProductSearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
                return ProductSearchViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSearchViewHolder {
        return ProductSearchViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductSearchViewHolder, position: Int) {
        val currentProduct = productList[position]
        Log.d("oxoxtushar", "adapter called")
        holder.bind(currentProduct, mContext)
    }
}