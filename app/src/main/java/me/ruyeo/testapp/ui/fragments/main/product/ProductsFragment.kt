package me.ruyeo.testapp.ui.fragments.main.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import me.ruyeo.testapp.R
import me.ruyeo.testapp.adapter.ProductAdapter
import me.ruyeo.testapp.databinding.FragmentProductsBinding
import me.ruyeo.testapp.ui.fragments.BaseFragment
import me.ruyeo.testapp.utils.UiStateList
import me.ruyeo.testapp.utils.extensions.viewBinding

@AndroidEntryPoint
class ProductsFragment : BaseFragment(R.layout.fragment_products) {

    private val binding by viewBinding { FragmentProductsBinding.bind(it) }
    private val viewModel by viewModels<ProductViewModel>()
    private val adapter by lazy { ProductAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        binding.productRv.adapter = adapter
        adapter.onClick = {
            findNavController().navigate(R.id.action_productsFragment_to_productDetailsFragment,
                bundleOf("productId" to it.id))
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.products.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        //show progress
                    }
                    is UiStateList.SUCCESS -> {
                        adapter.submitList(it.data)
                    }
                    is UiStateList.ERROR -> {
                        showMessage(it.message)
                    }
                    else -> Unit
                }
            }
        }
    }
}