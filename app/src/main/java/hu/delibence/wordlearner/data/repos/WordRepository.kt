package hu.delibence.wordlearner.data.repos

import hu.delibence.wordlearner.data.daos.WordDao
import hu.delibence.wordlearner.data.daos.partialWord
import hu.delibence.wordlearner.data.entities.Word

class WordRepository(private val wordDao: WordDao) {
    suspend fun CreateWord(word: Word) = wordDao.Create(word)

    suspend fun DeleteWord(partialWord: partialWord) = wordDao.Delete(partialWord)

    suspend fun DeleteAllWordsInGroup(groupId: Int) = wordDao.deleteAllInGroup(groupId)

    fun GetAllWords() = wordDao.getAll()

    fun GetAllInGroup(groupId: Int) = wordDao.getAllInGroup(groupId)

    fun GetByPriority() = wordDao.getByPriority()

    fun GetAllByPriority() = wordDao.getAllByPriority()

    fun UpdatePriority(wordId: Int, amount: Int) = wordDao.updatePriority(wordId, amount)
}