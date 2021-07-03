package com.mateusandreatta.helpfilmes.di

import com.mateusandreatta.helpfilmes.MainActivity
import com.mateusandreatta.helpfilmes.ui.HomeFragment
import dagger.Subcomponent

@Subcomponent(modules = [])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): MainComponent
    }

    fun inject(activity : MainActivity)
    fun inject(fragment : HomeFragment)

}