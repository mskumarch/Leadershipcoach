package com.meetingcoach.leadershipconversationcoach.data.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * AudioRecorder - Handles recording session audio for AI analysis
 */
class AudioRecorder(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var currentOutputFile: File? = null
    private var isRecording = false

    companion object {
        private const val TAG = "AudioRecorder"
    }

    fun startRecording(): File? {
        if (isRecording) return currentOutputFile

        val outputDir = File(context.filesDir, "recordings")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "session_$timestamp.m4a"
        val outputFile = File(outputDir, fileName)

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
                isRecording = true
                currentOutputFile = outputFile
                Log.d(TAG, "Recording started: ${outputFile.absolutePath}")
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed", e)
                return null
            } catch (e: Exception) {
                Log.e(TAG, "start() failed", e)
                return null
            }
        }
        
        return outputFile
    }

    fun stopRecording(): File? {
        if (!isRecording) return null

        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            Log.e(TAG, "stop() failed", e)
        } finally {
            mediaRecorder = null
            isRecording = false
        }
        
        Log.d(TAG, "Recording stopped. File saved: ${currentOutputFile?.length()} bytes")
        return currentOutputFile
    }

    fun pauseRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isRecording) {
            try {
                mediaRecorder?.pause()
                Log.d(TAG, "Recording paused")
            } catch (e: Exception) {
                Log.e(TAG, "pause() failed", e)
            }
        }
    }

    fun resumeRecording() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isRecording) {
            try {
                mediaRecorder?.resume()
                Log.d(TAG, "Recording resumed")
            } catch (e: Exception) {
                Log.e(TAG, "resume() failed", e)
            }
        }
    }

    fun release() {
        if (isRecording) {
            stopRecording()
        }
    }
}
