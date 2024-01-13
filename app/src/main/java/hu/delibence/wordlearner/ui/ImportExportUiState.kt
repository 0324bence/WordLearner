package hu.delibence.wordlearner.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import hu.delibence.wordlearner.R
data class ImportExportUiState  (
    val titles: List<Int> = listOf(R.string.import_, R.string.export),
) {
    var tabState by mutableIntStateOf(0)
}
