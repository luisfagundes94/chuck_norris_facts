package com.lucasdias.factcatalog.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucasdias.factcatalog.domain.model.Fact
import com.lucasdias.factcatalog.domain.sealedclass.Error
import com.lucasdias.factcatalog.domain.sealedclass.RequestStatus
import com.lucasdias.factcatalog.domain.sealedclass.SuccessWithoutResult
import com.lucasdias.factcatalog.domain.usecase.DeleteAllFactsFromDatabase
import com.lucasdias.factcatalog.domain.usecase.GetAllFactsFromDatabase
import com.lucasdias.factcatalog.domain.usecase.SearchFactsBySubjectFromApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class FactCatalogViewModel(
    internal val getAllFactsFromDatabase: GetAllFactsFromDatabase,
    private val searchFactsBySubjectFromApi: SearchFactsBySubjectFromApi,
    private val deleteAllFactsFromDatabase: DeleteAllFactsFromDatabase
) : ViewModel() {

    internal var coroutineContext = Dispatchers.IO
    internal var showAnErrorScreenLiveData = MutableLiveData<Unit>()
    internal var showAnEmptySearchScreenLiveData = MutableLiveData<Unit>()
    internal var turnOnLoadingLiveData = MutableLiveData<Unit>()
    internal var turnOffLoadingLiveData = MutableLiveData<Unit>()
    private var hasNetworkConnectivity = true

    fun deleteAllFacts() = deleteAllFactsFromDatabase.invoke()
    fun updateFactsLiveData(): LiveData<List<Fact>> = getAllFactsFromDatabase()
    fun showAnErrorScreenLiveData(): LiveData<Unit> = showAnErrorScreenLiveData
    fun showAnEmptySearchScreenLiveData(): LiveData<Unit> = showAnEmptySearchScreenLiveData
    fun turnOnLoadingLiveData(): LiveData<Unit> = turnOnLoadingLiveData
    fun turnOffLoadingLiveData(): LiveData<Unit> = turnOffLoadingLiveData

    fun searchFactsBySubject(subject: String) {
        if (subject.isEmpty()) return
        if (hasNetworkConnectivity.not()) return

        CoroutineScope(coroutineContext).launch {
            turnOnLoadingLiveData.postValue(Unit)
            val requestStatus = searchFactsBySubjectFromApi.invoke(subject = subject)
            requestStatusHandler(requestStatus = requestStatus)
            turnOffLoadingLiveData.postValue(Unit)
        }
    }

    private fun requestStatusHandler(requestStatus: RequestStatus) {
        when (requestStatus) {
            Error -> {
                showAnErrorScreenLiveData.postValue(Unit)
            }
            SuccessWithoutResult -> {
                showAnEmptySearchScreenLiveData.postValue(Unit)
            }
            else -> {}
        }
    }

    fun updateConnectivityStatus(hasNetworkConnectivity: Boolean) {
        this.hasNetworkConnectivity = hasNetworkConnectivity
    }
}
