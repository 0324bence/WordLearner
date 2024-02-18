package hu.delibence.wordlearner.ui.routes

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWord(navController: NavController, groupId: Int?, wordId: Int?) {
    if (groupId == null || wordId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.nav_error), style = MaterialTheme.typography.headlineLarge)
        }
        return
    }
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    val group by if (groupId == 0) {
        mutableStateOf(Group(0, stringResource(id = R.string.all)))
    } else {
        learnerViewModel.getOneGroup(groupId).collectAsState(initial = Group(0, stringResource(id = R.string.loading)))
    }

    val word by learnerViewModel.getSpecificWord(wordId).collectAsState(initial = null)
    val localWord = word


    if (localWord != null) {
        var lang1 by remember { mutableStateOf(localWord.word1) }
        var lang2 by remember { mutableStateOf(localWord.word2) }
        var priority by remember { mutableStateOf(localWord.priority.toString()) }


        val focusManager = LocalFocusManager.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.edit_word, group.name))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2f.dp),
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            contentWindowInsets = WindowInsets(20.dp, 20.dp, 20.dp, 20.dp)
        ) {padding ->
            Surface(modifier = Modifier
                .padding(padding)
                .fillMaxSize()) {
                Column() {
                    OutlinedTextField(
                        value = lang1,
                        onValueChange = {lang1 = it},
                        label = { Text(text = stringResource(id = R.string.lang1)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        singleLine = true,
                        isError = lang1.isEmpty()
                    )
                    OutlinedTextField(
                        value = lang2,
                        onValueChange = {lang2 = it},
                        label = { Text(text = stringResource(id = R.string.lang2)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                val newGroup = if (group.id == 0) {
                                    null
                                } else {
                                    group.id
                                }
                                learnerViewModel.createWord(Word(word1 = lang1, word2 = lang2, group = newGroup))
                                navController.popBackStack()
                            }
                        ),
                        singleLine = true,
                        isError = lang2.isEmpty()
                    )
                    OutlinedTextField(
                        value = priority,
                        onValueChange = {
                            if (it.isEmpty() || it == "-") {
                                priority = it
                                return@OutlinedTextField
                            }
                            val num = it.toIntOrNull()
                            if (num != null) {
                                priority = it
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.word_priority)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (priority.toIntOrNull() == null || lang1.isEmpty() || lang2.isEmpty()) {
                                    return@KeyboardActions
                                }
                                learnerViewModel.updateWord(Word(word1 = lang1, word2 = lang2, group = groupId, priority = priority.toInt(), id = wordId))
                                navController.popBackStack()
                            }
                        ),
                        singleLine = true,
                        isError = priority.toIntOrNull() == null
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            if (priority.toIntOrNull() == null || lang1.isEmpty() || lang2.isEmpty()) {
                                return@Button
                            }
                            learnerViewModel.updateWord(Word(word1 = lang1, word2 = lang2, group = groupId, priority = priority.toInt(), id = wordId))
                            navController.popBackStack()
                        }) {
                            Text(text = stringResource(id = R.string.save))
                        }
                    }
                }
            }
        }
    }
}