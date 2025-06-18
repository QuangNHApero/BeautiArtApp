package com.example.beautisdk.ui.screen.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class VslSplashViewModel : ViewModel(){
    private var _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading : StateFlow<Boolean> = _isLoading.asStateFlow()

    fun setLoading(isLoading: Boolean){
        _isLoading.value = isLoading
    }
}