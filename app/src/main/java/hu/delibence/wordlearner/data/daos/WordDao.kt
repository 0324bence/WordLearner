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
    suspend fun Delete(wordId: partialWord)

    @Query("Delete from words where `group` = :groupId")
    suspend fun deleteAllInGroup(groupId: Int)

    @Query("Select *, groups.name as groupname from words join groups on words.`group` = groups.id Order By word1 ASC")
    fun getAll(): Flow<List<extendedWord>>

    @Query("Select *, groups.name as groupname from words join groups on words.`group` = groups.id where `group` = :groupId Order By word1 ASC")
    fun getAllInGroup(groupId: Int): Flow<List<extendedWord>>

    @Query("SELECT * FROM (SELECT * FROM words join selectedwords on selectedwords.`group` = words.`group` where inplay = 1 ORDER BY priority DESC LIMIT 3) ORDER BY RANDOM() LIMIT 1")
    fun getByPriority(): Flow<List<Word>>

    //@Query("SELECT * FROM (SELECT * FROM word ORDER BY priority DESC LIMIT 3) ORDER BY RANDOM() LIMIT 1")
    @Query("SELECT * FROM (SELECT * FROM words where inplay = 1 ORDER BY priority  DESC LIMIT 3) ORDER BY RANDOM() LIMIT 1")
    fun getAllByPriority(): Flow<List<Word>>

    @Query("Select COUNT(case inplay when 1 then 1 else null end) as inplay, COUNT(*) as `all` from words join selectedwords on selectedwords.`group` = words.`group`")
    fun getWordCount(): Flow<List<wordCount>>

    @Query("Update words set inplay = 0 where id = :wordId")
    suspend fun removeFromPlay(wordId: Int)

    @Query("Update words set inplay = 1")
    suspend fun restoreAllToPlay()

    @Query("UPDATE words SET priority = (priority - :amount) WHERE id = :wordId")
    fun updatePriority(wordId: Int, amount: Int)

    @Query("UPDATE words SET flagged = :value WHERE id = :wordId")
    fun flag(wordId: Int, value: Boolean)
}
data class partialWord(val id: Int)

data class wordCount(val inplay: Int, val all: Int)

data class extendedWord(
    val id: Int,
    val word1: String,
    val word2: String,
    val priority: Int,
    val flagged: Boolean,
    val groupname: String,
    val group: Int,
    val inplay: Int
)