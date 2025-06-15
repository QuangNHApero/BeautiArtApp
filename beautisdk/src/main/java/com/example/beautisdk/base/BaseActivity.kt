package com.example.beautisdk.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.artbeautify.utils.ext.hideSystemBar
import com.example.beautisdk.ui.design_system.AppTheme

abstract class BaseActivity : ComponentActivity() {

    private lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        hideSystemBar()
        setContent {
            AppTheme {
                UpdateUI(
                    Modifier
                        .fillMaxSize()
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
    protected abstract fun UpdateUI(modifier: Modifier)

}