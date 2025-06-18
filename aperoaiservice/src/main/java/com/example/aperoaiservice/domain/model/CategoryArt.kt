package com.example.aperoaiservice.domain.model

data class CategoryArt(
    val _id : String,
    val name: String,
    val styles: List<StyleArt>
)