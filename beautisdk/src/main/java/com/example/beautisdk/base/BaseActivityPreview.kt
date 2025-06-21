package com.example.beautisdk.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.pick_photo.VslPickPhotoActivity
import com.example.beautisdk.utils.PermissionUtil
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

internal abstract class BaseActivityPreview : BaseActivity() {
    private lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestReadExternalStoragePermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLauncher()
    }

    private fun registerLauncher() {
        pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val uri = result.data?.data
                    if (uri != null) {
                        onImagePicked(uri)
                    }
                }
            }

        requestReadExternalStoragePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                launchCustomPickPhoto()
            } else {
                Toast.makeText(this, "Cần cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            try {
                withTimeout(1_000) {
                    if(VslImageHandlerUtil.checkShouldRefreshPhotos(this@BaseActivityPreview)) {
                        VslImageHandlerUtil.queryPhotoChunkManualIo(
                            context = this@BaseActivityPreview,
                            offset = 0,
                            limit = 30,
                            preloadWidth = 130.pxToDp().value.toInt(),
                            preloadHeight = 130.pxToDp().value.toInt()
                        )
                    }
                }
            } catch (_: TimeoutCancellationException) {
            }
        }
    }


    protected fun checkAndLaunchPickPhoto() {
        PermissionUtil.checkAndRequestReadPermission(
            context = this,
            launcher = requestReadExternalStoragePermissionLauncher,
            onGranted = {
                launchCustomPickPhoto()
            }
        )
    }

    protected fun launchCustomPickPhoto() {
        val intent = Intent(this, VslPickPhotoActivity::class.java)
        pickPhotoLauncher.launch(intent)
    }

    abstract fun onImagePicked(uri: Uri)

}