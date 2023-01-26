package com.example.randomfactsapp.repository

import com.example.randomfactsapp.api.FactsApi
import com.example.randomfactsapp.database.FactDao
import com.example.randomfactsapp.model.Fact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val factsApi: FactsApi,
    private val factDao: FactDao
) : Repository {

    override suspend fun getRandomFact() = factsApi.getRandomFact()

    override suspend fun getDailyFact() = factsApi.getDailyFact()

    override suspend fun saveFact(fact: Fact) {
        factDao.insertFact(fact)
    }

    override fun readFacts(): Flow<List<Fact>> = factDao.getAllFacts()

    override suspend fun deleteFact(fact: Fact) {
        factDao.deleteFact(fact)
    }

    override suspend fun checkFactSaved(id: String) = factDao.isFactSaved(id)
}