package com.meetingcoach.leadershipconversationcoach.data.repository

import com.meetingcoach.leadershipconversationcoach.data.local.StakeholderConverters
import com.meetingcoach.leadershipconversationcoach.data.local.StakeholderDao
import com.meetingcoach.leadershipconversationcoach.data.local.StakeholderEntity
import com.meetingcoach.leadershipconversationcoach.domain.models.Stakeholder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StakeholderRepository @Inject constructor(
    private val stakeholderDao: StakeholderDao
) {
    private val converter = StakeholderConverters()

    fun getAllStakeholders(): Flow<List<Stakeholder>> {
        return stakeholderDao.getAllStakeholders().map { entities ->
            entities.map { entity ->
                Stakeholder(
                    id = entity.id,
                    name = entity.name,
                    role = entity.role,
                    relationship = entity.relationship,
                    tendencies = converter.toTendenciesList(entity.tendenciesJson),
                    lastInteraction = entity.lastInteraction
                )
            }
        }
    }

    suspend fun addStakeholder(stakeholder: Stakeholder) = withContext(Dispatchers.IO) {
        val entity = StakeholderEntity(
            id = stakeholder.id,
            name = stakeholder.name,
            role = stakeholder.role,
            relationship = stakeholder.relationship,
            tendenciesJson = converter.fromTendenciesList(stakeholder.tendencies),
            lastInteraction = stakeholder.lastInteraction
        )
        stakeholderDao.insertStakeholder(entity)
    }

    suspend fun getStakeholder(id: String): Stakeholder? = withContext(Dispatchers.IO) {
        val entity = stakeholderDao.getStakeholderById(id) ?: return@withContext null
        Stakeholder(
            id = entity.id,
            name = entity.name,
            role = entity.role,
            relationship = entity.relationship,
            tendencies = converter.toTendenciesList(entity.tendenciesJson),
            lastInteraction = entity.lastInteraction
        )
    }
}
