package com.contactrapide.app.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contactrapide.app.feature.about.AboutScreen
import com.contactrapide.app.feature.contact.ContactScreen
import com.contactrapide.app.feature.home.HomeScreen
import com.contactrapide.app.feature.map.MapScreen
import com.contactrapide.app.feature.onboarding.OnboardingScreen
import com.contactrapide.app.feature.services.ServicesScreen
import com.contactrapide.app.feature.splash.SplashScreen

private const val ANIM = 300

@Composable
fun CrNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(ANIM)
            ) + fadeIn(animationSpec = tween(ANIM))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 4 },
                animationSpec = tween(ANIM)
            ) + fadeOut(animationSpec = tween(ANIM))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 4 },
                animationSpec = tween(ANIM)
            ) + fadeIn(animationSpec = tween(ANIM))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(ANIM)
            ) + fadeOut(animationSpec = tween(ANIM))
        }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
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
