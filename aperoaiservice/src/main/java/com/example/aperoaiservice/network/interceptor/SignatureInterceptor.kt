package com.example.aperoaiservice.network.interceptor

import com.apero.signature.SignatureParser
import com.example.aperoaiservice.AIServiceEntry
import okhttp3.Interceptor
import okhttp3.Response

internal class SignatureInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val signatureData = SignatureParser.parseData(
            AIServiceEntry.apiKey,
            AIServiceEntry.key,
            AIServiceEntry.timeStamp
        )
        val tokenIntegrity = signatureData.tokenIntegrity.ifEmpty { "" }

        val headers = hashMapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "device" to "android",
            "x-api-signature" to signatureData.signature,
            "x-api-timestamp" to signatureData.timeStamp.toString(),
            "x-api-keyid" to signatureData.keyId,
            "x-api-token" to tokenIntegrity,
            "x-api-bundleId" to AIServiceEntry.applicationId,
            "App-name" to AIServiceEntry.projectName,
        )
        val requestBuilder = chain.request().newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        return chain.proceed(requestBuilder.build())
    }
}