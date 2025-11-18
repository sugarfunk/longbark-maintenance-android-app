package com.longbark.maintenance.data.repository

import com.longbark.maintenance.data.local.dao.ClientDao
import com.longbark.maintenance.data.local.entities.ClientEntity
import com.longbark.maintenance.data.remote.api.LongBarkApiService
import com.longbark.maintenance.domain.model.Client
import com.longbark.maintenance.domain.repository.ClientRepository
import com.longbark.maintenance.util.Result
import com.longbark.maintenance.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val apiService: LongBarkApiService,
    private val clientDao: ClientDao
) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> =
        clientDao.getAllClients().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getClientById(clientId: String): Result<Client> {
        return when (val result = safeApiCall { apiService.getClientById(clientId) }) {
            is Result.Success -> {
                val client = result.data.toDomain()
                clientDao.insertClient(ClientEntity.fromDomain(client))
                Result.Success(client)
            }
            is Result.Error -> {
                val cachedClient = clientDao.getClientById(clientId)
                if (cachedClient != null) {
                    Result.Success(cachedClient.toDomain())
                } else {
                    result
                }
            }
            is Result.Loading -> result
        }
    }

    override suspend fun refreshClients(): Result<Unit> {
        return when (val result = safeApiCall { apiService.getClients() }) {
            is Result.Success -> {
                val clients = result.data.clients.map { ClientEntity.fromDomain(it.toDomain()) }
                clientDao.insertClients(clients)
                Result.Success(Unit)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}
