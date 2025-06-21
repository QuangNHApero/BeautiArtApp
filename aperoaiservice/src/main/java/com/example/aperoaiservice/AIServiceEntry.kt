package com.example.aperoaiservice

import android.app.Application

object AIServiceEntry {
    internal var projectName: String = ""
    internal var applicationId: String = ""
    var apiKey: String = ""
    internal var key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5z8DrSdxAFy5ju27JzxUDGD5OdPRnKVrXPypiBVT7NK4ltgbcud3+Li3H1DiAFNvaSDPumZMEbAkfGWZ6s3KtiI7TRmZwQ2yyH6mug6GhrCLD6CZJUQ2CPmhO3JYTYOgN53E6hwm/Teb9I156S04qHjLLLBxk9Mklu5X06kdhMBYwHFAZ3oByeoWUryrQC0Mv9C5ZahKzoQNuJNL2sv+ws2e5Zaj8Rid4AjhvqB6dYhWP4QM+0IiNjs/j08aRgcyOrenbQEIieU+XF6mQWF2Jfg317e0KjWnpru+uPVVgrEn9rNvQeXu2u4SZhT6rnLQzBLbJrngNcNw3gXfxxsoowIDAQAB"
    internal lateinit var application: Application

    fun initialize(
        projectName: String,
        applicationId: String,
        apiKey: String,
        application: Application,
    ) {
        AIServiceEntry.projectName = projectName
        AIServiceEntry.applicationId = applicationId
        AIServiceEntry.apiKey = apiKey
        AIServiceEntry.application = application

        setTimeStamp(System.currentTimeMillis())
    }

    private var timeDiff: Long = 0L
    val timeStamp: Long get() = System.currentTimeMillis() + timeDiff

    fun setTimeStamp(serverTimestamp: Long) {
        val clientTimestamp = System.currentTimeMillis()
        timeDiff = serverTimestamp - clientTimestamp
    }

}