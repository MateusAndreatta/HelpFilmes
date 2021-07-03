package com.mateusandreatta.helpfilmes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mateusandreatta.helpfilmes.di.MainComponent
import com.mateusandreatta.helpfilmes.di.MoviesApplication

class MainActivity : AppCompatActivity() {

    lateinit var mainComponent: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (applicationContext as MoviesApplication).appComponent.mainComponent().create()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}