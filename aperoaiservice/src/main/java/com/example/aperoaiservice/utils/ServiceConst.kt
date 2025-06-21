package com.example.aperoaiservice.utils

import androidx.annotation.StringDef

internal object ServiceConst {
    const val NOT_GET_API_TOKEN = "not_get_api_token"
    const val TIME_STAMP_NULL = "time_stamp_null"
    const val SEGMENTATION_NULL = "segmentation_null"
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

internal object AiClothesChangingConst {
    const val CONTENTSTYLE = "contentStyle"
}

internal object AiArtConst {
    const val STYLE_ID = "styleId"
    const val ALPHA = "alpha"
    const val STRENGHT = "strength"
    const val WIDTH = "width"
    const val HEIGHT = "height"
    const val MODE = "mode"
    const val FIX_OPEN_POSE = "fixOpenpose"
    const val BASE_MODEL = "baseModel"
}

internal object AiOutpaintingConst {
    const val LEFT_SCALE = "leftScale"
    const val RIGHT_SCALE = "rightScale"
    const val UP_SCALE = "upScale"
    const val DOWN_SCALE = "downScale"
    const val MAX_SIZE = "maxSize"
}

internal object AiCommonFormDataConst {
    const val FILE = "file"
    const val STYLE = "style"
    const val PROMPT = "prompt"
    const val SEED = "seed"
    const val NEGATIVE_PROMPT = "negativePrompt"
    const val POSITIVE_PROMPT = "positivePrompt"
    const val NUM_INFERENCE_STEPS = "numInferenceSteps"
    const val GUIDANCE_SCALE = "guidanceScale"
    const val ACCEPT_NSFW = "acceptNSFW"
    const val AI_FAMILY = "aiFamily"
    const val MASK = "mask"
}

internal object BodyBeautifyConst {
    const val BODY_SEED = "bodySeed"
    const val TYPE = "type"
}

object FaceBeautyConst {
    const val EYES = "eyes"
    const val V_LINE = "vLine"
    const val SMILE = "smile"
    const val LIPS_COLOR = "lipsColor"
    const val TEETH_WHITENING = "teethWhitening"
    const val SMOOTH = "smooth"
    const val DENOISE = "denoise"
    const val LIPS_COLOR_CLASSIC_RED = "classic_red"
    const val LIPS_COLOR_DEEP_BURGUNDY = "deep_burgundy"
    const val LIPS_COLOR_BRIGHT_PINK = "bright_pink"
    const val LIPS_COLOR_CORAL_PINK = "coral_pink"
    const val LIPS_COLOR_NUDE_PINK = "nude_pink"
    const val LIPS_COLOR_ROSE_PINK = "rose_pink"
    const val LIPS_COLOR_MAUVE = "mauve"
    const val LIPS_COLOR_PLUM = "plum"
    const val LIPS_COLOR_PEACH = "peach"
    const val LIPS_COLOR_CHEERY = "cheery"
}

object FolderNameConst {
    const val CLOTHES = "clothes"
    const val ENHANCE = "enhance"
    const val AI_ART = "ai_art"
    const val EXPAND = "expand"
    const val REMOVE_OBJECT = "remove_object"
    const val BODY_BEAUTIFY = "body_beautify"
    const val FACE_BEAUTY = "face_beauty"
    const val FITTING = "fitting"
    const val RESTORE = "restore"
    const val REMOVE_BACKGROUND = "remove_background"
}

@StringDef(
    ClothType.UPPER,
    ClothType.LOWER,
    ClothType.OVERALL
)
@Retention(AnnotationRetention.SOURCE)
annotation class ClothType {
    companion object {
        const val UPPER = "upper_body"
        const val LOWER = "lower_body"
        const val OVERALL = "full_body"
    }
}

internal object AiStyleConstant {
    const val DEFAULT_STYLE_CLOTHES = "clothesChangingStyles"
    const val DEFAULT_STYLE_TYPE = "imageToImage"
    const val INTERNET_ERROR_CODE = 444
    const val UNKNOWN_ERROR_CODE = 9999
    const val SEGMENT_DEFAULT = "ALL"
}