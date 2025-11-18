package com.longbark.maintenance.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.longbark.maintenance.data.local.dao.NotificationDao
import com.longbark.maintenance.domain.model.AppNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationDao: NotificationDao
) : ViewModel() {
    
    val notifications: StateFlow<List<AppNotification>> = notificationDao.getAllNotifications()
        .map { entities -> entities.map { it.toDomain() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val unreadCount: StateFlow<Int> = notificationDao.getUnreadCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationDao.markAsRead(notificationId)
        }
    }
    
    fun markAllAsRead() {
        viewModelScope.launch {
            notificationDao.markAllAsRead()
        }
    }
}
