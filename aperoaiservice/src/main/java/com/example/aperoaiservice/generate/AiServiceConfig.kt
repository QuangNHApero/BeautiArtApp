package com.example.aperoaiservice.generate

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.aperoaiservice.generate.network.repository.timestamp.TimeStampRepository
import com.example.aperoaiservice.generate.utils.ApplicationObserver
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AiServiceConfig {

    internal var isDebugMode : Boolean = false
    internal var projectName: String = ""
    internal var applicationId: String = ""
    var apiKey: String = ""
    internal lateinit var application: Application

    fun setUpData(
        projectName: String,
        applicationId: String,
        apiKey: String,
        application: Application,
        isDebugMode : Boolean = false
    ) {
        AiServiceConfig.projectName = projectName
        AiServiceConfig.applicationId = applicationId
        AiServiceConfig.apiKey = apiKey
        AiServiceConfig.application = application
        AiServiceConfig.isDebugMode = isDebugMode
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            ApplicationObserver(
                CoroutineScope(
                    SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ ->

                    }),
                ServiceFactory.getService<TimeStampRepository>()
            )
        )
    }

    private var timeDiff: Long = 0L
    val timeStamp: Long get() = System.currentTimeMillis() + timeDiff

    fun setTimeStamp(serverTimestamp: Long) {
        val clientTimestamp = System.currentTimeMillis()
        timeDiff = serverTimestamp - clientTimestamp
    }

    fun setupIntegrity(
        cloudProjectNumber: Long,
        requestHash: String,
        application: Application,
        urlIntegrity: String,
    ) {
        AperoIntegrity.getInstance().init(
            application, cloudProjectNumber, requestHash, SignatureInterceptor(), null, false
        )
        AperoIntegrity.getInstance().configBaseURL(urlIntegrity)
        AperoIntegrity.getInstance().setRequestNewToken(false)
    }
}