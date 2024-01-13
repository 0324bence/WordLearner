package hu.delibence.wordlearner.data.repos

import hu.delibence.wordlearner.data.daos.GroupDao
import hu.delibence.wordlearner.data.daos.extendedGroup
import hu.delibence.wordlearner.data.daos.partialGroup
import hu.delibence.wordlearner.data.entities.Group
import kotlinx.coroutines.flow.Flow

class GroupRepository(private val groupDao: GroupDao) {
    suspend fun CreateGroup(group: Group) = groupDao.Create(group)

    suspend fun DeleteGroup(groupId: partialGroup) = groupDao.Delete(groupId)

    suspend fun UpdateGroup(group: Group) = groupDao.Update(group)

    fun GetAllGroups(): Flow<List<extendedGroup>> = groupDao.getAll()

    fun getSpecificGroup(groupId: Int): Flow<Group> = groupDao.getSpecific(groupId)

    fun getSpecificGroupByName(name: String) = groupDao.getSpecificByName(name)
}