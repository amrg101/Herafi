package com.amrg.herafi.ui.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("/login_screen")
    object RegisterScreen : Screen("/register_screen")
    object HomeScreen : Screen("/home_screen")
    object SettingsScreen : Screen("/settings_screen")
    object LanguageScreen : Screen("/language_screen")
    object ProfileScreen : Screen("/profile_screen")
    object EditProfileScreen : Screen("/edit_profile_screen")
    object EditNameScreen : Screen("/edit_name_screen")
    object EditPasswordScreen : Screen("/edit_password_screen")

}