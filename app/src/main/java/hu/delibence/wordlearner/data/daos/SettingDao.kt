package hu.delibence.wordlearner.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hu.delibence.wordlearner.data.entities.Setting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Insert
    suspend fun Insert(setting: Setting)

    @Query("Update settings Set dark_mode = :value")
    suspend fun updateDarkMode(value: Boolean)

    @Query("update settings set use_system_theme = :value")
    suspend fun updateSystemTheme(value: Boolean)

    @Query("update settings set use_playset = :value")
    suspend fun updateUsePlayset(value: Boolean)

    @Query("update settings set positive_priority_mod = :value")
    suspend fun updatePositivePriorityMod(value: String)

    @Query("update settings set negative_priority_mod = :value")
    suspend fun updateNegativePriorityMod(value: String)

    @Query("select COUNT(*) as count from settings")
    suspend fun getRowCount(): RowCount

    @Query("Select * from settings limit 1")
    fun getSettings(): Flow<Setting>
}

data class RowCount(val count: Int)