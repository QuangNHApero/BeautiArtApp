package com.example.aperoaiservice.fetch.data.repository

import android.util.Log
import com.example.aperoaiservice.fetch.data.model.mapper.toDomain
import com.example.aperoaiservice.fetch.data.remote.StyleApi
import com.example.aperoaiservice.fetch.domain.model.CategoryArt
import com.example.aperoaiservice.fetch.domain.repository.StyleRepository

internal class StyleRepositoryImpl(
    private val api: StyleApi
) : StyleRepository {

    override suspend fun fetchCategories(url: String): Result<List<CategoryArt>> {
        return try {
            val response = api.getCategories(url)
            val items = response.data.items.map { it.toDomain() }
            Result.success(items)
        } catch (e: Exception) {
            Log.e("fetchCategories", "Lỗi gọi API", e)
            Result.failure(e)
        }
    }
}