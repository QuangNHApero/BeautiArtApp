package com.example.aperoaiservice.fetch.domain.repository

import com.example.aperoaiservice.fetch.domain.model.CategoryArt

interface StyleRepository {
    suspend fun fetchCategories(url: String): Result<List<CategoryArt>>
}