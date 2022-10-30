package com.aditya.project.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class FetchPostWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private val TAG = FetchPostWorker::class.java.simpleName

    override fun doWork(): Result {
        return if (fetchPost()) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    private fun fetchPost(): Boolean {
        val url = URL("https://jsonplaceholder.typicode.com/posts/1")
        val connection = url.openConnection() as HttpsURLConnection
        try {
            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val scanner = Scanner(inputStreamReader)
                val s = StringBuffer()
                while (scanner.hasNext()) {
                    s.append(scanner.nextLine())
                }
                Log.i(TAG, "fetchPost() success: $s")
                connection.disconnect()
                return true
            } else {
                Log.w(TAG, "fetchPost() failed: ${connection.responseMessage}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchPost() error: $e")
        }
        return false
    }
}