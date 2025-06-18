package com.example.beautisdk.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.beautisdk.ui.screen.pick_photo.VslPickPhotoActivity

internal abstract class BaseActivityPreview : BaseActivity() {
    private lateinit var pickPhotoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    onImagePicked(uri)
                }
            }
        }
    }

    protected fun launchCustomPickPhoto() {
        val intent = Intent(this, VslPickPhotoActivity::class.java)
        pickPhotoLauncher.launch(intent)
    }

    abstract fun onImagePicked(uri: Uri)

}