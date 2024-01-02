package hu.delibence.wordlearner.ui.routes

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroup(navController: NavController) {
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    var groupName by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.create_group))
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
                    value = groupName,
                    onValueChange = {groupName = it},
                    label = { Text(text = stringResource(id = R.string.group_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        learnerViewModel.createGroup(Group(name = groupName))
                        navController.popBackStack()
                    }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    }
}