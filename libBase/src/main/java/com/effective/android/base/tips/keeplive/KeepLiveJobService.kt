package com.effective.android.base.tips.keeplive

import android.Manifest
import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission

/**
 * 方案一 ： 使用 jobService 进行保活
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class KeepLiveJobService : JobService() {

    @RequiresPermission(allOf = [Manifest.permission.RECEIVE_BOOT_COMPLETED])
    override fun onCreate() {
        super.onCreate()
        startJobScheduler()
    }

    @RequiresPermission(allOf = [Manifest.permission.RECEIVE_BOOT_COMPLETED])
    private fun startJobScheduler() {
        try {
            val builder = JobInfo.Builder(1, ComponentName(packageName, KeepLiveJobService::class.java.name))
            builder.setPeriodic(5)
            builder.setPersisted(true)
            val jobScheduler = this.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(builder.build())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onStopJob(params: JobParameters?): Boolean = false

    override fun onStartJob(params: JobParameters?): Boolean = false
}