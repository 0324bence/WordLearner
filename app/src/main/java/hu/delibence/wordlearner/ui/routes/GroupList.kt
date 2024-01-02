package hu.delibence.wordlearner.ui.routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowRight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import hu.delibence.wordlearner.onTouchHeld
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupList(navController: NavController) {
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("creategroup")
            }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
            }
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(5) { index ->
                    ListItem(
                        headlineContent = { Text(text = "Group name $index") },
                        supportingContent = { Text(text = "10 words") },
                        trailingContent = { Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = "Open")},
                        modifier = Modifier.clickable {
                            navController.navigate("words/$index")
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun Groups(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(15) { index ->
            ListItem(
                headlineContent = { Text(text = "Group name $index") },
                supportingContent = { Text(text = "10 words") },
                trailingContent = { Icon(imageVector = Icons.Outlined.ArrowRight, contentDescription = "Open")},
                modifier = Modifier.clickable {
                    navController.navigate("items/$index")
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
@Preview
fun Words(groupId: Int? = 0) {
    if (groupId == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(id = R.string.nav_error), style = MaterialTheme.typography.headlineLarge)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(5) { index ->
            var held by remember { mutableStateOf(false) }
            ListItem(
                modifier = Modifier.clickable {
                    /*TODO*/
                }.onTouchHeld(500.milliseconds) {
                    if (it > 1.seconds) held = true
                },
                headlineContent = { Text(text = "Lang1 $index $held") },
                supportingContent = { Text(text = "Lang2") },
                trailingContent = { Icon(imageVector = Icons.Outlined.ArrowRight, contentDescription = "Open")}
            )
            HorizontalDivider()
        }
    }
}