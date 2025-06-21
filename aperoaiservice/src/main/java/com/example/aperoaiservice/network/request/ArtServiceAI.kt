package com.example.aperoaiservice.network.request

import com.example.aperoaiservice.network.model.AiArtRequest
import com.example.aperoaiservice.network.model.AiArtResponse
import com.example.aperoaiservice.network.model.PresignedLink
import okhttp3.ResponseBody
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

    @GET("https://api-style-manager.apero.vn/category?project=techtrek&segmentValue=IN&styleType=imageToImage&isApp=true")
    suspend fun fetchCategories(): Response<ResponseBody>
}
