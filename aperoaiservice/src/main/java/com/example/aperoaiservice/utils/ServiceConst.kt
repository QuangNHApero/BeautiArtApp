package com.example.aperoaiservice.utils


internal object ServiceConst {
    const val TIME_OUT_DURATION = 60000L
}

object ServiceError {
    const val UNKNOWN_ERROR_MESSAGE = "unknown_error_message"
    const val ERROR_TIME_OUT_MESSAGE = "error_time_out_message"
    const val SAVE_FILE_ERROR = "save_file_failed"
    const val CODE_UNKNOWN_ERROR = 9999
    const val CODE_FILE_NULL = 1000
    const val CODE_PARSING_ERROR = 1001
    const val CODE_TIMEOUT_ERROR = 1002
    const val CODE_PUSH_IMAGE_ERROR = 1003
    const val CODE_GET_LINK_ERROR = 1004
}