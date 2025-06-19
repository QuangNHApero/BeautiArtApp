package com.example.aperoaiservice.generate.network.repository.style

import com.example.aperoaiservice.generate.network.model.style.AiArtParams
import com.example.aperoaiservice.generate.network.model.style.AiArtRequest
import com.example.aperoaiservice.generate.network.repository.common.HandlerApiWithImageRepo
import com.example.aperoaiservice.generate.network.request.ArtServiceAI
import com.example.aperoaiservice.generate.network.response.ResponseState
import com.example.aperoaiservice.generate.utils.FolderNameConst.AI_ART
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
                folderName = AI_ART
            )
        }
    }

    private fun createMultipartBodyAiArt(
        params: AiArtParams,
        image: String
    ): AiArtRequest {
        return AiArtRequest(
            file = image,
            style = params.style,
            styleId = params.styleId,
            positivePrompt = params.positivePrompt,
            negativePrompt = params.negativePrompt,
            imageSize = params.imageSize,
            fixHeight = params.fixHeight,
            fixWidth = params.fixWidth,
            fixWidthAndHeight = params.fixWidthAndHeight,
            useControlnet = params.useControlnet,
            applyPulid = params.applyPulid,
            seed = params.seed,
            fastMode = params.fastMode
        )
    }
}
