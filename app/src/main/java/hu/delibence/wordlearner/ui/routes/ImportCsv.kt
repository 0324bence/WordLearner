package hu.delibence.wordlearner.ui.routes

import android.app.Application
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.net.FileNameMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportCsv(navController: NavController) {
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    var selectedFile by remember { mutableStateOf("") }
    var selectedFileName by remember { mutableStateOf("") }
    var groupName by remember { mutableStateOf("") }
    var fileDelimiter by remember { mutableStateOf(",") }

    val readCsv = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){
        if (it == null) {
            return@rememberLauncherForActivityResult
        }

        selectedFile = it.toString()

        val cursor = context.contentResolver.query(it, null, null, null, null)
        cursor?.moveToFirst()
        selectedFileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: 0) ?: "File opened"
        cursor?.close()

        val fileNameSegments = selectedFileName.removePrefix(".csv").split("_")

        groupName = if (fileNameSegments.size > 1) fileNameSegments.slice(0..fileNameSegments.size-2).joinToString("_") else ""
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
            val defaultColors = TextFieldDefaults.colors()

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.file_format, fileDelimiter), style = MaterialTheme.typography.titleMedium)
            }
            OutlinedTextField(
                value = selectedFileName,
                onValueChange = {},
                label = {Text(stringResource(id = R.string.file_to_load))},
                readOnly = true,
                enabled = false,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = defaultColors.unfocusedIndicatorColor,
                    disabledLabelColor = defaultColors.unfocusedLabelColor,
                    disabledPlaceholderColor = defaultColors.unfocusedPlaceholderColor,
                    disabledTextColor = defaultColors.unfocusedTextColor,
                    disabledSupportingTextColor = defaultColors.unfocusedSupportingTextColor,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Log.d("outputLog", "clicked box")
                        readCsv.launch(
                            arrayOf(
                                "text/comma-separated-values",
                                "text/csv",
                                "text/.csv"
                            )
                        )
                    }
            )
            OutlinedTextField(
                value = fileDelimiter,
                onValueChange = {fileDelimiter = it},
                label = {Text(stringResource(id = R.string.delimiter))},
                modifier = Modifier.fillMaxWidth(),
                isError = fileDelimiter.isEmpty()
            )
            OutlinedTextField(
                value = groupName,
                onValueChange = {groupName = it},
                label = {Text(stringResource(id = R.string.group_name))},
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    if (fileDelimiter.isEmpty()) return@Button
                    learnerViewModel.createGroup(Group(name = groupName))
                    learnerViewModel.viewModelScope.launch (Dispatchers.IO) {
                        val group = learnerViewModel.getOneGroupByName(groupName).first()
                        val inputStream = context.contentResolver.openInputStream(selectedFile.toUri())
                        if (inputStream != null) {
                            val reader = inputStream.reader()

                            val lines = reader.readLines()
                            val words = mutableListOf<Word>()
                            for (line in lines) {
                                val data = line.split(fileDelimiter)
                                if (data.size == 2) {
                                    words.add(
                                        Word(
                                            word1 = data[0],
                                            word2 = data[1],
                                            group = group.id
                                        )
                                    )
                                }
                            }
                            learnerViewModel.createAllWords(words)

                            reader.close()
                            inputStream.close()
                        }
                    }
                    navController.popBackStack()
                }) {
                    Text(text = stringResource(id = R.string.import_))
                }
            }

        }
    }
}