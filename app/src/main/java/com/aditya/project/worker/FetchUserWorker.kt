package com.aditya.project.worker

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class FetchUserWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        return if (fetchUser()) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    private fun fetchUser(): Boolean {
        val url = URL("https://jsonplaceholder.typicode.com/users/1")
        val connection = url.openConnection() as HttpsURLConnection
        try {
            Thread.sleep(10000)
            if (connection.responseCode == 200) {
                val inputStream = connection.inputStream
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val scanner = Scanner(inputStreamReader)
                val s = StringBuffer()
                while (scanner.hasNext()) {
                    s.append(scanner.nextLine())
                }
                Log.i(TAG, "fetchUser() success: $s")
                connection.disconnect()
                return true
            } else {
                Log.w(TAG, "fetchUser() failed: ${connection.responseMessage}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchUser() error: $e")
        }
        return false
    }
}