package com.example.aperoaiservice.generate.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.aperoaiservice.generate.AiServiceConfig
import com.example.aperoaiservice.generate.network.repository.timestamp.TimeStampRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class ApplicationObserver(
    private val applicationScope: CoroutineScope,
    private val aiServiceRepository: TimeStampRepository
) : DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        configAppResumeState()
    }

    private fun configAppResumeState() {
        applicationScope.launch {
            val timeStamp = aiServiceRepository.getTimeStampFromServer()
            timeStamp
                .onSuccess { data ->
                    AiServiceConfig.setTimeStamp(data)
                }
                .onFailure {

                }
        }
    }
}