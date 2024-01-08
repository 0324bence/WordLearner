package hu.delibence.wordlearner.data.repos

import androidx.room.Query
import hu.delibence.wordlearner.data.daos.SettingDao
import hu.delibence.wordlearner.data.entities.Setting

class SettingRepository(private val settingDao: SettingDao) {
    suspend fun insert(setting: Setting) = settingDao.Insert(setting)

    suspend fun updateDarkMode(value: Boolean) = settingDao.updateDarkMode(value)

    suspend fun updateSystemTheme(value: Boolean) = settingDao.updateSystemTheme(value)

    suspend fun updateUsePlayset(value: Boolean) = settingDao.updateUsePlayset(value)

    suspend fun updatePositivePriorityMod(value: String) = settingDao.updatePositivePriorityMod(value)

    suspend fun updateNegativePriorityMod(value: String) = settingDao.updateNegativePriorityMod(value)

    suspend fun getRowCount() = settingDao.getRowCount()

    fun getSettings() = settingDao.getSettings()
}