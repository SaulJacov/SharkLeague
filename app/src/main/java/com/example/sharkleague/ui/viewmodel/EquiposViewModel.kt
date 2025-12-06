package com.example.sharkleague.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sharkleague.data.model.Equipo
import com.example.sharkleague.data.repository.EquipoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EquiposViewModel(private val repository: EquipoRepository) : ViewModel() {

    val allEquipos: StateFlow<List<Equipo>> = repository.allEquipos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(equipo: Equipo) = viewModelScope.launch {
        repository.insert(equipo)
    }

    fun update(equipo: Equipo) = viewModelScope.launch {
        repository.update(equipo)
    }

    fun delete(equipo: Equipo) = viewModelScope.launch {
        repository.delete(equipo)
    }
}

class EquiposViewModelFactory(private val repository: EquipoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquiposViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EquiposViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
