package hu.delibence.wordlearner.ui.routes

import android.app.Application
import android.widget.CheckBox
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.entities.Setting
import hu.delibence.wordlearner.ui.LearnerViewModel
import hu.delibence.wordlearner.ui.LearnerViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings() {
    val context = LocalContext.current
    val learnerViewModel: LearnerViewModel = viewModel(
        factory = LearnerViewModelFactory(context.applicationContext as Application)
    )

    val settings by learnerViewModel.currentSettings.collectAsState(initial = Setting())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2f.dp),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    ) {pad ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(pad)) {
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.darkmode)) },
                    trailingContent = {
                        Switch(
                            checked = if (settings.useSystemTheme) isSystemInDarkTheme() else settings.darkMode,
                            onCheckedChange = {learnerViewModel.changeDarkMode(it)},
                            enabled = !settings.useSystemTheme
                        )
                    }
                )
                HorizontalDivider()
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.system_theme)) },
                    trailingContent = {
                        Switch(
                            checked = settings.useSystemTheme,
                            onCheckedChange = {learnerViewModel.changeUseSystemTheme(it)}
                        )
                    }
                )
                HorizontalDivider()
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.use_deck)) },
                    supportingContent = { Text(text = stringResource(id = R.string.deck_info)) },
                    trailingContent = {
                        Switch(
                            checked = settings.usePlayset,
                            onCheckedChange = {learnerViewModel.changeUsePlaySet(it)}
                        )
                    }
                )
                HorizontalDivider()
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.positive_priority)) },
                    trailingContent = {
                        OutlinedTextField(
                            value = settings.negativePriorityMod,
                            onValueChange = {
                                if (it.length < 3 && it.isDigitsOnly())
                                    learnerViewModel.changeNegativePriorityMod(it)
                            },
                            singleLine = true,
                            modifier = Modifier.width(60.dp),
                            textStyle = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            isError = settings.negativePriorityMod.isEmpty()
                        )
                    }
                )
                HorizontalDivider()
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(id = R.string.negative_priority)) },
                    trailingContent = {
                        OutlinedTextField(
                            value = settings.positivePriorityMod,
                            onValueChange = {
                                if (it.length < 3 && it.isDigitsOnly()) {
                                    learnerViewModel.changePositivePriorityMod(it)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.width(60.dp),
                            textStyle = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            isError = settings.positivePriorityMod.isEmpty()
                        )
                    }
                )
                HorizontalDivider()
            }
        }
    }
}