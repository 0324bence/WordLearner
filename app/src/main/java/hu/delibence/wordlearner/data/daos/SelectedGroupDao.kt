package hu.delibence.wordlearner.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hu.delibence.wordlearner.data.entities.SelectedGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedGroupDao {
    @Insert
    suspend fun Insert(selectedGroup: SelectedGroup)

    @Query("Delete from selectedwords where `group` = :groupId")
    suspend fun RemoveGroup(groupId: Int)

    @Query("Delete from selectedwords")
    suspend fun RemoveAll()

    @Query("Select * from selectedwords order by `group`")
    fun GetGroups(): Flow<List<SelectedGroup>>
}