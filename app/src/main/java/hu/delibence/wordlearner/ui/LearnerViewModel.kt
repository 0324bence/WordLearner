package hu.delibence.wordlearner.ui

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.daos.partialWord
import hu.delibence.wordlearner.data.databases.WordLearnerDatabase
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import hu.delibence.wordlearner.data.repos.GroupRepository
import hu.delibence.wordlearner.data.repos.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class Route(
    val label: Int,
    val icon: ImageVector
)

class LearnerViewModel(application: Application) : AndroidViewModel(application) {
//    private val _uiState = MutableStateFlow(LearnerUiState())
//    val uiState: StateFlow<LearnerUiState> = _uiState.asStateFlow()

    val routes = mapOf<String, Route>(
        "learn" to Route(R.string.nav_learn, Icons.Outlined.Language),
        "wordlist" to Route(R.string.nav_wordlist, Icons.Outlined.Folder),
        "importexport" to Route(R.string.nav_importexport, Icons.Outlined.SyncAlt)
    )
    val baseRoute = "learn"

    private val groupRepository: GroupRepository
    private val wordRepository: WordRepository

    val groups: Flow<List<Group>>

    val currentWord: Flow<List<Word>>

    init {
        val database = WordLearnerDatabase.getDatabase(application)
        val groupDao = database.groupDao()
        val wordDao = database.wordDao()
        groupRepository = GroupRepository(groupDao)
        wordRepository = WordRepository(wordDao)
        groups = groupRepository.GetAllGroups()

        currentWord = getRandomWordByPriority(0)
    }

    fun createGroup(group: Group) {
        viewModelScope.launch (Dispatchers.IO) {
            groupRepository.CreateGroup(group)
        }
    }

    fun createWord(word: Word) {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.CreateWord(word)
        }
    }

    fun deleteWord(wordId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.DeleteWord(partialWord(wordId))
        }
    }

    fun getWordsInGroup(groupId: Int): Flow<List<Word>> {
        if (groupId == 0) {
            return wordRepository.GetAllWords()
        }
        return wordRepository.GetAllInGroup(groupId)
    }

    fun getOneGroup(groupId: Int): Flow<List<Group>> {
        return groupRepository.getSpecificGroup(groupId)
    }

    fun getRandomWordByPriority(groupId: Int): Flow<List<Word>> {
        if (groupId == 0) {
            return wordRepository.GetAllByPriority()
        }
        return wordRepository.GetByPriority(groupId)
    }

    fun updateWordPriority(wordId: Int, amount: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.UpdatePriority(wordId, amount)
        }
    }
//    fun ChangePath(newPath: String) {
//        _uiState.value = LearnerUiState(currentPath = newPath)
//    }
}

class LearnerViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(LearnerViewModel::class.java)) {
            return LearnerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}