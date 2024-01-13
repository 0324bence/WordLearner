package hu.delibence.wordlearner.ui.routes

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory
import kotlinx.coroutines.flow.map

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportCsv(navController: NavController) {

    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    val viewModelGroups by learnerViewModel.groups.collectAsState(initial = listOf())
    val groups = viewModelGroups

    var expanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableIntStateOf(0) }
    var deleteGroup by remember { mutableStateOf(false) }


    val viewModelOtuputLines by if (groups.isNotEmpty() && groups.size-1 >= selectedGroup) {
        learnerViewModel.getWordsInGroup(groups[selectedGroup].id).map { words ->
        val outputList = mutableListOf<String>()
        words.forEach {word ->
            outputList.add("${word.word1},${word.word2}")
        }
        return@map outputList
    }.collectAsState(initial = null) } else {
        mutableStateOf(null)
    }
    val outputLines = viewModelOtuputLines

    val createCsv = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) {
//        Log.d("outputLog", "Create contract callback")
        if (it == null) {
            return@rememberLauncherForActivityResult
        }
//        Log.d("outputLog", it.toString())
        val fileOutputStream = context.contentResolver.openOutputStream(it) ?: return@rememberLauncherForActivityResult
        //learnerViewModel.viewModelScope.launch {
//            Log.d("outputLog", "launching coroutine")
            val writer = fileOutputStream.writer()

//            Log.d("outputLog", "start appending lines")

            outputLines?.forEach {words ->
//                Log.d("outputLog", "appending line")
                writer.appendLine(words)
            }

            writer.close()
            fileOutputStream.close()

            if (deleteGroup) {
                learnerViewModel.deleteGroup(groups[selectedGroup].id)
            }

            navController.popBackStack()
        //}
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.export_csv))
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
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (groups.isEmpty()) {
                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.no_export),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        // The `menuAnchor` modifier must be passed to the text field for correctness.
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = if (groups.size >= selectedGroup) groups[selectedGroup].name else "",
                        onValueChange = {},
                        label = { Text(stringResource(id = R.string.group_to_export)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        groups.forEachIndexed { index, group ->
                            DropdownMenuItem(
                                text = { Text(group.name) },
                                onClick = {
                                    selectedGroup = index
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = deleteGroup,
                            onValueChange = { deleteGroup = !deleteGroup },
                            role = Role.Checkbox
                        )
                ) {
                    Checkbox(
                        checked = deleteGroup,
                        onCheckedChange = null
                    )
                    Text(text = stringResource(id = R.string.delete_group))
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        createCsv.launch("${groups[selectedGroup].name}_export.csv")
                    }) {
                        Text(text = stringResource(id = R.string.export))
                    }
                }
            }
        }
    }
}