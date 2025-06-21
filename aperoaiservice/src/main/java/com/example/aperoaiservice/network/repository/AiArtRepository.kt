package com.example.aperoaiservice.network.repository

import com.example.aperoaiservice.network.model.AiArtParams
import com.example.aperoaiservice.network.response.ResponseState
import java.io.File

interface AiArtRepository  {
    suspend fun genArtAi(params: AiArtParams): ResponseState<File, Throwable>
    suspend fun fetchCategories(): ResponseState<String, Throwable>
}