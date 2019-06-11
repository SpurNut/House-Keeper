package com.spurnut.housekeeper.database.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TaskViewModelFactory(private val mApplication: Application, private val mExtra: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(mApplication, mExtra) as T
    }
}