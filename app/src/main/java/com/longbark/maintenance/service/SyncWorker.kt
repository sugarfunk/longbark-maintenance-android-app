package com.longbark.maintenance.service

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.longbark.maintenance.domain.repository.ClientRepository
import com.longbark.maintenance.domain.repository.DashboardRepository
import com.longbark.maintenance.domain.repository.SiteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dashboardRepository: DashboardRepository,
    private val clientRepository: ClientRepository,
    private val siteRepository: SiteRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Sync all data
            dashboardRepository.refreshDashboard()
            clientRepository.refreshClients()
            siteRepository.refreshSites()

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        const val WORK_NAME = "sync_work"

        fun setupPeriodicSync(context: Context, intervalMinutes: Long = 15) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                intervalMinutes,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
        }

        fun cancelSync(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
