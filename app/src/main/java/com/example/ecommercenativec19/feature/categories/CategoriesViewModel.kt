package com.example.ecommercenativec19.feature.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.GetCategoriesUseCase
import com.example.ecommercenativec19.feature.categories.domain.GetSubCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val getSubCategories: GetSubCategoriesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private var categoriesJob: Job? = null
    private var subCategoriesJob: Job? = null

    init {
        loadCategories()
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(id: String) {
        val rail = (_uiState.value.categories as? UiState.Success)?.data ?: return
        val selected = rail.firstOrNull { it.id == id } ?: return
        _uiState.update { it.copy(selectedCategoryId = id, selectedTitle = selected.name) }
        loadSubCategories(id)
    }

    /** Retries whichever region failed: the rail first, otherwise the grid of the selected category. */
    fun onRetry() {
        val state = _uiState.value
        if (state.categories is UiState.Error) {
            loadCategories()
        } else if (state.subCategories is UiState.Error) {
            state.selectedCategoryId?.let(::loadSubCategories)
        }
    }

    private fun loadCategories() {
        categoriesJob?.cancel()
        // The grid cannot load before we know which category is selected, so it waits with the rail.
        _uiState.update { it.copy(categories = UiState.Loading, subCategories = UiState.Loading) }
        categoriesJob = viewModelScope.launch {
            when (val result = getCategories()) {
                is AppResult.Failure -> {
                    // Without a rail there is no grid to load, so both regions show the same error.
                    val error = UiState.Error(result.error)
                    _uiState.update { it.copy(categories = error, subCategories = error) }
                }
                is AppResult.Success -> showCategories(result.data)
            }
        }
    }

    private fun showCategories(categories: List<Category>) {
        if (categories.isEmpty()) {
            _uiState.update { it.copy(categories = UiState.Empty, subCategories = UiState.Empty) }
            return
        }
        val first = categories.first()
        _uiState.update {
            it.copy(categories = UiState.Success(categories), selectedCategoryId = first.id, selectedTitle = first.name)
        }
        loadSubCategories(first.id)
    }

    private fun loadSubCategories(categoryId: String) {
        subCategoriesJob?.cancel()
        _uiState.update { it.copy(subCategories = UiState.Loading) }
        subCategoriesJob = viewModelScope.launch {
            when (val result = getSubCategories(categoryId)) {
                is AppResult.Failure -> _uiState.update { it.copy(subCategories = UiState.Error(result.error)) }
                is AppResult.Success -> {
                    val items = result.data
                    _uiState.update {
                        it.copy(subCategories = if (items.isEmpty()) UiState.Empty else UiState.Success(items))
                    }
                }
            }
        }
    }
}
