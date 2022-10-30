package com.aditya.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.aditya.project.worker.FetchPostWorker
import com.aditya.project.worker.FetchUserWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Defining Constraints for building Work Requests
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Defining Fetch User Work Request
        val fetchUserWorkRequest = OneTimeWorkRequestBuilder<FetchUserWorker>()
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        // Defining Fetch Post Work Request
        val fetchPostWorkRequest = OneTimeWorkRequestBuilder<FetchPostWorker>()
            .setConstraints(constraints)
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        // Initializing Work Manager and implementing Work Chaining
        WorkManager.getInstance(this)
            .beginWith(fetchUserWorkRequest)
            .then(fetchPostWorkRequest)
            .enqueue()
    }
}