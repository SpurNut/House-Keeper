package com.spurnut.housekeeper.tasksscreen

interface Callback<K,V> {
    fun callbackCall(data: Map<K,V>)
}