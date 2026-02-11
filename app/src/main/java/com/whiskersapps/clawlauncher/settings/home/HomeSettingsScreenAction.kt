package com.whiskersapps.clawlauncher.settings.home

import androidx.fragment.app.FragmentActivity
import com.whiskersapps.clawlauncher.settings.security.SecuritySettingsScreenAction

sealed interface HomeSettingsScreenAction {
    data object NavigateBack : HomeSettingsScreenAction
    data class SetShowSearchBar(val show: Boolean) : HomeSettingsScreenAction
    data class SetShowSearchBarPlaceholder(val show: Boolean) : HomeSettingsScreenAction
    data class SetSearchBarRadius(val radius: Float) : HomeSettingsScreenAction
    data class SaveSearchBarRadius(val radius: Float) : HomeSettingsScreenAction
    data class SetSwipeUpToSearch(val swipeUp: Boolean) : HomeSettingsScreenAction
    data class SetTintIcon(val tint: Boolean) : HomeSettingsScreenAction
    data class SetClockPlacement(val placement: String) : HomeSettingsScreenAction
    data class SetPillShapeClock(val pill: Boolean) : HomeSettingsScreenAction
    data class SetQuickButton(val button: Boolean) : HomeSettingsScreenAction
    data class SetSecondQuickButton(val secondButton: Boolean) : HomeSettingsScreenAction


    data object CloseButtonAppDialog : HomeSettingsScreenAction
    data class OpenButtonAppDialog(val button: Boolean) : HomeSettingsScreenAction

    data class ToggleButtonApp(val packageName: String) : HomeSettingsScreenAction
    data object SaveButtonApp : HomeSettingsScreenAction
}