package com.example.randomfactsapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.randomfactsapp.ui.DailyFactPage
import com.example.randomfactsapp.ui.MainViewModel
import com.example.randomfactsapp.ui.SavedFactsPage
import com.example.randomfactsapp.ui.TopBar
import com.example.randomfactsapp.ui.theme.RandomFactsAppTheme
import com.example.randomfactsapp.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentTheme = isSystemInDarkTheme()
            var isDark by remember {
                mutableStateOf(currentTheme)
            }
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window
                    window.statusBarColor =
                        if (isDark) Color.Black.toArgb() else Color.Gray.toArgb()
                }
            }
            RandomFactsAppTheme(isDark) {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Navigation(viewModel = viewModel) { isDark = !isDark }
                }
            }
        }
    }
}

@Composable
fun Navigation(
    viewModel: MainViewModel,
    toggleTheme: () -> Unit
) {

    val navController = rememberNavController()
    val items = listOf(Screen.DailyFact, Screen.SavedFacts)

    Scaffold(
        topBar = {
            TopBar(
                onToggle = {
                    toggleTheme()
                })
        },
        bottomBar = {
            BottomNavigation() {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, null) },
                        label = { Text(text = screen.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.DailyFact.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.DailyFact.route) {
                DailyFactPage(viewModel = viewModel)
            }
            composable(Screen.SavedFacts.route) {
                SavedFactsPage(viewModel = viewModel)
            }
        }
    }
}
