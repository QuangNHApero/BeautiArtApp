package com.example.aperoaiservice.generate.network.repository.timestamp

interface TimeStampRepository  {
    suspend fun getTimeStampFromServer(): Result<Long>
}