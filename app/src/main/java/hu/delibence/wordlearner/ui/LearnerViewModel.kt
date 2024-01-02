package hu.delibence.wordlearner.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import hu.delibence.wordlearner.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Route(
    val label: Int,
    val icon: ImageVector
)

class LearnerViewModel : ViewModel() {
//    private val _uiState = MutableStateFlow(LearnerUiState())
//    val uiState: StateFlow<LearnerUiState> = _uiState.asStateFlow()

    val routes = mapOf<String, Route>(
        "learn" to Route(R.string.nav_learn, Icons.Outlined.Language),
        "wordlist" to Route(R.string.nav_wordlist, Icons.Outlined.Folder),
        "importexport" to Route(R.string.nav_importexport, Icons.Outlined.SyncAlt)
    )
    val baseRoute = "learn"

    init {
//        _uiState.value = LearnerUiState()
    }

//    fun ChangePath(newPath: String) {
//        _uiState.value = LearnerUiState(currentPath = newPath)
//    }
}