package hu.delibence.wordlearner.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert
    suspend fun Create(group: Group)

    @Delete(entity = Group::class)
    fun Delete(groupId: partialGroup)

    @Query("select groups.id as id, groups.name as name, COUNT((Select word1 from words where `group` = groups.id)) as words from groups group by groups.id")
    fun getAll(): Flow<List<extendedGroup>>

    @Query("Select * from groups where id = :groupId  Order By name ASC")
    fun getSpecific(groupId: Int): Flow<List<Group>>
}

data class partialGroup(val id: Int)
data class extendedGroup(val id: Int, val name: String, val words: Int)