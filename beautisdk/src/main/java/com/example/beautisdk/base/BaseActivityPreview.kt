package com.example.beautisdk.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.beautisdk.ui.screen.pick_photo.VslPickPhotoActivity
import com.example.beautisdk.utils.PermissionUtil
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.launch

internal abstract class BaseActivityPreview : BaseActivity() {
    private lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestReadExternalStoragePermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLauncher()
    }

    private fun registerLauncher() {
        pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

    protected fun checkAndLaunchPickPhoto() {
        PermissionUtil.checkAndRequestReadPermission(
            context = this,
            launcher = requestReadExternalStoragePermissionLauncher,
            onGranted = {
                lifecycleScope.launch {
                    Log.d("quangnh","checkAndLaunchPickPhoto ${VslImageHandlerUtil.cachedPhotos.size}")
                    VslImageHandlerUtil.checkShouldRefreshPhotos(this@BaseActivityPreview)
                    launchCustomPickPhoto()
                }
            }
        )
    }

    protected fun launchCustomPickPhoto() {
        val intent = Intent(this, VslPickPhotoActivity::class.java)
        pickPhotoLauncher.launch(intent)
    }

    abstract fun onImagePicked(uri: Uri)

}