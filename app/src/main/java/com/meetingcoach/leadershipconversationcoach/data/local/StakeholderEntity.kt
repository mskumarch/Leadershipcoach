package com.meetingcoach.leadershipconversationcoach.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
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
    @TypeConverter
    fun fromTendenciesList(value: List<BehavioralTendency>?): String {
        if (value == null) return "[]"
        val jsonArray = org.json.JSONArray()
        value.forEach { item ->
            val jsonObj = org.json.JSONObject()
            jsonObj.put("type", item.type)
            jsonObj.put("frequency", item.frequency)
            jsonObj.put("strategy", item.strategy)
            jsonArray.put(jsonObj)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toTendenciesList(value: String?): List<BehavioralTendency> {
        if (value.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<BehavioralTendency>()
        try {
            val jsonArray = org.json.JSONArray(value)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                list.add(BehavioralTendency(
                    type = item.optString("type"),
                    frequency = item.optString("frequency"),
                    strategy = item.optString("strategy")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
