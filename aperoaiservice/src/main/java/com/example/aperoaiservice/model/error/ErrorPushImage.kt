package com.example.aperoaiservice.model.error

data class ErrorPushImage(
    override val cause: Throwable? = null,
    override val message: String? = null,
    val code: Int? = null
) : Throwable(cause = cause, message = message)