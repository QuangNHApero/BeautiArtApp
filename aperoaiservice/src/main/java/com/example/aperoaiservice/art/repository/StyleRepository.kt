package com.example.aperoaiservice.art.repository

import com.example.aperoaiservice.art.model.CategoryArt
import com.example.aperoaiservice.art.model.StyleArt
import com.example.aperoaiservice.art.network.VslStyleArtClient

object StyleRepository {
    suspend fun getStyleArts(url: String): List<CategoryArt> {
        val raw = VslStyleArtClient.service.fetchCategories(url)

        return raw.data.items
            .take(10)
            .map { category ->
                CategoryArt(
                    name = category.name,
                    styles = category.styles.map {
                        StyleArt(
                            _id = it._id,
                            name = it.name,
                            thumbnail = it.key,
                            baseModel = it.config.baseModel,
                            positivePrompt = it.config.positivePrompt,
                            negativePrompt = it.config.negativePrompt
                        )
                    }
                )
            }
    }
}