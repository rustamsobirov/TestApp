package me.ruyeo.testapp.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import me.ruyeo.testapp.R
import me.ruyeo.testapp.SharedPref
import me.ruyeo.testapp.databinding.FragmentLoginBinding
import me.ruyeo.testapp.ui.fragments.BaseFragment
import me.ruyeo.testapp.utils.UiStateObject
import me.ruyeo.testapp.utils.extensions.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding { FragmentLoginBinding.bind(it) }
    private val viewModel by viewModels<LoginViewModel>()
    @Inject
    lateinit var sharedPref: SharedPref

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        val map = HashMap<String,Any>()
        binding.apply {
            loginBtn.setOnClickListener {
                map["login"] = loginEt.text.toString()
                map["password"] = passwordEt.text.toString()
                viewModel.login(map)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when(it){
                    is UiStateObject.LOADING -> {
                        //show progress
                    }
                    is UiStateObject.SUCCESS -> {
                        sharedPref.token = it.data.token
                        findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
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