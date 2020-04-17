package com.lucasdias.search.data.category.remote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

internal interface CategoryService {

    @GET("jokes/categories")
    suspend fun searchFactsBySubjectFromApi(): Response<List<String>>
}
