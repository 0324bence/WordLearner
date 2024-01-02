package hu.delibence.wordlearner.ui.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.delibence.wordlearner.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWord(navController: NavController) {
    var lang1 by remember { mutableStateOf("") }
    var lang2 by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.add_word))
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
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lang2,
                    onValueChange = {lang2 = it},
                    label = { Text(text = stringResource(id = R.string.lang2)) },
                    modifier = Modifier.fillMaxWidth()
                )
                /*Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = { *//* Handle edit! *//* },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { *//* Handle settings! *//* },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            })
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Send Feedback") },
                            onClick = { *//* Handle send feedback! *//* },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Email,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
                    }
                }*/
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    }
}