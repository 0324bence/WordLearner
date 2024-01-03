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

    @Query("Select * from groups Order By name ASC")
    fun getAll(): Flow<List<Group>>

    @Query("Select * from groups where id = :groupId  Order By name ASC")
    fun getSpecific(groupId: Int): Flow<List<Group>>
}

data class partialGroup(val id: Int)