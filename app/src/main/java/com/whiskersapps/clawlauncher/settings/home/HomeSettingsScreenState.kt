package com.whiskersapps.clawlauncher.settings.home

import com.whiskersapps.clawlauncher.shared.model.Settings

data class HomeSettingsScreenState(
    // Loading
    val loading: Boolean = true,
    val settings: Settings = Settings(),
    val homeSettingsDialog: HomeSettingsDialog = HomeSettingsDialog(),
    val apps: List<com.whiskersapps.clawlauncher.shared.model.App> = emptyList(),

    // Settings
    val tintClock: Boolean = Settings.DEFAULT_TINT_CLOCK,
    val clockPlacement: String = Settings.DEFAULT_CLOCK_PLACEMENT,
    val swipeUpToSearch: Boolean = Settings.DEFAULT_SWIPE_UP_TO_SEARCH,
    val showSearchBar: Boolean = Settings.DEFAULT_SHOW_HOME_SEARCH_BAR,
    val showPlaceholder: Boolean = Settings.DEFAULT_SHOW_HOME_SEARCH_BAR_PLACEHOLDER,
    val searchBarRadius: Float = Settings.DEFAULT_APPS_SEARCH_BAR_RADIUS.toFloat(),
    val pillShapeClock: Boolean = Settings.DEFAULT_PILL_SHAPE_CLOCK,
    val setQuickButton: Boolean = Settings.DEFAULT_QUICK_BUTTON,
    val setSecondQuickButton: Boolean = Settings.DEFAULT_SECOND_QUICK_BUTTON,

) {
    data class HomeSettingsDialog(
        val show: Boolean = false,
        val selectedAppOne: String = "",
        val selectedAppTwo: String = "",
        val button: Boolean = true
    )
}
