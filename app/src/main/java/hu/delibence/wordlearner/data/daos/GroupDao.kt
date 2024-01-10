package hu.delibence.wordlearner.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import hu.delibence.wordlearner.data.entities.Group
import hu.delibence.wordlearner.data.entities.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Insert
    suspend fun Create(group: Group)

    @Delete(entity = Group::class)
    suspend fun Delete(groupId: partialGroup)

    @Update
    suspend fun Update(group: Group)

    @Query("select groups.id as id, groups.name as name, (Select COUNT(*) from words where `group` = groups.id) as words from groups")
    fun getAll(): Flow<List<extendedGroup>>

    @Query("Select * from groups where id = :groupId  Order By name ASC Limit 1")
    fun getSpecific(groupId: Int): Flow<Group>
}

data class partialGroup(val id: Int)
data class extendedGroup(val id: Int, val name: String, val words: Int)