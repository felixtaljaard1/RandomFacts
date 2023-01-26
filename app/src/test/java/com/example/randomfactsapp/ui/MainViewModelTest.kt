package com.example.randomfactsapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.randomfactsapp.DummyData.getDummyFact
import com.example.randomfactsapp.model.Fact
import com.example.randomfactsapp.repo.FakeRepository
import com.example.randomfactsapp.util.Resource
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutionRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepo: FakeRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeRepository()
        viewModel = MainViewModel(fakeRepo)
    }

    @Test
    fun `daily fact and random fact api calls`() = runBlocking {
        viewModel.getRandomFact()
        viewModel.dailyFact.test {
            Assert.assertTrue(awaitItem() is Resource.Loading)
            assertEquals(getDummyFact(), awaitItem().data)
        }
        viewModel.randomFact.test {
            assertEquals(getDummyFact(), awaitItem()?.data)
        }
    }

    @Test
    fun `random fact api error`() = runBlocking {
        fakeRepo.errorResponse = true
        viewModel.getRandomFact()
        viewModel.randomFact.test {
            Assert.assertTrue(awaitItem() is Resource.Loading)
            Assert.assertTrue(awaitItem() is Resource.Error)
        }
    }

    @Test
    fun `save fact`() = runBlocking {
        viewModel.saveOrDeleteFact(getDummyFact())
        fakeRepo.readFacts().test {
            assertEquals(listOf(getDummyFact()), awaitItem())
        }
    }

    @Test
    fun `save multiple facts`() = runBlocking {
        viewModel.saveOrDeleteFact(getDummyFact("1"))
        fakeRepo.readFacts().test {
            assertEquals(listOf(getDummyFact("1")), awaitItem())
        }
        viewModel.saveOrDeleteFact(getDummyFact("2"))
        fakeRepo.readFacts().test {
            assertEquals(listOf(getDummyFact("1"), getDummyFact("2")), awaitItem())
        }
    }

    @Test
    fun `delete facts`() = runBlocking {
        viewModel.saveOrDeleteFact(getDummyFact("1"))
        fakeRepo.readFacts().test {
            assertEquals(listOf(getDummyFact("1")), awaitItem())
        }
        viewModel.saveOrDeleteFact(getDummyFact("1"))
        fakeRepo.readFacts().test {
            Assert.assertEquals(listOf<List<Fact>>(), awaitItem())
        }
    }
}