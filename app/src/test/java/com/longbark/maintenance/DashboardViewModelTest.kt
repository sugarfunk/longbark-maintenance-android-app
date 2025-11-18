package com.longbark.maintenance

import com.longbark.maintenance.domain.model.DashboardStats
import com.longbark.maintenance.domain.repository.DashboardRepository
import com.longbark.maintenance.presentation.dashboard.DashboardUiState
import com.longbark.maintenance.presentation.dashboard.DashboardViewModel
import com.longbark.maintenance.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var dashboardRepository: DashboardRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dashboardRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboard success updates state`() = runTest {
        // Given
        val stats = DashboardStats(
            totalSites = 65,
            healthySites = 60,
            warningSites = 3,
            criticalSites = 2,
            recentAlerts = emptyList(),
            lastSyncTimestamp = System.currentTimeMillis()
        )
        
        coEvery { dashboardRepository.getDashboardStats() } returns Result.Success(stats)
        
        // When
        viewModel = DashboardViewModel(dashboardRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(65, (state as DashboardUiState.Success).stats.totalSites)
        assertEquals(60, state.stats.healthySites)
    }

    @Test
    fun `loadDashboard error updates state`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { 
            dashboardRepository.getDashboardStats() 
        } returns Result.Error(Exception(errorMessage), errorMessage)
        
        // When
        viewModel = DashboardViewModel(dashboardRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Error)
        assertEquals(errorMessage, (state as DashboardUiState.Error).message)
    }
}
