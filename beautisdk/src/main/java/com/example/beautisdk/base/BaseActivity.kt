package com.example.beautisdk.base

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.artbeautify.utils.ext.hideSystemBar
import com.example.beautisdk.VslBeautiEntry
import com.example.beautisdk.api.config.VslBeautiConfig
import com.example.beautisdk.ui.design_system.AppTheme

abstract class BaseActivity : ComponentActivity() {
    protected val config: VslBeautiConfig by lazy {
        VslBeautiEntry.config()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBar()
        Log.d("Beauti", "bg = ${config.backgroundColor}")

        setContent {
            AppTheme {
                UpdateUI(
                    Modifier
                        .fillMaxSize()
                        .background(config.backgroundColor)
                )
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackNavigation()
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemBar()
    }

    protected abstract fun onBackNavigation()


    @Composable
    protected fun ObserveLifecycle(onEvent: (Lifecycle.Event) -> Unit) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        DisposableEffect(lifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                onEvent(event)
            }
            lifecycle.addObserver(observer)
            onDispose { lifecycle.removeObserver(observer) }
        }
    }

    @Composable
    fun ObserveWindowFocus(
        onFocusChange: (Boolean) -> Unit
    ) {
        val windowInfo = LocalWindowInfo.current
        val isFocused  = windowInfo.isWindowFocused

        LaunchedEffect(isFocused) { onFocusChange(isFocused) }
    }

    @Composable
    protected abstract fun UpdateUI(modifier: Modifier)
}