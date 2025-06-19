package com.example.aperoaiservice.generate.network.repository.style

import com.example.aperoaiservice.generate.network.model.style.AiArtParams
import com.example.aperoaiservice.generate.network.response.ResponseState
import java.io.File

interface AiArtRepository  {
    suspend fun genArtAi(params: AiArtParams): ResponseState<File, Throwable>
}