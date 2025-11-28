package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.meetingcoach.leadershipconversationcoach.domain.models.BehavioralTendency

@Entity(tableName = "stakeholders")
data class StakeholderEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val role: String,
    val relationship: String,
    val tendenciesJson: String, // Stored as JSON
    val lastInteraction: Long?
)

class StakeholderConverters {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, BehavioralTendency::class.java)
    private val adapter = moshi.adapter<List<BehavioralTendency>>(listType)

    @TypeConverter
    fun fromTendenciesList(value: List<BehavioralTendency>?): String {
        return adapter.toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toTendenciesList(value: String): List<BehavioralTendency> {
        return try {
            adapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
