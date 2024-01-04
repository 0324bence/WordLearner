package hu.delibence.wordlearner.ui.routes

import android.app.Application
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.delibence.wordlearner.R
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.onTouchHeld
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GroupList(navController: NavController) {
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    var selectionMode by remember { mutableStateOf(false) }

    var groupList = remember { mutableStateListOf<Boolean>(false) }
    val groups = learnerViewModel.groups.collectAsState(initial = listOf())

    val words = learnerViewModel.getWordsInGroup(0).collectAsState(initial = listOf())

    if ((groups.value.size + 1) != groupList.size) {
        if (groupList.size > 1) {
            groupList.removeRange(1, groupList.size-1)
            groupList.add(false)
        }
        groups.value.forEach { _ ->
            groupList.add(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.wordlist_groups))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2f.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                actions = {
                    if (selectionMode) {
                        IconButton(onClick = {
                            if (false in groupList) {
                                groupList.replaceAll { true }
                            } else {
                                groupList.replaceAll { false }
                                selectionMode = false
                            }
                        }) {
                            Icon(imageVector = Icons.Outlined.SelectAll, contentDescription = "Select all")
                        }
                        IconButton(onClick = {
                            /*TODO selected*/
                        }) {
                            Icon(imageVector = Icons.Outlined.Bookmarks, contentDescription = "Use selected")
                        }
                        IconButton(
                            onClick = {
                                /*TODO selected*/
                            },
                            enabled = !groupList[0] && (groupList.count { it } == 1)
                        ) {
                            Icon(imageVector = Icons.Outlined.EditNote, contentDescription = "Select all")
                        }
                        IconButton(
                            onClick = {
                                groupList.slice(1..<groupList.size).forEachIndexed { i, v ->
                                    if (v) {
                                        learnerViewModel.deleteGroup(groups.value[i].id)
                                    }
                                }
                                selectionMode = false
                            },
                            enabled = !groupList[0]
                        ) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete selected")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!selectionMode) {
                FloatingActionButton(onClick = {
                    navController.navigate("creategroup")
                }) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
                }
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    ListItem(
                        headlineContent = { Text(text = stringResource(id = R.string.all)) },
                        supportingContent = { Text(text = stringResource(id = R.string.groups_words, words.value.size)) },
                        trailingContent = {
                            if (selectionMode) {
                                Checkbox(
                                    checked = groupList[0],
                                    onCheckedChange = { state ->
                                        groupList[0] = state
                                        if (!groupList.contains(true)) selectionMode = false
                                    }
                                )
                            } else {
                                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = "Open")
                            }
                        },
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                if (selectionMode) {
                                    groupList[0] = !groupList[0]
                                } else {
                                    navController.navigate("words/0")
                                }
                            },
                            onLongClick = {
                                selectionMode = true
                                groupList[0] = true
                            }
                        )
                    )
                    HorizontalDivider()
                }
                itemsIndexed(groups.value) { index, group ->
                    ListItem(
                        headlineContent = { Text(text = group.name) },
                        supportingContent = { Text(text = stringResource(id = R.string.groups_words, group.words)) },
                        trailingContent = {
                            if (selectionMode) {
                                Checkbox(
                                    checked = groupList[index+1],
                                    onCheckedChange = { state ->
                                        groupList[index+1] = state
                                        if (!groupList.contains(true)) selectionMode = false
                                    }
                                )
                            } else {
                                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = "Open")
                            }
                        },
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                if (selectionMode) {
                                    groupList[index+1] = !groupList[index+1]
                                } else {
                                    navController.navigate("words/${group.id}")
                                }
                            },
                            onLongClick = {
                                selectionMode = true
                                groupList[index+1] = true
                            }
                        )
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}