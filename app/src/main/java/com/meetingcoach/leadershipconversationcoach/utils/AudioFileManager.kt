package com.meetingcoach.leadershipconversationcoach.utils

import android.content.Context
import android.util.Log
import com.meetingcoach.leadershipconversationcoach.domain.CoachingConstants
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Audio File Manager
 * 
 * Handles cleanup and management of recorded audio files.
 * Prevents disk space issues by implementing retention policies.
 */
class AudioFileManager(private val context: Context) {
    
    private val TAG = "AudioFileManager"
    
    /**
     * Get recordings directory
     */
    private fun getRecordingsDir(): File {
        return File(context.filesDir, "recordings").apply {
            if (!exists()) mkdirs()
        }
    }
    
    /**
     * Get all audio files sorted by modification time (newest first)
     */
    fun getAllAudioFiles(): List<File> {
        val dir = getRecordingsDir()
        return dir.listFiles { file ->
            file.extension in listOf("m4a", "mp3", "wav", "aac")
        }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }
    
    /**
     * Clean up old audio files based on retention policy
     * 
     * Policy:
     * 1. Keep last N files (defined in CoachingConstants)
     * 2. Delete files older than N days
     * 
     * @return Number of files deleted
     */
    fun cleanupOldFiles(): Int {
        val files = getAllAudioFiles()
        var deletedCount = 0
        
        // Keep only the last N files
        val maxFiles = CoachingConstants.Resources.MAX_AUDIO_FILES_RETAINED
        if (files.size > maxFiles) {
            files.drop(maxFiles).forEach { file ->
                if (file.delete()) {
                    deletedCount++
                    Log.d(TAG, "Deleted old audio file: ${file.name}")
                }
            }
        }
        
        // Delete files older than retention period
        val retentionMillis = TimeUnit.DAYS.toMillis(
            CoachingConstants.Resources.AUDIO_RETENTION_DAYS.toLong()
        )
        val cutoffTime = System.currentTimeMillis() - retentionMillis
        
        files.forEach { file ->
            if (file.lastModified() < cutoffTime && file.exists()) {
                if (file.delete()) {
                    deletedCount++
                    Log.d(TAG, "Deleted expired audio file: ${file.name}")
                }
            }
        }
        
        if (deletedCount > 0) {
            Log.i(TAG, "Cleanup complete: $deletedCount files deleted")
        }
        
        return deletedCount
    }
    
    /**
     * Get total size of all audio files (in MB)
     */
    fun getTotalSizeMB(): Double {
        val totalBytes = getAllAudioFiles().sumOf { it.length() }
        return totalBytes / (1024.0 * 1024.0)
    }
    
    /**
     * Delete specific audio file
     */
    fun deleteFile(file: File): Boolean {
        return try {
            if (file.exists() && file.delete()) {
                Log.d(TAG, "Deleted file: ${file.name}")
                true
            } else {
                Log.w(TAG, "Failed to delete file: ${file.name}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting file: ${e.message}", e)
            false
        }
    }
    
    /**
     * Delete all audio files (use with caution!)
     */
    fun deleteAllFiles(): Int {
        val files = getAllAudioFiles()
        var deletedCount = 0
        
        files.forEach { file ->
            if (deleteFile(file)) {
                deletedCount++
            }
        }
        
        Log.i(TAG, "Deleted all audio files: $deletedCount files")
        return deletedCount
    }
    
    /**
     * Get file info for debugging
     */
    fun getFileInfo(): String {
        val files = getAllAudioFiles()
        return """
            Audio Files Summary:
            - Total files: ${files.size}
            - Total size: ${"%.2f".format(getTotalSizeMB())} MB
            - Oldest file: ${files.lastOrNull()?.name ?: "N/A"}
            - Newest file: ${files.firstOrNull()?.name ?: "N/A"}
        """.trimIndent()
    }
}
