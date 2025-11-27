package com.meetingcoach.leadershipconversationcoach.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Centralized JSON parsing utilities
 * 
 * Provides consistent error handling and logging for all JSON operations.
 * Replaces scattered try-catch blocks throughout the codebase.
 */
object JsonParser {
    
    private const val TAG = "JsonParser"
    
    /**
     * Parse JSON string to JSONObject
     * 
     * @param jsonString Raw JSON string (may include markdown code blocks)
     * @return Result with JSONObject or error
     */
    fun parseObject(jsonString: String?): Result<JSONObject> {
        if (jsonString.isNullOrBlank()) {
            return Result.Error("JSON string is null or empty")
        }
        
        return try {
            // Clean up JSON string (remove markdown blocks if present)
            val cleaned = jsonString.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            
            Result.Success(JSONObject(cleaned))
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to parse JSON object: ${e.message}", e)
            Result.Error("Invalid JSON format", e)
        }
    }
    
    /**
     * Parse JSON string to JSONArray
     */
    fun parseArray(jsonString: String?): Result<JSONArray> {
        if (jsonString.isNullOrBlank()) {
            return Result.Error("JSON string is null or empty")
        }
        
        return try {
            val cleaned = jsonString.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            
            Result.Success(JSONArray(cleaned))
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to parse JSON array: ${e.message}", e)
            Result.Error("Invalid JSON array format", e)
        }
    }
    
    /**
     * Safely get int from JSONObject with default
     */
    fun getInt(json: JSONObject, key: String, default: Int = 0): Int {
        return try {
            json.optInt(key, default)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get int for key '$key', using default: $default")
            default
        }
    }
    
    /**
     * Safely get string from JSONObject with default
     */
    fun getString(json: JSONObject, key: String, default: String = ""): String {
        return try {
            json.optString(key, default)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get string for key '$key', using default: $default")
            default
        }
    }
    
    /**
     * Safely get string list from JSONArray
     */
    fun getStringList(jsonArray: JSONArray?): List<String> {
        if (jsonArray == null) return emptyList()
        
        return try {
            (0 until jsonArray.length()).map { jsonArray.getString(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse string list: ${e.message}", e)
            emptyList()
        }
    }
    
    /**
     * Safely get JSONArray from JSONObject
     */
    fun getArray(json: JSONObject, key: String): JSONArray? {
        return try {
            json.optJSONArray(key)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get array for key '$key'")
            null
        }
    }
    
    /**
     * Convert string array to formatted string
     * 
     * Example: ["a", "b", "c"] -> "a | b | c"
     */
    fun arrayToString(jsonArray: JSONArray?, separator: String = " | "): String {
        if (jsonArray == null) return ""
        
        return try {
            (0 until jsonArray.length())
                .joinToString(separator) { jsonArray.getString(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to convert array to string: ${e.message}", e)
            ""
        }
    }
}
