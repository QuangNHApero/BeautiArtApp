package com.example.beautisdk.ui.screen.splash

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aperoaiservice.domain.repository.StyleRepository
import com.example.beautisdk.data.VslBeautiRemote
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.utils.PermissionUtil
import com.example.beautisdk.utils.VslBeautiConst.BASE_URL
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

internal class VslSplashViewModel : ViewModel(){
    private val _effect = Channel<VslSplashEffect>()
    val effect = _effect.receiveAsFlow()

    fun preloadDatas(context: Context, styleRepo: StyleRepository) {
        viewModelScope.launch(Dispatchers.IO) {
            if (PermissionUtil.hasReadExternalPermission(context)) {
                if (PermissionUtil.hasReadExternalPermission(context)) {
                    try {
                        withTimeout(5000L) {
                            val job1 = async {
                                VslImageHandlerUtil.queryPhotoChunkManualIo(
                                    context = context,
                                    offset = 0,
                                    limit = 30,
                                    preloadWidth = 130.pxToDp().value.toInt(),
                                    preloadHeight = 130.pxToDp().value.toInt()
                                )
                            }
                            val job2 = async {
                                styleRepo.fetchCategories(BASE_URL).fold(
                                    onSuccess = { data ->
                                        VslBeautiRemote.remoteCategorys = data
                                    },
                                    onFailure = { throwable ->
                                        Log.e(TAG, "Failed to fetch categories", throwable)
                                    }
                                )
                            }
                            awaitAll(job1, job2)
                        }
                    } catch (e: TimeoutCancellationException) {
                        Log.w(TAG, "Preload timed out after 5 seconds")
                    }
                }
                _effect.send(VslSplashEffect.NavigateToNextActivity)
            } else {
                _effect.send(VslSplashEffect.NavigateToNextActivity)
            }
        }
    }

    companion object {
        private const val TAG = "VslSplashViewModel"
    }
}

internal sealed class VslSplashEffect {
    object NavigateToNextActivity : VslSplashEffect()
}