package com.breakingbad.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.breakingbad.data.ApiSource
import com.breakingbad.data.CharacterRepository
import kotlinx.coroutines.Dispatchers

class ListViewModel: ViewModel() {

    private val loading = MutableLiveData(false)
    private val repository: CharacterRepository = CharacterRepository(ApiSource())

    fun getCharacters() = liveData(Dispatchers.Main) {
        loading.value = true
        emit(repository.getCharacters())
        loading.value = false
    }
}
