package hu.delibence.wordlearner.data.repos

import hu.delibence.wordlearner.data.daos.WordDao
import hu.delibence.wordlearner.data.daos.extendedWord
import hu.delibence.wordlearner.data.daos.partialWord
import hu.delibence.wordlearner.data.daos.wordCount
import hu.delibence.wordlearner.data.entities.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {
    suspend fun CreateWord(word: Word) = wordDao.Create(word)

    suspend fun DeleteWord(wordId: Int) = wordDao.Delete(partialWord(wordId))

    suspend fun DeleteAllWordsInGroup(groupId: Int) = wordDao.deleteAllInGroup(groupId)

    fun GetAllWords(): Flow<List<extendedWord>> = wordDao.getAll()

    fun GetAllInGroup(groupId: Int): Flow<List<extendedWord>> = wordDao.getAllInGroup(groupId)

    fun GetByPriority() = wordDao.getByPriority()

    fun GetAllByPriority() = wordDao.getAllByPriority()

    fun GetWordCount(): Flow<wordCount> = wordDao.getWordCount()

    suspend fun RemoveFromPlay(wordId: Int) = wordDao.removeFromPlay(wordId)

    suspend fun RestoreAllToPlay() = wordDao.restoreAllToPlay()

    fun UpdatePriority(wordId: Int, amount: Int) = wordDao.updatePriority(wordId, amount)
}