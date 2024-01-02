package hu.delibence.wordlearner.data.repos

import hu.delibence.wordlearner.data.daos.GroupDao
import hu.delibence.wordlearner.data.entities.Group

class GroupRepository(private val groupDao: GroupDao) {
    suspend fun CreateGroup(group: Group) = groupDao.Create(group)

    suspend fun DeleteGroup(group: Group) = groupDao.Delete(group)

    fun GetAllGroups() = groupDao.getAll()

    fun getSpecificGroup(groupId: Int) = groupDao.getSpecific(groupId)
}