package hu.delibence.wordlearner.ui.routes

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ImportExport(navController: NavController) {
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2f.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(imageVector = Icons.Outlined.Settings, contentDescription = "")
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.nav_importexport))
                }
            )
        }
    ) {pad ->
        val pagerState = rememberPagerState(initialPage = learnerViewModel.importExportUiState.tabState) { learnerViewModel.importExportUiState.titles.size }

        LaunchedEffect(learnerViewModel.importExportUiState.tabState) {
            pagerState.animateScrollToPage(learnerViewModel.importExportUiState.tabState)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                learnerViewModel.importExportUiState.tabState = pagerState.currentPage
            }
        }

        Column(modifier = Modifier.padding(pad)) {
            PrimaryTabRow(
                selectedTabIndex = learnerViewModel.importExportUiState.tabState,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                learnerViewModel.importExportUiState.titles.forEachIndexed { index, title ->
                    Tab(
                        selected = learnerViewModel.importExportUiState.tabState == index,
                        onClick = { learnerViewModel.importExportUiState.tabState = index },
                        text = { Text(text = stringResource(title), maxLines = 2, overflow = TextOverflow.Ellipsis) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (page == 0) {
                        item {
                            ListItem(
                                headlineContent = { Text(text = "Import from file") },
                                supportingContent = { Text(text = ".csv")},
                                trailingContent = {
                                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
                                },
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("importcsv")
                                    }
                            )
                            HorizontalDivider()
                        }
                        item {
                            ListItem(
                                headlineContent = { Text(text = "Import from text") },
                                trailingContent = {
                                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
                                }
                            )
                            HorizontalDivider()
                        }
                    } else {
                        item {
                            ListItem(
                                headlineContent = { Text(text = "Export to text file") },
                                supportingContent = { Text(text = ".csv")},
                                trailingContent = {
                                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
                                },
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("exportcsv")
                                    }
                            )
                            HorizontalDivider()
                        }
                        item {
                            ListItem(
                                headlineContent = { Text(text = "Export to PDF file") },
                                supportingContent = { Text(text = ".pdf")},
                                trailingContent = {
                                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
                                }
                            )
                            HorizontalDivider()
                        }
                        item {
                            ListItem(
                                headlineContent = { Text(text = "Share export") },
                                trailingContent = {
                                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null)
                                },
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate("exportshare")
                                    }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}