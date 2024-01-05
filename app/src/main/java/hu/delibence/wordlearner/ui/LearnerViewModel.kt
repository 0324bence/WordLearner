package hu.delibence.wordlearner.ui

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hu.delibence.wordlearner.R
import hu.delibence.wordlearner.data.daos.extendedGroup
import hu.delibence.wordlearner.data.daos.extendedWord
import hu.delibence.wordlearner.data.daos.partialGroup
import hu.delibence.wordlearner.data.daos.partialWord
import hu.delibence.wordlearner.data.daos.wordCount
import hu.delibence.wordlearner.data.databases.WordLearnerDatabase
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.SelectedGroup
import hu.delibence.wordlearner.data.entities.Word
import hu.delibence.wordlearner.data.repos.GroupRepository
import hu.delibence.wordlearner.data.repos.SelectedGroupRepository
import hu.delibence.wordlearner.data.repos.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
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
        "settings" to Route(R.string.settings, Icons.Outlined.Settings),
        "importexport" to Route(R.string.nav_importexport, Icons.Outlined.SyncAlt)
    )
    val baseRoute = "learn"

    var darkmode by mutableStateOf(true)
    var useSystemTheme by mutableStateOf(true)
    var usePlaySet by mutableStateOf(true)
    var positivePriorityMod by mutableStateOf("5")
    var negativePriorityMod by mutableStateOf("1")

    private val groupRepository: GroupRepository
    private val wordRepository: WordRepository
    private val selectedGroupRepository: SelectedGroupRepository

    val groups: Flow<List<extendedGroup>>
    val selectedGroups: Flow<List<SelectedGroup>>
    var currentWord: Flow<List<Word>>
    val allWords: Flow<List<wordCount>>

    init {
        val database = WordLearnerDatabase.getDatabase(application)
        val groupDao = database.groupDao()
        val wordDao = database.wordDao()
        val selectedGroupDao = database.selectedGroupDao()
        groupRepository = GroupRepository(groupDao)
        wordRepository = WordRepository(wordDao)
        selectedGroupRepository = SelectedGroupRepository(selectedGroupDao)
        groups = groupRepository.GetAllGroups()
        selectedGroups = selectedGroupRepository.getGroups()
        allWords = wordRepository.GetWordCount()

        currentWord = getRandomWordByPriority()
    }

    fun changeDarkMode(value: Boolean) {
        darkmode = value
    }

    fun changeUseSystemTheme(value: Boolean) {
        useSystemTheme = value
    }

    fun changeNegativePriorityMod(value: String) {
        negativePriorityMod = value
    }

    fun changePositivePriorityMod(value: String) {
        positivePriorityMod = value
    }

    fun changeUsePlaySet(value: Boolean) {
        usePlaySet = value
    }

    fun removeFromPlay(wordId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.RemoveFromPlay(wordId)
        }
    }

    fun restoreAllToPlay() {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.RestoreAllToPlay()
        }
    }

    fun selectGroup(groupId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            selectedGroupRepository.addGroup(groupId)
        }
    }

    fun deselectAllGroups() {
        viewModelScope.launch (Dispatchers.IO) {
            selectedGroupRepository.removeAll()
        }
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

    fun deleteAllWordsInGroup(groupId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.DeleteAllWordsInGroup(groupId)
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            groupRepository.DeleteGroup(partialGroup(groupId))
            wordRepository.DeleteAllWordsInGroup(groupId)
            selectedGroupRepository.removeGroup(groupId)
        }
    }

    fun getWordsInGroup(groupId: Int): Flow<List<extendedWord>> {
        if (groupId == 0) {
            return wordRepository.GetAllWords()
        }
        return wordRepository.GetAllInGroup(groupId)
    }

    fun getOneGroup(groupId: Int): Flow<List<Group>> {
        return groupRepository.getSpecificGroup(groupId)
    }

    private fun getRandomWordByPriority(): Flow<List<Word>> {
        return wordRepository.GetByPriority()
    }

    fun updateWordPriority(wordId: Int, amount: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            wordRepository.UpdatePriority(wordId, amount)
        }
    }
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