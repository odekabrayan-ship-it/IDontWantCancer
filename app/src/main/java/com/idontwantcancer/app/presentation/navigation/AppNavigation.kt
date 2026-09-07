package com.idontwantcancer.app.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.idontwantcancer.app.presentation.alerts.AlertsScreen
import com.idontwantcancer.app.presentation.alerts.AlertsViewModel
import com.idontwantcancer.app.presentation.home.HomeScreen
import com.idontwantcancer.app.presentation.home.HomeViewModel
import com.idontwantcancer.app.presentation.prevention.PreventionScreen
import com.idontwantcancer.app.presentation.prevention.PreventionViewModel
import com.idontwantcancer.app.presentation.boundary.DefaultIntelligenceCommandDispatcher
import com.idontwantcancer.app.presentation.search.SearchScreen
import com.idontwantcancer.app.presentation.search.SearchViewModel
import com.idontwantcancer.app.presentation.settings.SettingsScreen
import com.idontwantcancer.app.presentation.signal.SignalDetailScreen
import com.idontwantcancer.app.presentation.signal.SignalDetailViewModel
import com.idontwantcancer.app.presentation.healing.HealingSanctuaryScreen
import com.idontwantcancer.app.presentation.healing.HealingViewModel
import com.idontwantcancer.app.presentation.healing.MyJourneyScreen
import com.idontwantcancer.app.presentation.onboarding.MissionSelectionScreen
import com.idontwantcancer.app.presentation.model.IntelligenceUiInteraction
import com.idontwantcancer.app.domain.model.UserMission
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayout
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType

@Composable
fun AppNavigation(
    initialSignalId: String? = null,
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val userMission by viewModel.userMission.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    
    if (userMission == UserMission.UNDEFINED) {
        MissionSelectionScreen()
        return
    }

    // NEW: Sovereign Mission Switcher
    // Automatically re-routes the user when they change missions in Settings
    LaunchedEffect(userMission) {
        val destination = if (userMission == UserMission.HEALING) Screen.HealingSanctuary else Screen.Home
        navController.navigate(destination) {
            popUpTo(0) { inclusive = true } // Clear entire backstack for clean state
            launchSingleTop = true
        }
    }

    // Stage 5 Overhaul: Handle initial deep link from notification
    LaunchedEffect(initialSignalId) {
        if (initialSignalId != null) {
            navController.navigate(Screen.SignalDetail(initialSignalId))
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = remember(userMission) {
        if (userMission == UserMission.HEALING) {
            listOf(
                NavigationItem.HealingSanctuary,
                NavigationItem.MyJourney,
                NavigationItem.Verify
            )
        } else {
            listOf(
                NavigationItem.Home,
                NavigationItem.Verify,
                NavigationItem.Settings
            )
        }
    }
    
    val layout = AdaptiveLayout.current

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            items.forEach { item ->
                item(
                    icon = { Icon(item.icon, contentDescription = stringResource(item.labelRes)) },
                    label = { Text(stringResource(item.labelRes)) },
                    selected = currentDestination?.hierarchy?.any { it.hasRoute(item.screen::class) } == true,
                    onClick = {
                        navController.navigate(item.screen) {
                            // popUpTo start destination for clean navigation within current mission
                            val startDest = if (userMission == UserMission.HEALING) Screen.HealingSanctuary else Screen.Home
                            popUpTo(startDest) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        val startDestination = remember(userMission) {
            if (userMission == UserMission.HEALING) Screen.HealingSanctuary else Screen.Home
        }
        
        // Step 204: Use NavHost transitions for top-level screen changes to maintain visual continuity.
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) },
            popEnterTransition = { fadeIn(animationSpec = tween(400)) },
            popExitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            composable<Screen.Home> {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val onHomeInteraction: (IntelligenceUiInteraction) -> Unit = remember(homeViewModel) {
                    { interaction ->
                        viewModel.dispatch(interaction, homeViewModel) { action ->
                            when (action) {
                                is DefaultIntelligenceCommandDispatcher.NavigateBackAction -> navController.popBackStack()
                                is IntelligenceUiInteraction.EnterHealingSanctuary -> {
                                    navController.navigate(Screen.HealingSanctuary)
                                }
                                is Screen -> navController.navigate(action)
                            }
                        }
                    }
                }
                HomeScreen(
                    viewModel = homeViewModel,
                    onInteraction = onHomeInteraction
                )
            }
            composable<Screen.Alerts> {
                val alertsViewModel: AlertsViewModel = hiltViewModel()
                val onAlertsInteraction: (IntelligenceUiInteraction) -> Unit = remember(alertsViewModel) {
                    { interaction ->
                        viewModel.dispatch(interaction, alertsViewModel) { action ->
                            when (action) {
                                is DefaultIntelligenceCommandDispatcher.NavigateBackAction -> navController.popBackStack()
                                is Screen.SignalDetail -> {
                                    // Suppressed: AlertsScreen handles its own detail pane adaptively
                                }
                                is Screen -> navController.navigate(action)
                            }
                        }
                    }
                }
                AlertsScreen(
                    viewModel = alertsViewModel,
                    onInteraction = onAlertsInteraction
                )
            }
            composable<Screen.Search> { backStackEntry ->
                val search: Screen.Search = backStackEntry.toRoute()
                val searchViewModel: SearchViewModel = hiltViewModel()
                val onSearchInteraction: (IntelligenceUiInteraction) -> Unit = remember(searchViewModel) {
                    { interaction ->
                        viewModel.dispatch(interaction, searchViewModel) { action ->
                            when (action) {
                                is DefaultIntelligenceCommandDispatcher.NavigateBackAction -> navController.popBackStack()
                                is Screen -> navController.navigate(action)
                            }
                        }
                    }
                }
                SearchScreen(
                    initialStoreMode = search.storeMode,
                    onInteraction = onSearchInteraction,
                    viewModel = searchViewModel
                )
            }
            composable<Screen.Prevention> {
                PreventionScreen()
            }
            composable<Screen.Settings> {
                SettingsScreen()
            }
            composable<Screen.HealingSanctuary> {
                val healingViewModel: HealingViewModel = hiltViewModel()
                val onHealingInteraction: (IntelligenceUiInteraction) -> Unit = remember(healingViewModel) {
                    { interaction ->
                        viewModel.dispatch(interaction, healingViewModel) { action ->
                            when (action) {
                                is DefaultIntelligenceCommandDispatcher.NavigateBackAction -> navController.popBackStack()
                                is Screen -> navController.navigate(action)
                            }
                        }
                    }
                }
                HealingSanctuaryScreen(
                    onSettings = { onHealingInteraction(IntelligenceUiInteraction.EnterSettings) },
                    viewModel = healingViewModel
                )
            }
            composable<Screen.MyJourney> {
                val healingViewModel: HealingViewModel = hiltViewModel()
                MyJourneyScreen(viewModel = healingViewModel)
            }
            composable<Screen.SignalDetail> {
                val detailViewModel: SignalDetailViewModel = hiltViewModel()
                val onDetailInteraction: (IntelligenceUiInteraction) -> Unit = remember(detailViewModel) {
                    { interaction ->
                        viewModel.dispatch(interaction, detailViewModel) { action ->
                            when (action) {
                                is DefaultIntelligenceCommandDispatcher.NavigateBackAction -> navController.popBackStack()
                                is Screen -> navController.navigate(action)
                            }
                        }
                    }
                }
                SignalDetailScreen(
                    viewModel = detailViewModel,
                    onInteraction = onDetailInteraction
                )
            }
        }
    }
}
