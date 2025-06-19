package com.example.aperoaiservice.fetch.data.model.mapper

import com.example.aperoaiservice.fetch.data.model.remote.CategoryResponse
import com.example.aperoaiservice.fetch.data.model.remote.StyleResponse
import com.example.aperoaiservice.fetch.domain.model.CategoryArt
import com.example.aperoaiservice.fetch.domain.model.StyleArt

internal fun CategoryResponse.toDomain(): CategoryArt {
    return CategoryArt(
        _id = _id,
        name = name,
        styles = styles.map { it.toDomain() }
    )
}

internal fun StyleResponse.toDomain(): StyleArt {
    return StyleArt(
        _id = _id,
        name = name,
        thumbnail = key,
        positivePrompt = config?.positivePrompt,
        negativePrompt = config?.negativePrompt,
        mode = config?.mode,
        basemodel = config?.basemodel
    )
}
