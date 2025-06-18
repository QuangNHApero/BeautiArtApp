package com.example.aperoaiservice.data.repository

import com.example.aperoaiservice.data.model.mapper.toDomain
import com.example.aperoaiservice.data.remote.StyleApi
import com.example.aperoaiservice.domain.model.CategoryArt
import com.example.aperoaiservice.domain.repository.StyleRepository

internal class StyleRepositoryImpl(
    private val api: StyleApi
) : StyleRepository {

    override suspend fun fetchCategories(url: String): List<CategoryArt> {
        val response = api.getCategories(url)
        return response.data.items.map { it.toDomain() }
    }
}