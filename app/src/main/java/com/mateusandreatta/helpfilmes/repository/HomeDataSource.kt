package com.mateusandreatta.helpfilmes.repository

import com.mateusandreatta.helpfilmes.network.ErrorResponse
import com.mateusandreatta.helpfilmes.network.NetworkResponse
import com.mateusandreatta.helpfilmes.network.model.dto.MovieDTO
import kotlinx.coroutines.CoroutineDispatcher

interface HomeDataSource {
    suspend fun getListsOfMovies(dispatcher: CoroutineDispatcher, homeResultCallBack: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit)
}