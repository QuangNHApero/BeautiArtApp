package com.example.beautisdk.utils

import android.content.ContentUris
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
import com.example.beautisdk.ui.screen.pick_photo.data.PhotoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

internal object VslImageHandlerUtil {
    private const val TAG = "ImageHandlerUtil"
    private val _cachedPhotos = mutableListOf<PhotoItem>()
    private val _cachedIds = mutableSetOf<Long>()
    val cachedPhotos: List<PhotoItem> get() = _cachedPhotos
    private val isLoadFullImage = AtomicBoolean(false)

    /**
     * Preload một ảnh từ URL vào bộ nhớ đệm.
     * @param context Context ứng dụng hoặc activity.
     * @param uri URI ảnh cần preload.
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

    suspend fun saveImageToExternal(
        context: Context,
        uri: Uri
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        val inputStream = try {
            resolver.openInputStream(uri)
                ?: return@withContext Result.failure(Exception("Không thể đọc ảnh từ URI: $uri"))
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.failure(Exception("Không thể mở ảnh từ đường dẫn: ${e.message}"))
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
                    ?: return@withContext Result.failure(Exception("Không thể tạo ảnh mới trong MediaStore."))

                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                } ?: return@withContext Result.failure(Exception("Không thể mở OutputStream để lưu ảnh."))

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
                    return@withContext Result.failure(Exception("Không thể tạo thư mục: ${directory.absolutePath}"))
                }

                val file = File(directory, fileName)
                FileOutputStream(file).use { output ->
                    inputStream.copyTo(output)
                }

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, file.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("Lỗi khi lưu ảnh: ${e.localizedMessage ?: "Không rõ lỗi"}"))
        } finally {
            inputStream.close()
        }
    }


    suspend fun checkShouldRefreshPhotos(context: Context) = withContext(Dispatchers.IO) {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val latestId =
            context.contentResolver.query(
                collection, projection, null, null, "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )?.use { cursor -> if (cursor.moveToFirst()) cursor.getLong(0) else null }

        latestId?.let {
            val cachedFirstId = _cachedPhotos.firstOrNull()?.id
            if (cachedFirstId != null && latestId != cachedFirstId) {
                clearPhotos()
                isLoadFullImage.set(false)
            }
        }
    }

    private fun clearPhotos() {
        _cachedPhotos.clear()
        _cachedIds.clear()
    }

    suspend fun queryPhotoChunkManualIo(
        context: Context,
        offset: Int,
        limit: Int,
        preloadWidth: Int,
        preloadHeight: Int
    ): List<PhotoItem> = withContext(Dispatchers.IO) {
        if (isLoadFullImage.get()) return@withContext emptyList()

        val result = mutableListOf<PhotoItem>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            val hasData = if (offset > 0) cursor.move(offset) else cursor.moveToFirst()
            if (!hasData) return@use

            if (cursor.moveToPosition(offset)) {
                var fetchedCount = 0
                do {
                    if (fetchedCount >= limit) break

                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(collection, id)
                    result.add(PhotoItem(id, uri))

                    if (_cachedPhotos.none { it.id == id }) {
                        _cachedPhotos.add(PhotoItem(id, uri))
                    }

                    preload(context = context, uri = uri, widthPx =  preloadWidth, heightPx = preloadHeight)
                    fetchedCount++
                } while (cursor.moveToNext())
            }
        }

        if (result.isEmpty()) isLoadFullImage.set(true)

        return@withContext result
    }
}