package com.whiskersapps.clawlauncher.launcher.home

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.whiskersapps.clawlauncher.settings.home.HomeSettingsScreenAction
import com.whiskersapps.clawlauncher.settings.home.HomeSettingsScreenState
import com.whiskersapps.clawlauncher.shared.model.Settings
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_BUTTON_APP_ONE
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_BUTTON_APP_TWO
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_CLOCK_PLACEMENT
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_DARK_MODE
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_HOME_SEARCH_BAR_RADIUS
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_PILL_SHAPE_CLOCK
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_SHOW_HOME_SEARCH_BAR
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_SWIPE_UP_TO_SEARCH
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_TINT_CLOCK
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_QUICK_BUTTON
import com.whiskersapps.clawlauncher.shared.model.Settings.Companion.DEFAULT_SECOND_QUICK_BUTTON

data class HomeScreenState(
    // Loading
    val loading: Boolean = true,
    val apps: List<com.whiskersapps.clawlauncher.shared.model.App> = emptyList(),

    // Widgets
    val clock: String = "",
    val date: String = "",

    // Settings
    val clockPlacement: String = DEFAULT_CLOCK_PLACEMENT,
    val enableSwipeUp: Boolean = DEFAULT_SWIPE_UP_TO_SEARCH,
    val showSearchBar: Boolean = DEFAULT_SHOW_HOME_SEARCH_BAR,
    val showPlaceholder: Boolean = DEFAULT_SHOW_HOME_SEARCH_BAR,
    val searchBarRadius: Float = DEFAULT_HOME_SEARCH_BAR_RADIUS.toFloat(),
    val tintClock: Boolean = DEFAULT_TINT_CLOCK,
    val pillShapeClock: Boolean = DEFAULT_PILL_SHAPE_CLOCK,
    val setQuickButton: Boolean = DEFAULT_QUICK_BUTTON,
    val setSecondQuickButton: Boolean = DEFAULT_SECOND_QUICK_BUTTON,
    val buttonAppOne: String = DEFAULT_BUTTON_APP_ONE,
    val buttonAppTwo: String = DEFAULT_BUTTON_APP_TWO,

    // Dialogs
    val showMenuDialog: Boolean = false,
    val showLockScreenDialog: Boolean = false
)