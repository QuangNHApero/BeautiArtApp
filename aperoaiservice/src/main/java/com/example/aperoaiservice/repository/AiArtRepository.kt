package com.example.aperoaiservice.repository

import com.example.aperoaiservice.model.AiArtParams
import com.example.aperoaiservice.response.ResponseState
import java.io.File

interface AiArtRepository  {
    suspend fun genArtAi(params: AiArtParams): ResponseState<File, Throwable>
}