package com.example.aperoaiservice.generate.network.request

import com.example.aperoaiservice.generate.network.model.PresignedLink
import com.example.aperoaiservice.generate.network.model.style.AiArtRequest
import com.example.aperoaiservice.generate.network.model.style.AiArtResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface ArtServiceAI : PushImageService {

    @GET("/api/v5/image-ai/presigned-link")
    suspend fun getLink(): Response<PresignedLink>

    @POST("/api/v5/image-ai")
    suspend fun genArtAi(
        @Body requestBody: AiArtRequest,
    ): Response<AiArtResponse>

}
