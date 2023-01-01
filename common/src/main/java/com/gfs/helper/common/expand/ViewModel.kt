package com.gfs.helper.common.expand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T : ViewModel> ViewModelStoreOwner.createViewModel() : T {
    return ViewModelProvider(this)[T::class.java]
}

fun ViewModel.launch(
    errorLiveData: MutableLiveData<Exception>? = null,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> Unit
) {
    viewModelScope.launch(coroutineDispatcher) {
        try {
            block.invoke()
        } catch (e: Exception) {
            if (errorLiveData != null) {
                withContext(Dispatchers.Main) {
                    errorLiveData.value = e
                }
            } else {
                e.printStackTrace()
            }
        }
    }
}