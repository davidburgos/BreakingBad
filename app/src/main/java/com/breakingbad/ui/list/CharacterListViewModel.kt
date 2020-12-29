package com.breakingbad.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.breakingbad.data.model.Character
import com.breakingbad.data.networking.ApiSource
import com.breakingbad.data.persistance.CharacterDataBase
import com.breakingbad.data.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterListViewModel(application: Application): AndroidViewModel(application) {

    private val list = MediatorLiveData<List<Character>>()
    private val error = MutableLiveData<Int>()
    private val loading = MutableLiveData(true)
    private val repository: CharacterRepository =
        CharacterRepository(ApiSource(), CharacterDataBase.getDatabase(application).getCharacterDAO())

    init {
        loadCharacters()
    }

    fun getError(): LiveData<Int> = error
    fun loading(): LiveData<Boolean> = loading
    fun getCharacters(): LiveData<List<Character>> = list

    fun setCharacterAsFavorite(character: Character) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setCharacterAsFavorite(character.id, character.isFavorite)
        }
    }

    fun getCharacterById(characterId: Int): LiveData<Character> {
        val character = MediatorLiveData<Character>()
        viewModelScope.launch(Dispatchers.Main) {
            loading.value = true
            character.addSource(repository.getCharacterById(characterId)) { value -> character.value = value }
            loading.value = false
        }
        return character
    }

    private fun loadCharacters() {
        viewModelScope.launch(Dispatchers.Main) {
            loading.value = true
            list.addSource(repository.getCharacters()) { value -> list.value = value }
            loading.value = false
        }
    }
}
