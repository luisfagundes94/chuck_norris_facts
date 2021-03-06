package com.lucasdias.search.domain.usecase

import com.lucasdias.core_components.base.domain.usecase.BaseUseCase
import com.lucasdias.search.domain.repository.SearchHistoricRepository

internal class SetSearchHistoric(
    private val searchHistoricRepository: SearchHistoricRepository
) : BaseUseCase<String, Unit?> {

    override operator fun invoke(parameter: String?) =
        parameter?.let { nonNullParameter ->
            searchHistoricRepository.setSearch(search = nonNullParameter)
        }
}
