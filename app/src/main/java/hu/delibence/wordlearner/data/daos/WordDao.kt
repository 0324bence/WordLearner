package hu.delibence.wordlearner.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import kotlinx.coroutines.flow.Flow


@Dao
interface WordDao {
    @Insert
    suspend fun Create(word: Word)

    @Delete(entity = Word::class)
    fun Delete(wordId: partialWord)

    @Query("Select * from words Order By word1 ASC")
    fun getAll(): Flow<List<Word>>

    @Query("Select * from words where `group` = :groupId Order By word1 ASC")
    fun getAllInGroup(groupId: Int): Flow<List<Word>>

    @Query("SELECT * FROM (SELECT * FROM words WHERE `group` = :groupId ORDER BY priority DESC LIMIT 3) ORDER BY RANDOM() LIMIT 1")
    fun getByPriority(groupId: Int): Flow<List<Word>>

    //@Query("SELECT * FROM (SELECT * FROM word ORDER BY priority DESC LIMIT 3) ORDER BY RANDOM() LIMIT 1")
    @Query("SELECT * FROM (SELECT * FROM words ORDER BY priority DESC LIMIT 3) ORDER BY RANDOM() LIMIT 1")
    fun getAllByPriority(): Flow<List<Word>>

    @Query("UPDATE words SET priority = (priority - :amount) WHERE id = :wordId")
    fun updatePriority(wordId: Int, amount: Int)

    @Query("UPDATE words SET flagged = :value WHERE id = :wordId")
    fun flag(wordId: Int, value: Boolean)
}
data class partialWord(val id: Int)
