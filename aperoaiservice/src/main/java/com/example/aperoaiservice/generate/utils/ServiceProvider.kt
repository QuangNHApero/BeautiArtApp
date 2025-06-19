package com.example.aperoaiservice.generate.utils

import com.example.aperoaiservice.generate.network.ApiClient
import com.example.aperoaiservice.generate.network.repository.style.AiArtRepository
import com.example.aperoaiservice.generate.network.repository.style.AiArtRepositoryImpl

object ServiceProvider {
    private val apiClient: ApiClient = ApiClient.getInstance()

    fun provideAiArtRepository(): AiArtRepository =
        AiArtRepositoryImpl(
            apiClient.buildArtAi(),
            HandlerApiWithImageImpl(apiClient.buildArtAi())
        )
}