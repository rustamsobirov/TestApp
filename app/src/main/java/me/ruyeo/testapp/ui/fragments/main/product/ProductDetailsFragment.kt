package me.ruyeo.testapp.ui.fragments.main.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenStarted
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ruyeo.testapp.R
import me.ruyeo.testapp.databinding.FragmentProductDetailsBinding
import me.ruyeo.testapp.ui.fragments.BaseFragment
import me.ruyeo.testapp.utils.UiStateObject
import me.ruyeo.testapp.utils.extensions.viewBinding

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment(R.layout.fragment_product_details) {

    private val binding by viewBinding { FragmentProductDetailsBinding.bind(it) }
    private val viewModel by viewModels<ProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var id = 0
        arguments?.let {
            id = it.getInt("productId")
        }
        viewModel.getProduct(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.product.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        //show progress
                    }
                    is UiStateObject.SUCCESS -> {
                        binding.apply {
                            name.text = it.data.name
                            description.text = it.data.description
                            price.text = it.data.price.toString()
                            Picasso.get().load(it.data.photoUrl).into(image)
                        }
                    }
                    is UiStateObject.ERROR -> {
                        showMessage(it.message)
                    }
                    else -> Unit
                }
            }
        }
    }
}