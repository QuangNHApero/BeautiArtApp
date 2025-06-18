package com.example.aperoaiservice.domain.repository

import com.example.aperoaiservice.domain.model.CategoryArt

interface StyleRepository {
    suspend fun fetchCategories(url: String): List<CategoryArt>
}