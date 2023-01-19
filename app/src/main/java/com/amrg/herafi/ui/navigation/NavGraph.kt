package com.amrg.herafi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amrg.herafi.ui.screens.edit_name_screen.EditNameScreen
import com.amrg.herafi.ui.screens.edit_password_screen.EditPasswordScreen
import com.amrg.herafi.ui.screens.edit_profile_screen.EditProfileScreen
import com.amrg.herafi.ui.screens.home_screen.HomeScreen
import com.amrg.herafi.ui.screens.login_screen.LoginScreen
import com.amrg.herafi.ui.screens.register_screen.RegisterScreen
import com.amrg.herafi.ui.screens.settings_screen.SettingsScreen
import com.amrg.herafi.ui.screens.language_screen.LanguageScreen
import com.amrg.herafi.ui.screens.profile_screen.ProfileScreen

@Composable
fun NavGraph(startDestination: String, navController: NavHostController) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.LanguageScreen.route) {
            LanguageScreen(navController = navController)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(navController = navController)
        }
        composable(route = Screen.EditNameScreen.route) {
            EditNameScreen(navController = navController)
        }
        composable(route = Screen.EditPasswordScreen.route) {
            EditPasswordScreen(navController = navController)
        }
    }
}