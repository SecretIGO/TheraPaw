package com.experiments.therapaw.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _toolbarTitle = MutableLiveData<String>()
    val toolbarTitle: LiveData<String> get() = _toolbarTitle

    fun setToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }

    private val _menuActive = MutableLiveData<Array<String>>()
    val menuActive: LiveData<Array<String>> get() = _menuActive

    fun setMenuActive(active: Array<String>){
        _menuActive.value = active
    }
}