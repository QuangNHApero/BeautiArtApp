package com.example.aperoaiservice.publicapi

import com.example.aperoaiservice.data.remote.StyleApi
import com.example.aperoaiservice.data.repository.StyleRepositoryImpl
import com.example.aperoaiservice.domain.repository.StyleRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object StyleRepositoryFactory {
    fun create(): StyleRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(StyleApi::class.java)
        return StyleRepositoryImpl(api)
    }
}
