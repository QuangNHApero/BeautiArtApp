package com.example.beautisdk.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

internal object VslImageHandlerUtil {
    private const val TAG = "ImageHandlerUtil"
    /**
     * Preload một ảnh từ URL vào bộ nhớ đệm.
     * @param context Context ứng dụng hoặc activity.
     * @param url URL ảnh cần preload.
     * @param cacheKey Tùy chọn: key để gán cache rõ ràng.
     */
    suspend fun preload(
        context: Context,
        uri: Uri,
        cacheKey: String? = null,
        widthPx: Int = 430,
        heightPx: Int = 430
    ) {
        withContext(Dispatchers.IO) {
            runCatching {
                val request = ImageRequest.Builder(context)
                    .data(uri)
                    .apply {
                        cacheKey?.let {
                            memoryCacheKey(it)
                            diskCacheKey(it)
                        }
                        size(widthPx, heightPx)
                        memoryCachePolicy(CachePolicy.ENABLED)
                        diskCachePolicy(CachePolicy.ENABLED)
                    }
                    .build()

                ImageLoader(context).execute(request)
            }.onFailure {
                Log.e(TAG, "Preload failed: ${it.message}", it)
            }
        }
    }

    fun saveImageToExternal(
        context: Context,
        uri: Uri,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val resolver = context.contentResolver
            val inputStream = try {
                resolver.openInputStream(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Không thể mở ảnh từ đường dẫn: ${e.message}")
                    onError("Không thể mở ảnh từ đường dẫn: ${e.message}")
                }
                return@launch
            }

            if (inputStream == null) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Không thể đọc ảnh từ URI: $uri")
                    onError("Không thể đọc ảnh từ URI: $uri")
                }
                return@launch
            }

            val fileName = "image_${System.currentTimeMillis()}.jpg"

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(
                            MediaStore.Images.Media.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES + "/Beautify"
                        )
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }

                    val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                    val imageUri = resolver.insert(collection, contentValues)

                    if (imageUri == null) {
                        withContext(Dispatchers.Main) {
                            Log.e(TAG, "Không thể tạo ảnh mới trong MediaStore.")
                            onError("Không thể tạo ảnh mới trong MediaStore.")
                        }
                        inputStream.close()
                        return@launch
                    }

                    val outputStream = resolver.openOutputStream(imageUri)
                    if (outputStream == null) {
                        withContext(Dispatchers.Main) {
                            Log.e(TAG, "Không thể mở OutputStream để lưu ảnh.")
                            onError("Không thể mở OutputStream để lưu ảnh.")
                        }
                        inputStream.close()
                        return@launch
                    }

                    outputStream.use {
                        inputStream.copyTo(it)
                        inputStream.close()
                    }

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                } else {
                    // Android 7–9
                    val directory = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "Beautify"
                    )
                    if (!directory.exists() && !directory.mkdirs()) {
                        withContext(Dispatchers.Main) {
                            Log.e(TAG, "Không thể tạo thư mục: ${directory.absolutePath}")
                            onError("Không thể tạo thư mục: ${directory.absolutePath}")
                        }
                        inputStream.close()
                        return@launch
                    }

                    val file = File(directory, fileName)
                    FileOutputStream(file).use { output ->
                        inputStream.copyTo(output)
                        inputStream.close()
                    }

                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.DATA, file.absolutePath)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    }
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                }

                withContext(Dispatchers.Main) {
                    onSuccess()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                inputStream.close()
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "Error saving image: ${e.message}")
                    onError("Lỗi khi lưu ảnh: ${e.localizedMessage ?: "Không rõ lỗi"}")
                }
            }
        }
    }
}