package me.ruyeo.testapp.ui.fragments.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.ruyeo.testapp.model.ErrorResponse
import me.ruyeo.testapp.model.User
import me.ruyeo.testapp.repository.AuthRepository
import me.ruyeo.testapp.utils.UiStateObject
import me.ruyeo.testapp.utils.extensions.userMessage
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel(){

    private val _login = MutableStateFlow<UiStateObject<User>>(UiStateObject.EMPTY)
    val login = _login

    fun login(map: HashMap<String,Any>) = viewModelScope.launch {
        _login.value = UiStateObject.LOADING
        try {
            val response = repository.login(map)
            if (response.code() >= 400){
                val error = Gson().fromJson(response.errorBody()!!.charStream(),ErrorResponse::class.java)
                _login.value = UiStateObject.ERROR(error.errorMessage)
            }else{
                _login.value = UiStateObject.SUCCESS(response.body()!!)
            }

        }catch (e: Exception){
            _login.value = UiStateObject.ERROR(e.userMessage())
        }
    }
}