package dev.unusedvariable.vlr.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.unusedvariable.vlr.data.NavState
import dev.unusedvariable.vlr.data.VlrRepository
import dev.unusedvariable.vlr.utils.Constants
import dev.unusedvariable.vlr.utils.TimeElapsed
import dev.unusedvariable.vlr.utils.Waiting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VlrViewModel @Inject constructor(
    private val repository: VlrRepository
) : ViewModel() {

    private var _navState: MutableStateFlow<NavState> = MutableStateFlow(NavState.NEWS)
    val navState: StateFlow<NavState> = _navState

    fun setNavigation(state: NavState) {
        _navState.tryEmit(state)
    }

    lateinit var action: Action

    fun getMatchInfo(matchUrl: String) = repository.getMatchInfo(matchUrl)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Waiting())

    fun getTournamentDetails(matchUrl: String) = repository.getTournamentInfo(matchUrl)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Waiting())

    fun getTournaments() = repository.getTournaments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Waiting())

    fun getAllMatches() = repository.getAllMatchesPreview()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Waiting())

    fun getNews() = repository.getAllNews()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Waiting())

    fun clearCache() {
        TimeElapsed.reset(Constants.KEY_UPCOMING)
        TimeElapsed.reset(Constants.KEY_COMPLETED)
        TimeElapsed.reset(Constants.KEY_TOURNAMENT_ALL)
        TimeElapsed.reset(Constants.KEY_TOURNAMENT_ALL)
        TimeElapsed.reset(Constants.KEY_NEWS)
    }

    private val _forceRefreshCounter = MutableStateFlow(0)
    val forceRefreshCounter: StateFlow<Int> = _forceRefreshCounter

    fun updateCounter() {
        _forceRefreshCounter.value++
    }
}