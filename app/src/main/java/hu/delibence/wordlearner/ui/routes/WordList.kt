package hu.delibence.wordlearner.ui.routes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.onTouchHeld
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordList(navController: NavController, groupId: Int?) {
    if (groupId != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.nav_error), style = MaterialTheme.typography.headlineLarge)
        }
    }
    var selectionMode by remember { mutableStateOf(false) }
    var wordList = remember { mutableStateListOf(false, false, false, false, false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Group $groupId")
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
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(wordList) { index, checked ->

                    ListItem(
                        headlineContent = { Text(text = "Lang1 $index") },
                        supportingContent = { Text(text = "Lang2") },
                        trailingContent = {
                            if (selectionMode) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { state ->
                                        wordList[index] = state
                                        if (!wordList.contains(true)) selectionMode = false
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .clickable {
//                            navController.navigate("words")
                            }
                            .onTouchHeld(500.milliseconds) { dur ->
                                if (dur > 1.seconds && dur < 2.seconds) {
                                    Log.d("Wordlist", "Held down")
                                    selectionMode = true
                                    wordList[index] = true
                                }
                            }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}