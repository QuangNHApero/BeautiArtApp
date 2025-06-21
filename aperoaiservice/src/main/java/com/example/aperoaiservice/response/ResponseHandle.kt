package com.example.aperoaiservice.response

import com.example.aperoaiservice.network.ApiClient
import com.example.aperoaiservice.utils.ServiceError.CODE_TIMEOUT_ERROR
import com.example.aperoaiservice.utils.ServiceError.CODE_UNKNOWN_ERROR
import com.example.aperoaiservice.utils.ServiceError.ERROR_TIME_OUT_MESSAGE
import com.example.aperoaiservice.utils.ServiceError.UNKNOWN_ERROR_MESSAGE
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

internal suspend fun Call<ResponseBody>.enqueueCallResult(): ResponseState<Response<ResponseBody>, Throwable> =
    withTimeoutOrNull(ApiClient.REQUEST_TIMEOUT.seconds) {
        suspendCoroutine { continuation ->
            this@enqueueCallResult.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        !response.isSuccessful -> {
                            continuation.resume(
                                ResponseState.Error(
                                    Throwable(response.errorBody().toString()),
                                    response.code()
                                )
                            )
                        }

                        response.body() != null -> {
                            continuation.resume(ResponseState.Success(response))
                        }


                        else -> {
                            continuation.resume(
                                ResponseState.Error(
                                    Throwable(UNKNOWN_ERROR_MESSAGE),
                                    response.code()
                                )
                            )

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    continuation.resume(ResponseState.Error(t, CODE_UNKNOWN_ERROR))
                }
            })
        }
    } ?: ResponseState.Error(Throwable(ERROR_TIME_OUT_MESSAGE), CODE_TIMEOUT_ERROR)