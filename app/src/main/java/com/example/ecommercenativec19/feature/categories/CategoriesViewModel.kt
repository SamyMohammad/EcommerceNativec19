package com.example.ecommercenativec19.feature.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.categories.domain.GetCategoriesUseCase
import com.example.ecommercenativec19.feature.categories.domain.GetSubCategoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(
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
        val rail = (_uiState.value.categories as? LoadState.Content)?.data ?: return
        val selected = rail.firstOrNull { it.id == id } ?: return
        _uiState.update { current ->
            current.copy(
                categories = LoadState.Content(rail.map { it.copy(isSelected = it.id == id) }),
                selectedTitle = selected.label,
            )
        }
        loadSubCategories(id)
    }

    /** Retries whichever region failed: the rail first, otherwise the grid of the selected category. */
    fun onRetry() {
        val state = _uiState.value
        if (state.categories is LoadState.Error) {
            loadCategories()
        } else if (state.subCategories is LoadState.Error) {
            val rail = (state.categories as? LoadState.Content)?.data.orEmpty()
            rail.firstOrNull { it.isSelected }?.let { loadSubCategories(it.id) }
        }
    }

    private fun loadCategories() {
        categoriesJob?.cancel()
        // The grid cannot load before we know which category is selected, so it waits with the rail.
        _uiState.update { it.copy(categories = LoadState.Loading, subCategories = LoadState.Loading) }
        categoriesJob = viewModelScope.launch {
            when (val result = getCategories()) {
                is AppResult.Failure -> {
                    // Without a rail there is no grid to load, so both regions show the same error.
                    val error = LoadState.Error(result.error)
                    _uiState.update { it.copy(categories = error, subCategories = error) }
                }
                is AppResult.Success -> showCategories(result.data.map { RailCategory(it.id, it.name) })
            }
        }
    }

    private fun showCategories(categories: List<RailCategory>) {
        if (categories.isEmpty()) {
            _uiState.update { it.copy(categories = LoadState.Empty, subCategories = LoadState.Empty) }
            return
        }
        val rail = categories.mapIndexed { index, category -> category.copy(isSelected = index == 0) }
        _uiState.update { it.copy(categories = LoadState.Content(rail), selectedTitle = rail.first().label) }
        loadSubCategories(rail.first().id)
    }

    private fun loadSubCategories(categoryId: String) {
        subCategoriesJob?.cancel()
        _uiState.update { it.copy(subCategories = LoadState.Loading) }
        subCategoriesJob = viewModelScope.launch {
            when (val result = getSubCategories(categoryId)) {
                is AppResult.Failure -> _uiState.update { it.copy(subCategories = LoadState.Error(result.error)) }
                is AppResult.Success -> {
                    val items = result.data.map { SubCategoryItem(it.id, it.name) }
                    _uiState.update {
                        it.copy(subCategories = if (items.isEmpty()) LoadState.Empty else LoadState.Content(items))
                    }
                }
            }
        }
    }
}
