package com.amrg.herafi

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.amrg.herafi.ui.navigation.NavGraph
import com.amrg.herafi.ui.navigation.Screen
import com.amrg.herafi.ui.theme.HerafiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HerafiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        startDestination = Screen.LoginScreen.route,
                        navController = navController
                    )
                    LaunchedEffect(key1 = Unit) {
                        mainViewModel.loggedIn.collect { loggedIn ->
                            if (loggedIn) {
                                /* navController.navigate(Screen.HomeScreen.route) {
                                     popUpTo(Screen.LoginScreen.route) {
                                         inclusive = true
                                     }*/
                                Toast.makeText(
                                    HerafiApplication.applicationContext,
                                    "Login Success",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        // Preventing the System settings to change the font size.
        val configuration = resources.configuration
        configuration.fontScale = 1.0.toFloat()
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }
}

