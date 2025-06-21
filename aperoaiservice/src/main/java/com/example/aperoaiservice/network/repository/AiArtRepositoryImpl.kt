package com.example.aperoaiservice.network.repository

import com.example.aperoaiservice.network.model.AiArtParams
import com.example.aperoaiservice.network.model.AiArtRequest
import com.example.aperoaiservice.network.repository.common.HandlerApiWithImageRepo
import com.example.aperoaiservice.network.request.ArtServiceAI
import com.example.aperoaiservice.network.response.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class AiArtRepositoryImpl(
    private val artService: ArtServiceAI,
    private val handlerApiWithImageRepo: HandlerApiWithImageRepo
) : AiArtRepository {
    override suspend fun genArtAi(params: AiArtParams): ResponseState<File, Throwable> {
        return withContext(Dispatchers.IO) {
            handlerApiWithImageRepo.callApiWithImage(
                pathImage = params.pathImageOrigin,
                preSignLink = {
                    artService.getLink()
                },
                callApi = { image ->
                    val aiArtRequest = createMultipartBodyAiArt(params, image)
                    val response = artService.genArtAi(aiArtRequest)
                    if (response.isSuccessful) {
                        ResponseState.Success(response.body()?.data?.url)
                    } else {
                        ResponseState.Error(throw Exception(response.message()), response.code())
                    }
                },
                folderName = "AI_ART"
            )
        }
    }

    override suspend fun fetchCategories(): ResponseState<String, Throwable> {
        return withContext(Dispatchers.IO) {
            try {
                val response = artService.fetchCategories()
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    if (!body.isNullOrEmpty()) {
                        ResponseState.Success(body)
                    } else {
                        ResponseState.Error(Exception("Empty response body"), response.code())
                    }
                } else {
                    ResponseState.Error(Exception(response.message()), response.code())
                }
            } catch (e: Exception) {
                ResponseState.Error(throw Exception(e.message), e.hashCode())
            }
        }
    }

    private fun createMultipartBodyAiArt(
        params: AiArtParams,
        image: String
    ): AiArtRequest {
        return AiArtRequest(
            file = image,
            styleId = params.styleId,
            positivePrompt = params.positivePrompt,
            negativePrompt = params.negativePrompt,
            mode = params.mode,
            baseModel = params.baseModel,
            imageSize = params.imageSize
        )
    }
}
