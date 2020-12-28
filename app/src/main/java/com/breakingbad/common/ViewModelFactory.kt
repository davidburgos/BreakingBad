package com.breakingbad.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class ViewModelFactory(private val viewModelBlock: () -> ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModelBlock() as T
    }
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.createViewModelFactory(noinline viewModelBlock: () -> T) =
    ViewModelFactory {
        viewModelBlock()
    }
