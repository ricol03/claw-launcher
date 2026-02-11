package com.whiskersapps.clawlauncher.settings.home

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.whiskersapps.clawlauncher.R
import com.whiskersapps.clawlauncher.settings.security.HiddenAppsDialog
import com.whiskersapps.clawlauncher.settings.security.SecuritySettingsScreenAction
import com.whiskersapps.clawlauncher.settings.style.StyleSettingsScreenIntent
import com.whiskersapps.clawlauncher.shared.view.composables.ContentColumn
import com.whiskersapps.clawlauncher.shared.view.composables.NavBar
import com.whiskersapps.clawlauncher.shared.view.composables.SimpleSetting
import com.whiskersapps.clawlauncher.shared.view.composables.SliderSetting
import com.whiskersapps.clawlauncher.shared.view.composables.SwitchSetting
import com.whiskersapps.clawlauncher.shared.view.composables.settingPadding
import com.whiskersapps.clawlauncher.shared.view.theme.REGULAR_LABEL_STYLE
import com.whiskersapps.clawlauncher.shared.view.theme.SMALL_LABEL_STYLE
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeSettingsScreenRoot(
    navController: NavController,
    vm: HomeSettingsScreenVM = koinViewModel(),
) {

    HomeSettingsScreen(
        onAction = { action ->
            when (action) {
                HomeSettingsScreenAction.NavigateBack -> navController.navigateUp()
                else -> vm.onAction(action)
            }
        },
        vm = vm
    )
}

@Composable
fun HomeSettingsScreen(
    onAction: (HomeSettingsScreenAction) -> Unit,
    vm: HomeSettingsScreenVM,
    state: HomeSettingsScreenState = vm.state.collectAsState().value,
) {
    ContentColumn(
        useSystemBarsPadding = true,
        navigationBar = {
            NavBar(navigateBack = { onAction(HomeSettingsScreenAction.NavigateBack) })
        },
        loading = state.loading
    ) {

        SwitchSetting(
            title = stringResource(R.string.HomeSettings_tint_clock),
            description = stringResource(R.string.HomeSettings_tint_clock_description),
            value = state.tintClock,
            onValueChange = {
                onAction(HomeSettingsScreenAction.SetTintIcon(it))
            }
        )

        SwitchSetting(
            title = stringResource(R.string.HomeSettings_pill_shape),
            description = stringResource(R.string.HomeSettings_pill_shape_description),
            value = state.pillShapeClock,
            onValueChange = {
                onAction(HomeSettingsScreenAction.SetPillShapeClock(it))
            }
        )

        Column(modifier = Modifier.settingPadding()) {
            Text(
                text = "Clock Placement",
                style = REGULAR_LABEL_STYLE,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Display the clock in a different position",
                style = SMALL_LABEL_STYLE,
                color = MaterialTheme.colorScheme.onBackground
            )

            MultiChoiceSegmentedButtonRow {
                listOf("top", "center").forEachIndexed { index, choice ->
                    SegmentedButton(
                        checked = state.clockPlacement == choice.lowercase(),
                        onCheckedChange = {
                            onAction(HomeSettingsScreenAction.SetClockPlacement(choice))
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = 2
                        ),
                        label = {
                            Text(
                                text = when (choice) {
                                    "top" -> "Top"
                                    else -> "Center"
                                }
                            )
                        },
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            activeBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }

        SwitchSetting(
            title = stringResource(R.string.HomeSettings_swipe_to_search),
            description = stringResource(R.string.HomeSettings_swipe_to_search_description),
            value = state.swipeUpToSearch,
            onValueChange = {
                onAction(HomeSettingsScreenAction.SetSwipeUpToSearch(it))
            }
        )

        SwitchSetting(
            title = stringResource(R.string.HomeSettings_search_bar),
            description = stringResource(R.string.HomeSettings_search_bar_description),
            value = state.showSearchBar,
            onValueChange = {
                onAction(HomeSettingsScreenAction.SetShowSearchBar(it))
            }
        )

        if (state.showSearchBar) {
            SwitchSetting(
                title = stringResource(R.string.HomeSettings_quick_button),
                description = stringResource(R.string.HomeSettings_quick_button_description),
                value = state.setQuickButton,
                onValueChange = {
                    onAction(HomeSettingsScreenAction.SetQuickButton(it))
                }
            )

            if (state.setQuickButton) {
                SimpleSetting(
                    title = "Select app (first button)",
                    value = "Select the app to be available as the first quick button",
                    onClick = {
                        onAction(HomeSettingsScreenAction.OpenButtonAppDialog(true))
                    }
                )

                SwitchSetting(
                    title = stringResource(R.string.HomeSettings_second_quick_button),
                    description = stringResource(R.string.HomeSettings_second_quick_button_description),
                    value = state.setSecondQuickButton,
                    onValueChange = {
                        onAction(HomeSettingsScreenAction.SetSecondQuickButton(it))
                    }
                )

                if (state.setSecondQuickButton) {
                    SimpleSetting(
                        title = "Select app (second button)",
                        value = "Select the app to be available as the second quick button",
                        onClick = {
                            onAction(HomeSettingsScreenAction.OpenButtonAppDialog(false))
                        }
                    )
                }
            }
        }

        SwitchSetting(
            title = stringResource(R.string.HomeSettings_placeholder),
            description = stringResource(R.string.HomeSettings_placeholder_description),
            value = state.showPlaceholder,
            onValueChange = {
                onAction(HomeSettingsScreenAction.SetShowSearchBarPlaceholder(it))
            }
        )

        SliderSetting(
            title = stringResource(R.string.HomeSettings_search_bar_radius),
            description = stringResource(R.string.HomeSettings_search_bar_radius_description),
            min = 0f,
            max = 50f,
            steps = 50,
            value = state.searchBarRadius,
            enabled = state.showSearchBar,
            onValueChange = {
                onAction(HomeSettingsScreenAction.SetSearchBarRadius(it))
            },
            onValueChangeFinished = {
                onAction(HomeSettingsScreenAction.SaveSearchBarRadius(it))
            }
        )

        HomeSettingsDialog(onAction = { onAction(it) }, state = state)

    }
}