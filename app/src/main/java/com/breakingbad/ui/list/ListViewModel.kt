package com.breakingbad.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breakingbad.common.Result
import com.breakingbad.data.ApiSource
import com.breakingbad.data.CharacterRepository
import com.breakingbad.data.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel: ViewModel() {

    private val list = MutableLiveData<List<Character>>()
    private val error = MutableLiveData<Int>()
    private val loading = MutableLiveData(true)
    private val repository: CharacterRepository = CharacterRepository(ApiSource())

    init {
        loadCharacters()
    }

    fun getError(): LiveData<Int> = error
    fun loading(): LiveData<Boolean> = loading
    fun getCharacters(): LiveData<List<Character>> = list

    private fun loadCharacters() {
        viewModelScope.launch(Dispatchers.Main) {
            loading.value = true
            when (val result = repository.getCharacters()) {
                is Result.Error -> error.value = result.messageIdRes
                is Result.Success -> {
                    if (result.success) {
                        list.value = result.data
                    }
                }
            }
            loading.value = false
        }
    }
}
