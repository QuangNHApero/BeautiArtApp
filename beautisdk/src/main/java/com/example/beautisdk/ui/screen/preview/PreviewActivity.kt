package com.example.beautisdk.ui.screen.preview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.ui.theme.ArtBeautifyTheme

class PreviewActivity : BaseActivity() {

    override fun onBackNavigation() {

    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        Greeting("Preview", modifier)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArtBeautifyTheme {
        Greeting("Android")
    }
}