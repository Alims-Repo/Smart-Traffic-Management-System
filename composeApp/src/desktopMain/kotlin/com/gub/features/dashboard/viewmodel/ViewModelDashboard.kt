package com.gub.features.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gub.core.domain.Response
import com.gub.features.dashboard.data.repository.SystemStatusWebSocket
import com.gub.models.ModelSystemStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelDashboard : ViewModel() {

    private val _systemStatus = MutableStateFlow<Response<ModelSystemStatus>>(Response.Loading)
    val systemStatus : StateFlow<Response<ModelSystemStatus>> = _systemStatus.asStateFlow()

    init {
        viewModelScope.launch {
            val socket = SystemStatusWebSocket {
                _systemStatus.value = it
            }
            socket.connect()
        }
    }
}