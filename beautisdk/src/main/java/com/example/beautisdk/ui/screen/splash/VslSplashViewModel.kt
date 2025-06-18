package com.example.beautisdk.ui.screen.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.utils.PermissionUtil
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

internal class VslSplashViewModel : ViewModel(){
    private val _effect = Channel<VslSplashEffect>()
    val effect = _effect.receiveAsFlow()

    fun preloadPhotos(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (PermissionUtil.hasReadExternalPermission(context)) {

                if (PermissionUtil.hasReadExternalPermission(context)) {
                    try {
                        withTimeout(3000L) {
                            VslImageHandlerUtil.queryPhotoChunkManualIo(
                                context = context,
                                offset = 0,
                                limit = 30,
                                130.pxToDp().value.toInt(),
                                130.pxToDp().value.toInt()
                            )
                        }
                    } catch (e: TimeoutCancellationException) {
                        Log.w("PreloadPhotos", "Preload timed out after 3 seconds")
                    }
                }
                _effect.send(VslSplashEffect.NavigateToNextActivity)
            } else {
                _effect.send(VslSplashEffect.NavigateToNextActivity)
            }
        }
    }
}

internal sealed class VslSplashEffect {
    object NavigateToNextActivity : VslSplashEffect()
}