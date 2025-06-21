package com.example.beautisdk.utils.repository

import android.util.Log
import com.example.aperoaiservice.network.repository.AiArtRepository
import com.example.aperoaiservice.network.response.ResponseState
import com.example.beautisdk.ui.data.model.ApiResponse
import com.example.beautisdk.ui.data.model.CategoryArt
import com.example.beautisdk.utils.pref.VslSharedPref
import com.example.beautisdk.utils.pref.VslSharedPrefConst.PREF_ART_TEMPLATE_STYLES
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ArtRepositoryImpl(private val aiArtRepository: AiArtRepository) : ArtRepository {
    companion object{
        private const val TAG = "ArtRepositoryImpl"
    }

    override suspend fun loadFromRemote(): Flow<List<CategoryArt>> = flow {
        try {
            val resultData = aiArtRepository.fetchCategories()

            when (resultData) {
                is ResponseState.Success -> {
                    val json = resultData.data.orEmpty()
                    Log.d(TAG, "Result data: $json")
                    if (json.isEmpty()) {
                        emitAll(loadFromLocal())
                    } else {
                        VslSharedPref.putValue(PREF_ART_TEMPLATE_STYLES, json)
                        val parsed = Gson().fromJson(json, ApiResponse::class.java)
                        Log.d(TAG, "Parsed result: $parsed")
                        emit(parsed.data.items)
                    }
                }

                is ResponseState.Error -> {
                    Log.e(TAG, "Error: ${resultData.error}")
                    emitAll(loadFromLocal())
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            emitAll(loadFromLocal())
        }
    }.flowOn(Dispatchers.IO)


    private fun loadFromLocal(): Flow<List<CategoryArt>> = flow {
        try {
            val json = VslSharedPref.getValue(PREF_ART_TEMPLATE_STYLES, "")
            if (json.isEmpty()) {
                emit(emptyList())
            } else {
                val parsed = Gson().fromJson(json, ApiResponse::class.java)
                emit(parsed.data.items)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)
}