package com.example.aperoaiservice.generate.network.repository.timestamp

import com.example.aperoaiservice.generate.network.request.TimeStampServiceAI
import com.example.aperoaiservice.generate.utils.ServiceConst.TIME_STAMP_NULL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class TimeStampRepositoryImpl(
    private val timeStampService: TimeStampServiceAI,
) : TimeStampRepository {
    override suspend fun getTimeStampFromServer(): Result<Long> {
        return withContext(Dispatchers.IO) {
            try {
                val response = timeStampService.getTimestamp()
                if (response.isSuccessful) {
                    val timeStampResponse = response.body()
                    val timeStamp = timeStampResponse?.data?.timestamp
                    if (timeStamp != null) {
                        Result.success(timeStamp)
                    } else {
                        Result.failure(NullPointerException(TIME_STAMP_NULL))
                    }
                } else {
                    Result.failure(Throwable(response.message()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}