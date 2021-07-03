package com.mateusandreatta.helpfilmes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mateusandreatta.helpfilmes.AppConstants
import com.mateusandreatta.helpfilmes.network.ErrorResponse
import com.mateusandreatta.helpfilmes.network.NetworkResponse
import com.mateusandreatta.helpfilmes.network.TmdbApi
import com.mateusandreatta.helpfilmes.network.model.dto.MovieDTO
import com.mateusandreatta.helpfilmes.network.model.dto.MovieResponseDTO
import com.mateusandreatta.helpfilmes.repository.HomeDataSource
import com.mateusandreatta.helpfilmes.repository.HomeDataSourceImpl
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()
    private var homeDataSourceMock: HomeDataSourceMock? = null
    private var moviesListMock : List<MovieDTO> = listOf(MovieDTO(0,"backdrop.jpg","poster.jpg", "title"))
    private var listsOfMoviesMock : List<List<MovieDTO>> = listOf(moviesListMock,moviesListMock,moviesListMock,moviesListMock)

    @Before
    fun init(){
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when LISTS OF MOVIES request returns with SUCCESS expect live data lists filled`() = dispatcher.runBlockingTest {

        // Arrange
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.Success(listsOfMoviesMock))
        val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

        // Act
        viewModel.getListOfMovies()

        // Assert
        assertEquals(listsOfMoviesMock, viewModel.listOfMovies?.value)
        assertEquals(false, viewModel.isLoading?.value)
        assertEquals(false, viewModel.errorMessageVisibility?.value)
    }

    @Test
    fun `when LISTS OF MOVIES request returns with API ERROR expect ERROR live data filled`() = dispatcher.runBlockingTest {

        // Arrange
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.ApiError(ErrorResponse(), 0))
        val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

        // Act
        viewModel.getListOfMovies()

        // Assert
        assertEquals(null, viewModel.listOfMovies?.value)
        assertEquals(false, viewModel.isLoading?.value)
        assertEquals(true, viewModel.errorMessageVisibility?.value)
        assertEquals(AppConstants.API_ERROR_MESSAGE, viewModel.errorMessage?.value)
    }

    @Test
    fun `when LISTS OF MOVIES request returns with NETWORK ERROR expect ERROR live data filled`() = dispatcher.runBlockingTest {

        // Arrange
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.NetworkError(IOException()))
        val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

        // Act
        viewModel.getListOfMovies()

        // Assert
        assertEquals(null, viewModel.listOfMovies?.value)
        assertEquals(false, viewModel.isLoading?.value)
        assertEquals(true, viewModel.errorMessageVisibility?.value)
        assertEquals(AppConstants.NETWORK_ERROR_MESSAGE, viewModel.errorMessage?.value)
    }

    @Test
    fun `when LISTS OF MOVIES request returns with UNKNOW ERROR expect ERROR live data filled`() = dispatcher.runBlockingTest {

        // Arrange
        homeDataSourceMock = HomeDataSourceMock(NetworkResponse.UnknowError(Throwable()))
        val viewModel = HomeViewModel(homeDataSourceMock!!, dispatcher)

        // Act
        viewModel.getListOfMovies()

        // Assert
        assertEquals(null, viewModel.listOfMovies?.value)
        assertEquals(false, viewModel.isLoading?.value)
        assertEquals(true, viewModel.errorMessageVisibility?.value)
        assertEquals(AppConstants.UNEXPECTED_ERROR_MESSAGE, viewModel.errorMessage?.value)
    }

}
// https://www.youtube.com/watch?v=5flUmnjRyLU&list=PLVft2c8X4CuE1OUBheR-WWyCkSegczvP1&index=14&ab_channel=RotaDev
class HomeDataSourceMock(private val result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) : HomeDataSource{
    override suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallBack: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit
    ) {
        homeResultCallBack(result)
    }

}