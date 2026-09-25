package com.contactrapide.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contactrapide.app.feature.about.AboutScreen
import com.contactrapide.app.feature.contact.ContactScreen
import com.contactrapide.app.feature.home.HomeScreen
import com.contactrapide.app.feature.map.MapScreen
import com.contactrapide.app.feature.services.ServicesScreen
import com.contactrapide.app.feature.splash.SplashScreen

@Composable
fun CrNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onCall = { /* TODO: appel */ },
                onWhatsApp = { /* TODO: whatsapp */ },
                onLocation = { navController.navigate(Routes.MAP) },
                onServices = { navController.navigate(Routes.SERVICES) },
                onAbout = { navController.navigate(Routes.ABOUT) },
                onContact = { navController.navigate(Routes.CONTACT) }
            )
        }
        composable(Routes.SERVICES) {
            ServicesScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MAP) {
            MapScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.CONTACT) {
            ContactScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
