package com.example.aperoaiservice.utils

import com.apero.aigenerate.network.repository.common.HandlerApiWithImageImpl
import com.example.aperoaiservice.network.ApiClient
import com.example.aperoaiservice.repository.AiArtRepository
import com.example.aperoaiservice.repository.AiArtRepositoryImpl

object ServiceProvider {

    private val apiClient: ApiClient = ApiClient.getInstance()

    fun provideAiArtRepository(): AiArtRepository =
        AiArtRepositoryImpl(
            apiClient.buildArtAi(),
            HandlerApiWithImageImpl(apiClient.buildArtAi())
        )

}
