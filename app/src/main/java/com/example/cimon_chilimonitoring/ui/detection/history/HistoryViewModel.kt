package com.example.cimon_chilimonitoring.ui.detection.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cimon_chilimonitoring.data.local.entity.HistoryEntity
import com.example.cimon_chilimonitoring.data.local.repository.HistoryRepo
import com.example.cimon_chilimonitoring.data.remote.response.ResultsItemBlog
import com.example.cimon_chilimonitoring.data.remote.response.ResultsItemHistory
import com.example.cimon_chilimonitoring.helper.di.AppExecutors

class HistoryViewModel(private val eventRepo: HistoryRepo) : ViewModel()   {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _favoriteEventsEntity = MutableLiveData<List<HistoryEntity>>()
    val favoriteEventsEntity: LiveData<List<HistoryEntity>> = _favoriteEventsEntity

    private val _listStory = MutableLiveData<List<ResultsItemHistory>?>()
    val listStory: LiveData<List<ResultsItemHistory>?> = _listStory

    init {
        observeHistory()
    }

    private fun observeHistory() {
        _isLoading.value = true
        eventRepo.getHistory().observeForever { events ->
            _isLoading.value = false
            _favoriteEventsEntity.value = events
        }
    }

    suspend fun getHistory(token:String){
        _isLoading.value = true
        try {
            val response = ApiConfig.getApiService(token).getHistory()
            _listStory.value = response.data?.results?.filterNotNull()
            Log.d("HistoryVM", "Success fetching story ${_listStory.value}")
        } catch (e: Exception) {
            Log.e("HistoryVM", "Error fetching story", e)
            _listStory.value = null
        } finally {
            _isLoading.value = false
        }
    }
}