/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.androidsamples.architecturetemplates.ui.bearitemtype

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import it.androidsamples.architecturetemplates.data.BearItemTypeRepository
import it.androidsamples.architecturetemplates.ui.bearitemtype.BearItemTypeUiState.Error
import it.androidsamples.architecturetemplates.ui.bearitemtype.BearItemTypeUiState.Loading
import it.androidsamples.architecturetemplates.ui.bearitemtype.BearItemTypeUiState.Success
import javax.inject.Inject

@HiltViewModel
class BearItemTypeViewModel @Inject constructor(
    private val bearItemTypeRepository: BearItemTypeRepository
) : ViewModel() {

    val uiState: StateFlow<BearItemTypeUiState> = bearItemTypeRepository
        .bearItemTypes.map<List<String>, BearItemTypeUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addBearItemType(name: String) {
        viewModelScope.launch {
            bearItemTypeRepository.add(name)
        }
    }
}

sealed interface BearItemTypeUiState {
    object Loading : BearItemTypeUiState
    data class Error(val throwable: Throwable) : BearItemTypeUiState
    data class Success(val data: List<String>) : BearItemTypeUiState
}
