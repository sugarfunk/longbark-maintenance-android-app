package com.longbark.maintenance.domain.repository

import com.longbark.maintenance.domain.model.Client
import com.longbark.maintenance.util.Result
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun getClientById(clientId: String): Result<Client>
    suspend fun refreshClients(): Result<Unit>
}
