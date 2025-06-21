package com.example.beautisdk.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import coil.ImageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.beautisdk.ui.screen.pick_photo.data.PhotoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
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


    suspend fun checkShouldRefreshPhotos(context: Context): Boolean = withContext(Dispatchers.IO) {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val latestId = context.contentResolver.query(
            collection, projection, null, null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getLong(0) else null
        }

        latestId?.let { newId ->
            val cachedFirstId = _cachedPhotos.firstOrNull()?.id
            if (cachedFirstId != null && newId != cachedFirstId) {
                clearPhotos()
                isLoadFullImage.set(false)
                return@withContext true
            }
        }

        return@withContext false
    }

    private fun clearPhotos() {
        _cachedIds.clear()
        _cachedPhotos.clear()
        isLoadFullImage.set(false)
    }

    suspend fun queryPhotoChunkManualIo(
        context: Context,
        offset: Int,
        limit: Int,
        preloadWidth: Int,
        preloadHeight: Int
    ): List<PhotoItem> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            queryPhotoChunkApi29Plus(context, offset, limit, preloadWidth, preloadHeight)
        else
            queryPhotoChunkLegacy  (context, offset, limit, preloadWidth, preloadHeight)

    @Suppress("NewApi")
    private suspend fun queryPhotoChunkApi29Plus(
        context: Context,
        offset: Int,
        limit: Int,
        preloadWidth: Int,
        preloadHeight: Int
    ): List<PhotoItem> = withContext(Dispatchers.IO) {

        if (isLoadFullImage.get()) return@withContext emptyList()

        val result     = mutableListOf<PhotoItem>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        val args = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT,  limit)
            putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
            putStringArray(
                ContentResolver.QUERY_ARG_SORT_COLUMNS,
                arrayOf(MediaStore.Images.Media.DATE_ADDED)
            )
            putInt(
                ContentResolver.QUERY_ARG_SORT_DIRECTION,
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )
        }

        context.contentResolver.query(collection, projection, args, null)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext() && isActive) {
                val id  = cursor.getLong(idCol)
                val uri = ContentUris.withAppendedId(collection, id)

                if (_cachedIds.add(id)) _cachedPhotos += PhotoItem(id, uri)
                result += PhotoItem(id, uri)

                preload(context, uri, widthPx = preloadWidth, heightPx = preloadHeight)
            }
        }

        if (result.isEmpty()) isLoadFullImage.set(true)
        return@withContext result
    }

    private suspend fun queryPhotoChunkLegacy(
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
            if (!cursor.moveToPosition(offset)) return@use

            val idCol   = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var fetched = 0

            while (cursor.moveToNext() && fetched < limit) {
                coroutineContext.ensureActive()

                val id  = cursor.getLong(idCol)
                val uri = ContentUris.withAppendedId(collection, id)
                val item = PhotoItem(id, uri)

                if (_cachedIds.add(id)) _cachedPhotos += item
                result += item

                preload(context, uri, widthPx = preloadWidth, heightPx = preloadHeight)
                fetched++
            }

        }

        if (result.isEmpty()) isLoadFullImage.set(true)

        return@withContext result
    }
}