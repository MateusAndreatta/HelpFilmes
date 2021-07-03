package com.mateusandreatta.helpfilmes.repository

import com.mateusandreatta.helpfilmes.AppConstants
import com.mateusandreatta.helpfilmes.network.ErrorResponse
import com.mateusandreatta.helpfilmes.network.NetworkResponse
import com.mateusandreatta.helpfilmes.network.TmdbApi
import com.mateusandreatta.helpfilmes.network.model.dto.MovieDTO
import com.mateusandreatta.helpfilmes.network.model.dto.MovieResponseDTO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(private val tmdbApi: TmdbApi): HomeDataSource{
    override suspend fun getListsOfMovies(
        dispatcher: CoroutineDispatcher,
        homeResultCallBack: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit
    ) {
        withContext(dispatcher){
            try{
                val trendingMoviesResponse = async { tmdbApi.getTrending(AppConstants.LANGUAGE, 1) }
                val upComingMoviesResponse = async { tmdbApi.getUpcoming(AppConstants.LANGUAGE, 1) }
                val popularMoviesResponse = async { tmdbApi.getPopular(AppConstants.LANGUAGE, 1) }
                val topRatedMoviesResponse = async { tmdbApi.getTopRated(AppConstants.LANGUAGE, 1) }

                processData(
                    homeResultCallBack,
                    trendingMoviesResponse.await(),
                    upComingMoviesResponse.await(),
                    popularMoviesResponse.await(),
                    topRatedMoviesResponse.await()
                )

            }catch (e: Exception){
                throw e
            }

        }
    }

    private fun processData(
        homeResultCallBack: (result: NetworkResponse<List<List<MovieDTO>>, ErrorResponse>) -> Unit,
        trending: NetworkResponse<MovieResponseDTO, ErrorResponse>,
        upComing: NetworkResponse<MovieResponseDTO, ErrorResponse>,
        popular: NetworkResponse<MovieResponseDTO, ErrorResponse>,
        topRated: NetworkResponse<MovieResponseDTO, ErrorResponse>) {

        val pair1 = buildResponse(trending)
        val pair2 = buildResponse(upComing)
        val pair3 = buildResponse(popular)
        val pair4 = buildResponse(topRated)

        when{
            pair1.first == null -> {
                pair1.second?.let{homeResultCallBack(it)}
                return
            }
            pair2.first == null -> {
                pair1.second?.let{homeResultCallBack(it)}
                return
            }
            pair3.first == null -> {
                pair1.second?.let{homeResultCallBack(it)}
                return
            }
            pair4.first == null -> {
                pair1.second?.let{homeResultCallBack(it)}
                return
            }
            else -> {
                val resultList = ArrayList<List<MovieDTO>>()
                pair1.first?.let{resultList.add(it)}
                pair2.first?.let{resultList.add(it)}
                pair3.first?.let{resultList.add(it)}
                pair4.first?.let{resultList.add(it)}
                homeResultCallBack(NetworkResponse.Success(resultList))
            }
        }
    }

    private fun buildResponse(response: NetworkResponse<MovieResponseDTO, ErrorResponse>):
            Pair<List<MovieDTO>?, NetworkResponse<List<List<MovieDTO>>,ErrorResponse>?> {
        return when(response){
            is NetworkResponse.Success -> {
                Pair(response.body.results, null)
            }
            is NetworkResponse.ApiError -> {
                Pair(null, NetworkResponse.ApiError(response.body, response.code))
            }
            is NetworkResponse.NetworkError -> {
                Pair(null, NetworkResponse.NetworkError(IOException()))
            }
            is NetworkResponse.UnknowError -> {
                Pair(null, NetworkResponse.UnknowError(Throwable()))
            }
        }
    }
}