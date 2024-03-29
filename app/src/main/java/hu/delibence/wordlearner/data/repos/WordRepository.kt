package hu.delibence.wordlearner.data.repos

import hu.delibence.wordlearner.data.daos.WordDao
import hu.delibence.wordlearner.data.daos.extendedWord
import hu.delibence.wordlearner.data.daos.partialWord
import hu.delibence.wordlearner.data.daos.wordCount
import hu.delibence.wordlearner.data.entities.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {
    suspend fun CreateWord(word: Word) = wordDao.Create(word)

    suspend fun CreateAllWords(words: List<Word>) = wordDao.CreateAll(words)

    suspend fun DeleteWord(wordId: Int) = wordDao.Delete(partialWord(wordId))

    suspend fun DeleteAllWordsInGroup(groupId: Int) = wordDao.deleteAllInGroup(groupId)

    suspend fun UpdateWord(word: Word) = wordDao.Update(word)

    fun GetAllWords(): Flow<List<extendedWord>> = wordDao.getAll()

    fun GetAllInGroup(groupId: Int): Flow<List<extendedWord>> = wordDao.getAllInGroup(groupId)

    fun GetByPriority() = wordDao.getByPriority()

    fun GetSpecificWord(wordId: Int) = wordDao.getSpecificWord(wordId)

    fun GetAllByPriority() = wordDao.getAllByPriority()

    fun GetWordCount(): Flow<wordCount> = wordDao.getWordCount()

    suspend fun RemoveFromPlay(wordId: Int) = wordDao.removeFromPlay(wordId)

    suspend fun RestoreAllToPlay() = wordDao.restoreAllToPlay()

    suspend fun UpdatePriority(wordId: Int, amount: Int) = wordDao.updatePriority(wordId, amount)

    suspend fun FlagWord(wordId: Int, value: Boolean) = wordDao.flag(wordId, value)
}