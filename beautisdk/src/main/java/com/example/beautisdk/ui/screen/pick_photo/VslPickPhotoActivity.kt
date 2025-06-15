package com.example.beautisdk.ui.screen.pick_photo

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.beautisdk.base.BaseActivity

class VslPickPhotoActivity : BaseActivity() {
    override fun onBackNavigation() {
        finish()
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {

    }

    private fun onPhotoApply(selectedUri: Uri) {
        val intent = Intent().apply {
            data = selectedUri
        }
        setResult(RESULT_OK, intent)
        finish()
    }

}