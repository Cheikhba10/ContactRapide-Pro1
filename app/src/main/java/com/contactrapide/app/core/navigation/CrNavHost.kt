package com.contactrapide.app.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.contactrapide.app.feature.about.AboutScreen
import com.contactrapide.app.feature.contact.ContactScreen
import com.contactrapide.app.feature.home.HomeScreen
import com.contactrapide.app.feature.map.MapScreen
import com.contactrapide.app.feature.onboarding.OnboardingScreen
import com.contactrapide.app.feature.provider.ProviderFormScreen
import com.contactrapide.app.feature.request.RequestFormScreen
import com.contactrapide.app.feature.services.ServiceDetailScreen
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
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(ANIM)) +
                fadeIn(animationSpec = tween(ANIM))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it / 4 }, animationSpec = tween(ANIM)) +
                fadeOut(animationSpec = tween(ANIM))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 4 }, animationSpec = tween(ANIM)) +
                fadeIn(animationSpec = tween(ANIM))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(ANIM)) +
                fadeOut(animationSpec = tween(ANIM))
        }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            })
        }
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onFinished = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Routes.HOME) {
            HomeScreen(
                onCall = { },
                onWhatsApp = { },
                onLocation = { navController.navigate(Routes.MAP) },
                onServices = { navController.navigate(Routes.SERVICES) },
                onAbout = { navController.navigate(Routes.ABOUT) },
                onContact = { navController.navigate(Routes.CONTACT) },
                onCategoryClick = { index ->
                    navController.navigate("${Routes.SERVICE_DETAIL}/$index")
                }
            )
        }
        composable(Routes.SERVICES) {
            ServicesScreen(
                onBack = { navController.popBackStack() },
                onServiceClick = { index ->
                    navController.navigate("${Routes.SERVICE_DETAIL}/$index")
                }
            )
        }
        composable(
            route = "${Routes.SERVICE_DETAIL}/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            ServiceDetailScreen(
                serviceIndex = index,
                onBack = { navController.popBackStack() },
                onRequest = {
                    navController.navigate("${Routes.REQUEST_FORM}/$index")
                }
            )
        }
        composable(
            route = "${Routes.REQUEST_FORM}/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: 0
            RequestFormScreen(
                serviceIndex = index,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.MAP) { MapScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.CONTACT) { ContactScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = { navController.popBackStack() },
                onBecomeProvider = { navController.navigate(Routes.PROVIDER_FORM) }
            )
        }
        composable(Routes.PROVIDER_FORM) {
            ProviderFormScreen(onBack = { navController.popBackStack() })
        }
    }
}
