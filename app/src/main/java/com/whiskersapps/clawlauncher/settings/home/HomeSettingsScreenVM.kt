package com.whiskersapps.clawlauncher.settings.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiskersapps.clawlauncher.launcher.apps.di.AppsRepo
import com.whiskersapps.clawlauncher.settings.di.SettingsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeSettingsScreenVM(
    private val settingsRepo: SettingsRepo,
    private val appsRepo: AppsRepo
) : ViewModel() {

    private val _state = MutableStateFlow(HomeSettingsScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepo.settings.collect { settings ->
                _state.update {
                    it.copy(
                        loading = false,
                        apps = appsRepo.allApps,
                        tintClock = settings.tintClock,
                        clockPlacement = settings.clockPlacement,
                        swipeUpToSearch = settings.swipeUpToSearch,
                        showSearchBar = settings.showHomeSearchBar,
                        showPlaceholder = settings.showHomeSearchBarPlaceholder,
                        searchBarRadius = settings.homeSearchBarRadius.toFloat(),
                        pillShapeClock = settings.pillShapeClock,
                        setQuickButton = settings.showQuickButton,
                        setSecondQuickButton = settings.showSecondQuickButton,
                        homeSettingsDialog = it.homeSettingsDialog.copy(selectedAppOne = settings.buttonAppOne, selectedAppTwo = settings.buttonAppTwo)
                    )
                }
            }
        }
    }

    fun onAction(action: HomeSettingsScreenAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (action) {

                is HomeSettingsScreenAction.SetSearchBarRadius -> settingsRepo.setHomeSearchBarRadius(
                    action.radius.toInt()
                )

                is HomeSettingsScreenAction.SetShowSearchBar -> settingsRepo.setShowHomeSearchBar(
                    action.show
                )

                is HomeSettingsScreenAction.SetShowSearchBarPlaceholder -> settingsRepo.setShowHomeSearchBarPlaceholder(
                    action.show
                )

                HomeSettingsScreenAction.NavigateBack -> {}

                is HomeSettingsScreenAction.SaveSearchBarRadius -> settingsRepo.setHomeSearchBarRadius(
                    action.radius.toInt()
                )

                is HomeSettingsScreenAction.SetSwipeUpToSearch -> settingsRepo.setSwipeUpToSearch(
                    action.swipeUp
                )

                is HomeSettingsScreenAction.SetTintIcon -> settingsRepo.setTintClock(action.tint)

                is HomeSettingsScreenAction.SetClockPlacement -> settingsRepo.setClockPlacement(
                    action.placement
                )

                is HomeSettingsScreenAction.SetPillShapeClock -> settingsRepo.setPillShapeClock(
                    action.pill
                )

                is HomeSettingsScreenAction.SetQuickButton -> settingsRepo.setQuickButton(
                    action.button
                )

                is HomeSettingsScreenAction.SetSecondQuickButton -> settingsRepo.setSecondQuickButton(
                    action.secondButton
                )

                HomeSettingsScreenAction.CloseButtonAppDialog -> closeButtonAppDialog()
                is HomeSettingsScreenAction.OpenButtonAppDialog -> showButtonAppDialog(action.button)
                HomeSettingsScreenAction.SaveButtonApp -> saveButtonApp()
                is HomeSettingsScreenAction.ToggleButtonApp -> toggleButtonApp(action.packageName)
            }
        }
    }

    private fun closeButtonAppDialog() {
        _state.update {
            it.copy(
                homeSettingsDialog = it.homeSettingsDialog.copy(
                    show = false,
                    selectedAppOne = state.value.homeSettingsDialog.selectedAppOne,
                    selectedAppTwo = state.value.homeSettingsDialog.selectedAppTwo
                )
            )
        }
    }

    private fun showButtonAppDialog(button: Boolean) {
        _state.update { it.copy(homeSettingsDialog = it.homeSettingsDialog.copy(show = true)) }
        _state.update { it.copy(homeSettingsDialog = it.homeSettingsDialog.copy(button = button)) }
    }

    private fun toggleButtonApp(packageName: String) {
        if (_state.value.homeSettingsDialog.button)
            _state.update { it.copy(homeSettingsDialog = it.homeSettingsDialog.copy(selectedAppOne = packageName)) }
        else _state.update { it.copy(homeSettingsDialog = it.homeSettingsDialog.copy(selectedAppTwo = packageName)) }
    }

    private fun saveButtonApp() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.value.homeSettingsDialog.button)
                settingsRepo.setButtonAppOne(state.value.homeSettingsDialog.selectedAppOne)
            else
                settingsRepo.setButtonAppTwo(state.value.homeSettingsDialog.selectedAppTwo)

            closeButtonAppDialog()
        }
    }

}