package com.example.sharkleague.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharkleague.data.model.Partido
import com.example.sharkleague.data.repository.PartidoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PartidosViewModel(private val repository: PartidoRepository) : ViewModel() {

    val allPartidos: StateFlow<List<Partido>> = repository.allPartidos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(partido: Partido) = viewModelScope.launch {
        repository.insert(partido)
    }

    fun update(partido: Partido) = viewModelScope.launch {
        repository.update(partido)
    }

    fun delete(partido: Partido) = viewModelScope.launch {
        repository.delete(partido)
    }
}

class PartidosViewModelFactory(private val repository: PartidoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartidosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PartidosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
