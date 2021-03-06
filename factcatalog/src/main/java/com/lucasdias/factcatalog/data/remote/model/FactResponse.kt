package com.lucasdias.factcatalog.data.remote.model

internal data class FactResponse(
    val id: String,
    val value: String,
    val categories: List<String>,
    val url: String,
    val icon_url: String,
    val created_at: String,
    val update_at: String
)
