package hu.delibence.wordlearner.ui.routes

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowLeft
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import hu.delibence.wordlearner.onTouchHeld
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

data class WordItem(
    var checked: Boolean,
    val word: Word
)

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WordList(navController: NavController, groupId: Int?) {
    if (groupId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.nav_error), style = MaterialTheme.typography.headlineLarge)
        }
        return
    }
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    var selectionMode by remember { mutableStateOf(false) }
    var wordList = remember { mutableStateListOf<Boolean>() }
    //val wordList = mutableStateListOf<Boolean>()

    val words by learnerViewModel.getWordsInGroup(groupId).collectAsState(initial = listOf())
    if (words.size != wordList.size) {
        if (wordList.size > 0) wordList.removeRange(0, wordList.size-1)
        words.forEach { _ ->
            wordList.add(false)
        }
    }

    val groups by if (groupId == 0) {
        mutableStateOf(listOf(Group(0, stringResource(id = R.string.all))))
    } else {
        learnerViewModel.getOneGroup(groupId).collectAsState(initial = listOf(Group(0, stringResource(id = R.string.loading))))
    }

    val group = groups.first()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = group.name)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2f.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (selectionMode) {
                        IconButton(onClick = {
                            if (false in wordList) {
                                wordList.replaceAll { true }
                            } else {
                                wordList.replaceAll { false }
                                selectionMode = false
                            }
                        }) {
                            Icon(imageVector = Icons.Outlined.SelectAll, contentDescription = "Select all")
                        }
                        IconButton(onClick = {
                            wordList.forEachIndexed { i, v ->
                                if (v) {
                                    learnerViewModel.deleteWord(words[i].id)
                                }
                            }
                            selectionMode = false
                        }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete selected")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!selectionMode && groupId != 0) {
                FloatingActionButton(onClick = { navController.navigate("createword/$groupId") }) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(words) { index, wordItem ->

                    ListItem(
                        headlineContent = { Text(text = wordItem.word1) },
                        supportingContent = { Text(text = wordItem.word2) },
                        trailingContent = {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (selectionMode) {
                                    Checkbox(
                                        checked = wordList[index],
                                        onCheckedChange = { state ->
                                            wordList[index] = state
                                            if (!wordList.contains(true)) selectionMode = false
                                        }
                                    )
                                } else {
                                    Text(text = "[${wordItem.priority}]")
                                    if (groupId == 0) {
                                        Text(text = wordItem.groupname)
                                    }
                                }

                            }
                        },
                        modifier = Modifier
//                            .clickable {
////                            navController.navigate("words")
//                                if (selectionMode) {
//                                    wordList[index] = !wordList[index]
//                                    if (!wordList.any { item -> item }) selectionMode = false
//                                } else {
//                                    Log.d("Wordlist", "clicked")
//                                }
//                            }
//                            .onTouchHeld(500.milliseconds) { dur ->
//                                if (dur > 1.seconds && dur < 2.seconds) {
////                                    Log.d("Wordlist", "Held down")
//                                    selectionMode = true
//                                    wordList[index] = true
//                                }
//                            }
                            .combinedClickable(
                                onClick = {
                                    if (selectionMode) {
                                        wordList[index] = !wordList[index]
                                        if (!wordList.any { item -> item }) selectionMode = false
                                    } else {
                                        Log.d("Wordlist", "clicked")
                                    }
                                },
                                onLongClick = {
                                    selectionMode = true
                                    wordList[index] = true
                                }
                            )
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}