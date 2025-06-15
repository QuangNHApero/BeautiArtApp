package com.example.beautisdk.utils

import android.util.Log

object TimeLogger {
    private val timeMap = mutableMapOf<String, Long>()

    fun start(tag: String) {
        timeMap[tag] = System.currentTimeMillis()
        Log.d(tag, "🟢 Start time")
    }

    fun end(tag: String, message: String = "Completed") {
        val start = timeMap[tag]
        val now = System.currentTimeMillis()

        if (start != null) {
            val elapsed = now - start
            Log.d(tag, "✅ $message - elapsed: ${elapsed}ms")
            timeMap.remove(tag)
        } else {
            Log.w(tag, "⚠️ Call start(tag) before end(tag)")
        }
    }
}