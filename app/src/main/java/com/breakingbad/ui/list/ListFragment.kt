package com.breakingbad.ui.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.breakingbad.R
import com.breakingbad.common.createViewModelFactory
import com.breakingbad.common.Result

/**
 * A List [Fragment] that will display all characters.
 */
class ListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel by navGraphViewModels<ListViewModel>(R.id.nav_graph) {
        createViewModelFactory { ListViewModel() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        viewModel.getCharacters().observe(viewLifecycleOwner, Observer {
            val text = when (it) {
                is Result.Error -> "Error: ${getString(it.messageIdRes)}"
                is Result.Success -> "Success: ${it.data.size}"
            }
            Log.i("test", text)
        })
    }
}
