package com.mateusandreatta.helpfilmes.di

import androidx.lifecycle.ViewModel
import com.mateusandreatta.helpfilmes.viewmodel.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}