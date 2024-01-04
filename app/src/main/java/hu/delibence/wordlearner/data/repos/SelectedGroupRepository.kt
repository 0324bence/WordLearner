package hu.delibence.wordlearner.data.repos

import hu.delibence.wordlearner.data.daos.SelectedGroupDao
import hu.delibence.wordlearner.data.entities.SelectedGroup

class SelectedGroupRepository(private val selectedGroupDao: SelectedGroupDao) {
    suspend fun addGroup(groupId: Int) = selectedGroupDao.Insert(SelectedGroup(group = groupId))

    suspend fun removeGroup(groupId: Int) = selectedGroupDao.RemoveGroup(groupId)

    suspend fun removeAll() = selectedGroupDao.RemoveAll()

    fun getGroups() = selectedGroupDao.GetGroups()
}