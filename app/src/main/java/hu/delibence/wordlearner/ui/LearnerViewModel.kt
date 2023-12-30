package hu.delibence.wordlearner.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Route(
    val label: String,
    val icon: ImageVector
)

class LearnerViewModel : ViewModel() {
//    private val _uiState = MutableStateFlow(LearnerUiState())
//    val uiState: StateFlow<LearnerUiState> = _uiState.asStateFlow()

    val routes = mapOf<String, Route>(
        "learn" to Route("Learn", Icons.Outlined.Language),
        "wordlist" to Route("Word list", Icons.Outlined.Folder),
        "importexport" to Route("Import/Export", Icons.Outlined.SyncAlt)
    )
    val baseRoute = "learn"

    init {
//        _uiState.value = LearnerUiState()
    }

//    fun ChangePath(newPath: String) {
//        _uiState.value = LearnerUiState(currentPath = newPath)
//    }
}