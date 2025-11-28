package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StakeholderDao {
    @Query("SELECT * FROM stakeholders")
    fun getAllStakeholders(): Flow<List<StakeholderEntity>>

    @Query("SELECT * FROM stakeholders WHERE id = :id")
    suspend fun getStakeholderById(id: String): StakeholderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStakeholder(stakeholder: StakeholderEntity)

    @Delete
    suspend fun deleteStakeholder(stakeholder: StakeholderEntity)
}
