package com.mateusandreatta.helpfilmes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateusandreatta.helpfilmes.AppConstants
import com.mateusandreatta.helpfilmes.di.IODispatcher
import com.mateusandreatta.helpfilmes.network.NetworkResponse
import com.mateusandreatta.helpfilmes.network.model.dto.MovieDTO
import com.mateusandreatta.helpfilmes.repository.HomeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeDataSource: HomeDataSource,@IODispatcher private val dispatcher : CoroutineDispatcher) : ViewModel() {

    private val _listsOfMovies: MutableLiveData<List<List<MovieDTO>>>? = MutableLiveData()
    val listOfMovies: LiveData<List<List<MovieDTO>>>? = _listsOfMovies

    private val _errorMessage: MutableLiveData<String>? = MutableLiveData()
    val errorMessage: LiveData<String>? = _errorMessage

    private val _errorMessageVisibility: MutableLiveData<Boolean>? = MutableLiveData()
    val errorMessageVisibility: LiveData<Boolean>? = _errorMessageVisibility

    private val _isLoading: MutableLiveData<Boolean>? = MutableLiveData()
    val isLoading: LiveData<Boolean>? = _isLoading

    init {
        getListOfMovies()
    }

    fun getListOfMovies(){
        showErrorMessage(false)

        try{
            viewModelScope.launch(dispatcher) {
                homeDataSource.getListsOfMovies(dispatcher){result ->
                    when(result){
                        is NetworkResponse.Success -> {
                            _listsOfMovies?.postValue(result.body)
                            _isLoading?.postValue(false)
                            _errorMessageVisibility?.postValue(false)
                        }
                        is NetworkResponse.NetworkError -> {
                            _isLoading?.value = false
                            _errorMessageVisibility?.value = true
                            showErrorMessage(true, AppConstants.NETWORK_ERROR_MESSAGE)
                        }
                        is NetworkResponse.ApiError -> {
                            _isLoading?.value = false
                            _errorMessageVisibility?.value = true
                            showErrorMessage(true, AppConstants.API_ERROR_MESSAGE)
                        }
                        is NetworkResponse.UnknowError -> {
                            _isLoading?.value = false
                            _errorMessageVisibility?.value = true
                            showErrorMessage(true, AppConstants.UNEXPECTED_ERROR_MESSAGE)
                        }
                    }
                }
            }

        }catch (e : Exception){
            throw e
        }

    }

    private fun showErrorMessage(show: Boolean, msg: String? = null){
        _isLoading?.postValue(!show)
        _errorMessageVisibility?.postValue(show)
        _errorMessage?.postValue(msg)
    }

}