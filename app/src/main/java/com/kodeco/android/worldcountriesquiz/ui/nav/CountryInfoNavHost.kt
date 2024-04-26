package com.kodeco.android.worldcountriesquiz.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kodeco.android.worldcountriesquiz.database.SettingsPrefs
import com.kodeco.android.worldcountriesquiz.repositories.CountryRepository
import com.kodeco.android.worldcountriesquiz.ui.components.SplashScreen
import com.kodeco.android.worldcountriesquiz.ui.screens.Screen
import com.kodeco.android.worldcountriesquiz.ui.screens.about.AboutScreen
import com.kodeco.android.worldcountriesquiz.ui.screens.countrydetails.CountryDetailsScreen
import com.kodeco.android.worldcountriesquiz.ui.screens.countrydetails.CountryDetailsViewModel
import com.kodeco.android.worldcountriesquiz.ui.screens.countrylist.CountryListScreen
import com.kodeco.android.worldcountriesquiz.ui.screens.countrylist.CountryListViewModel
import com.kodeco.android.worldcountriesquiz.ui.screens.quiz.QuizScreen
import com.kodeco.android.worldcountriesquiz.ui.screens.quiz.QuizViewModel
import com.kodeco.android.worldcountriesquiz.ui.screens.settings.SettingsScreen
import com.kodeco.android.worldcountriesquiz.ui.screens.settings.SettingsViewModel
import com.kodeco.android.worldcountriesquiz.ui.screens.start.StartScreen

@Composable
fun CountryInfoNavHost(
    repository: CountryRepository,
    worldCountriesPrefs: SettingsPrefs,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.path) {
        composable(Screen.Splash.path) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Start.path) {
            StartScreen(
                onQuizClick = { navController.navigate(Screen.Quiz.path) },
                onLearnClick = { navController.navigate(Screen.List.path) },
                onSettingsTap = { navController.navigate(Screen.Settings.path) },
                onAboutTap = { navController.navigate(Screen.About.path) },
                )
        }

        composable(Screen.Quiz.path) {
            QuizScreen(navController = navController,
                quizViewModel = viewModel(
                    factory = QuizViewModel.QuizViewModelFactory(
                        repository = repository, 10
                    ),
                ),
                navigateToStartScreen = { navController.navigateUp() },
                )
        }

        composable(Screen.List.path) {
            CountryListScreen(
                viewModel = viewModel(
                    factory = CountryListViewModel.CountryInfoViewModelFactory(
                        repository = repository,
                    ),
                ),
                onCountryRowTap = { countryIndex ->
                    navController.navigate("${Screen.Details.path}/$countryIndex")
                },
                worldCountriesPrefs = worldCountriesPrefs,
                navigateToStartScreen = { navController.navigateUp() },
                )
        }

        composable(
            route = "${Screen.Details.path}/{countryIndex}",
            arguments = listOf(navArgument("countryIndex") { type = NavType.IntType }),
        ) { backStackEntry ->
            val countryIndex = backStackEntry.arguments!!.getInt("countryIndex")
            CountryDetailsScreen(
                countryIndex = countryIndex,
                viewModel = viewModel(
                    factory = CountryDetailsViewModel.CountryDetailsViewModelFactory(
                        repository = repository,
                    ),
                ),
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(Screen.About.path) {
            AboutScreen(
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(Screen.Settings.path) {
            SettingsScreen(
                viewModel = viewModel(
                    factory = SettingsViewModel.CountryInfoSettingsViewModelFactory(
                        worldCountriesPrefs = worldCountriesPrefs,
                    ),
                ),
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}
