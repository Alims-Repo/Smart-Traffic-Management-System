package com.gub.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gub.core.domain.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class ViewModelSystem : ViewModel() {
    private val _systemStatus = MutableStateFlow<Response<ModelSystemStatus>>(Response.Loading)
    val systemStatus : StateFlow<Response<ModelSystemStatus>> = _systemStatus.asStateFlow()


    init {
        viewModelScope.launch {
            while (true) {
                delay(3000)
                _systemStatus.value = Response.Success(
                    ModelSystemStatus(
                        Random.nextInt(0..100),
                        Random.nextInt(0..100),
                        Random.nextInt(0..100),
                        Random.nextInt(0..100)
                    )
                )
            }
        }
    }
}