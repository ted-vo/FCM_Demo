package com.horical.demofcm

import android.util.Log
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.horical.demofcm.extension.tag

/**
 * Created by Nhơn Võ on 5/26/18
 */
class MyJobService : JobService() {

    companion object {
        val TAG: String = MyJobService::class.java.simpleName.tag()
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        Log.d(TAG, "Performing long running task in scheduled job")
        return false
    }

    override fun onStartJob(job: JobParameters?): Boolean = false
}