package com.spurnut.housekeeper.database.viewmodel

import com.spurnut.housekeeper.database.enity.Task
import kotlinx.coroutines.Job

interface TViewModel {
    fun update(task: Task) : Job
}