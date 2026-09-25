package com.contactrapide.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contactrapide.app.core.preferences.UserPreferences
import com.contactrapide.app.feature.about.AboutScreen
import com.contactrapide.app.feature.contact.ContactScreen
import com.contactrapide.app.feature.home.HomeScreen
import com.contactrapide.app.feature.map.MapScreen
import com.contactrapide.app.feature.onboarding.OnboardingScreen
import com.contactrapide.app.feature.services.ServicesScreen
import com.contactrapide.app.feature.splash.SplashScreen

@Composable
fun CrNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    // Vérifier si onboarding déjà fait
                    kotlinx.coroutines.MainScope().let { _ ->
                        // On utilise une approche simple : splash → onboarding si 1re fois
                        // (pour éviter le suspend ici, on va direct vers onboarding
                        // puis onboarding redirige si déjà fait)
                        navController.navigate(Routes.ONBOARDING) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onCall = { },
                onWhatsApp = { },
                onLocation = { navController.navigate(Routes.MAP) },
                onServices = { navController.navigate(Routes.SERVICES) },
                onAbout = { navController.navigate(Routes.ABOUT) },
                onContact = { navController.navigate(Routes.CONTACT) }
            )
        }
        composable(Routes.SERVICES) { ServicesScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.MAP) { MapScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.CONTACT) { ContactScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.ABOUT) { AboutScreen(onBack = { navController.popBackStack() }) }
    }
}
