package com.thechance.viewmodel.profile.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thechance.ui.utilities.Event
import com.thechance.viewmodel.profile.logout.LogoutUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(private val logoutUseCase: com.devfalah.usecases.LogoutUseCase) :
    ViewModel() {

    private val _logoutUIEvent: MutableStateFlow<Event<LogoutUIEvent?>> = MutableStateFlow(Event(null))
    val logoutUIEvent= _logoutUIEvent.asStateFlow()
    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
            logoutEvent()
        }
    }

    fun onCloseDialog() {
        _logoutUIEvent.update { Event(LogoutUIEvent.CloseDialogEvent) }
    }

    private fun logoutEvent() {
        _logoutUIEvent.update { Event(LogoutUIEvent.LogoutEvent) }
    }
}