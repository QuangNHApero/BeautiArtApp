package com.example.beautisdk.utils.repository

import com.example.beautisdk.ui.data.model.CategoryArt
import kotlinx.coroutines.flow.Flow

interface ArtRepository {
    suspend fun loadFromRemote() : Flow<List<CategoryArt>>
}