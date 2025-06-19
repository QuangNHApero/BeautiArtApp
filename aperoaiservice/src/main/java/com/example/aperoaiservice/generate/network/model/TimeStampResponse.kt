package com.example.aperoaiservice.generate.network.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TimeStampResponse(
    @SerializedName("data") val data: DataTimeStamp? = null
)

@Keep
data class DataTimeStamp(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("timestring")
    val timestring: String
)
