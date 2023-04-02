package com.example.myinventory.presentation.product_list

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myinventory.R
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.databinding.ItemProductBinding

class ProductListPagingAdapter(
    private val mContext: Context
) :
    PagingDataAdapter<ProductEntity, ProductListPagingAdapter.ProductListViewHolder>(DiffUtil) {

    val isDataSetChanged = MutableLiveData(false)

    class ProductListViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
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
            fun from(parent: ViewGroup): ProductListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
                return ProductListViewHolder(binding)
            }
        }
    }

    object DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val currentProduct = getItem(position)
        if (currentProduct != null) {
            holder.bind(currentProduct, mContext)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder.from(parent)
    }

    override fun onViewAttachedToWindow(holder: ProductListViewHolder) {
        super.onViewAttachedToWindow(holder)
        isDataSetChanged.value = true
    }
}