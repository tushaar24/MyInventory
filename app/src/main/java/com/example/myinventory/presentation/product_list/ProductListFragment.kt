package com.example.myinventory.presentation.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myinventory.data.common.entity.ProductEntity
import com.example.myinventory.databinding.FragmentProductListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: ProductListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel =
            ViewModelProvider(requireActivity())[ProductListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.progressBar.visibility = View.VISIBLE

        setUpProductListPagingAdapter()

        binding.svMovies.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    mViewModel.getSearchedProducts(query)
                }

                mViewModel.searchProducts.observe(requireActivity()) { productList ->
                    binding.progressBar.visibility = View.GONE
                    if (!productList.isNullOrEmpty()) {
                        setUpSearchProductsAdapter(productList)
                        binding.grpNoProductFoundOnSearch.visibility = View.GONE
                    } else {
                        binding.rvProductList.visibility = View.GONE
                        binding.rvProductSearchList.visibility = View.GONE
                        setUpProductListPagingAdapter()
                        binding.grpNoProductFoundOnSearch.visibility = View.VISIBLE
                    }
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.rvProductList.visibility = View.VISIBLE
                    binding.rvProductSearchList.visibility = View.GONE
                    binding.grpNoProductFoundOnSearch.visibility = View.GONE
                    setUpProductListPagingAdapter()
                }
                return false
            }
        })

        binding.fabAddProduct.setOnClickListener {
            val action =
                ProductListFragmentDirections.actionProductListFragmentToAddProductFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUpSearchProductsAdapter(productList: List<ProductEntity>) {
        binding.rvProductList.visibility = View.GONE
        binding.rvProductSearchList.visibility = View.VISIBLE
        val adapter = ProductSearchListAdapter(requireContext(), productList)
        binding.rvProductSearchList.adapter = adapter
        binding.rvProductSearchList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpProductListPagingAdapter() {
        val mAdapter = ProductListPagingAdapter(requireContext())
        binding.rvProductList.adapter = mAdapter
        binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getAllCachedProducts().collectLatest { pagingData ->
                mAdapter.submitData(pagingData)
            }
        }

        mAdapter.isDataSetChanged.observe(requireActivity()) { isDataSetChanged ->
            if (isDataSetChanged) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}