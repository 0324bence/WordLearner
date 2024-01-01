package hu.delibence.wordlearner.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.ui.routes.ImportExport
import hu.delibence.wordlearner.ui.routes.Learn
import hu.delibence.wordlearner.ui.routes.WordList

@Composable
fun MainScreen(learnerViewModel: LearnerViewModel = viewModel()) {
    val navController = rememberNavController()
//    val learnerUiState = learnerViewModel.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(10f),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                tonalElevation = 2f.dp
            ) {
                learnerViewModel.routes.forEach {
                    NavigationBarItem(
                        icon = { Icon(it.value.icon, contentDescription = it.value.label) },
                        label = { Text(it.value.label) },
                        selected = currentRoute == it.key,
                        onClick = { navController.navigate(it.key) }
                    )
                }
            }
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            NavHost(
                navController,
                startDestination = learnerViewModel.baseRoute,
                enterTransition = {
                    fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(200))
                }
            ) {
                learnerViewModel.routes.forEach { entry ->
                    composable(route = entry.key) {
//                        Text(navController.currentDestination?.route ?: "No route")
                        when (entry.key) {
                            "learn" -> Learn()
                            "wordlist" -> WordList()
                            "importexport" -> ImportExport()
                            else -> {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = stringResource(id = R.string.nav_error), style = MaterialTheme.typography.headlineLarge)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}